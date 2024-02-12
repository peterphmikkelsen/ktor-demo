package com.demo.utils.tracing

import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.StatementInterceptor
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.getScopeName
import org.koin.java.KoinJavaComponent.inject

val tracer by inject<Tracer>(Tracer::class.java)

fun <T> traced(statement: () -> T): T {
    val name = statement.getScopeName().value
        .substringAfterLast(".")
        .replace("$1", "")
        .replace("$", ".")

    val span = tracer.spanBuilder(name).startSpan()
    return try {
        statement()
    } catch (t: Throwable) {
        span.recordException(t)
        span.setStatus(StatusCode.ERROR)
        throw t
    } finally {
        span.end()
    }
}

fun <T> tracedTransaction(includeQuery: Boolean = true, statement: Transaction.() -> T): T {
    return transaction {
        registerInterceptor(TracingInterceptor(includeQuery))
        statement()
    }
}

private class TracingInterceptor(val includeQuery: Boolean): StatementInterceptor {
    private val span = tracer.spanBuilder("exposedTransaction").startSpan()

    override fun beforeExecution(transaction: Transaction, context: StatementContext) {
        val query = context.expandArgs(TransactionManager.current())
        span.let {
            it.setAttribute("statementCount", transaction.statementCount.toLong())
            it.setAttribute("dbUrl", transaction.db.url)
            it.setAttribute("dbVendor", transaction.db.vendor)
            it.setAttribute("dbVersion", transaction.db.version.toDouble())
            if (includeQuery) it.setAttribute("query", query)
            it.setAttribute("table", context.statement.targets.map { target -> target.tableName }.toString())
        }
    }

    override fun afterExecution(
        transaction: Transaction,
        contexts: List<StatementContext>,
        executedStatement: PreparedStatementApi
    ) {
        span.end()
    }
}