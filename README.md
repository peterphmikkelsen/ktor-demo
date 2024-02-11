# Ktor Demo Server Application
A demo Ktor server application - a reference backend service that includes everything from database access to observability.

## Technologies

### Core
At the center of this service lies - of course - [Ktor](https://ktor.io/); an asynchronous server (and client) application framework built for Kotlin by JetBrains.

Another component that I consider part of the core is dependency injection. Now, dependency injection is a weird topic; many people - including myself - have had tough experiences with it but with [Koin](https://insert-koin.io/)
I generally haven't had any issues. Koin gives you full control over which components can get injected and there is no "magic" happening, which is the number one reason why my experience with dependency injection has been... Interesting (_\*cough\*_ - Spring - _\*cough\*_).

### Observability
Most (all?) modern, real-world services have some form of observability attached to it, be it _just_ logging or also metrics and traces. So, of course this reference service also has it. A combination of [Logback](https://logback.qos.ch/), [Micrometer](https://micrometer.io/) and [Open Telemetry](https://opentelemetry.io/)
gives us all the observaility we need.

### Database
What service is complete without a database? (probably quite a lot actually) So, for this we will use a standard Postgres database. And to access it we will be using [Exposed](https://github.com/JetBrains/Exposed), a lightweight ORM framework also built by JetBrains for Kotlin.
This combined with [Flyway](https://github.com/flyway/flyway) as the migration tool gives us a very capable setup.
