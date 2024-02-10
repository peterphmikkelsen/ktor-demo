package com.demo.users

sealed interface Status {
    data object Active : Status
    data object Inactive : Status
}