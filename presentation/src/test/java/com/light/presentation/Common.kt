package com.light.presentation

import org.mockito.invocation.InvocationOnMock

inline fun <reified T : Any> InvocationOnMock.invocationOnSuccess(valueCase: T) {
    (arguments[0] as (T) -> Unit).invoke(valueCase)
}

inline fun <reified T : Any> InvocationOnMock.invocationOnError(valueCase: T) {
    (arguments[1] as (T) -> Unit).invoke(valueCase)
}
