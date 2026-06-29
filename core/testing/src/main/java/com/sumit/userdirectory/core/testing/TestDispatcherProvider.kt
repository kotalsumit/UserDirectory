package com.sumit.userdirectory.core.testing

import com.sumit.userdirectory.core.common.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {
    override val default: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val main: CoroutineDispatcher = testDispatcher
}
