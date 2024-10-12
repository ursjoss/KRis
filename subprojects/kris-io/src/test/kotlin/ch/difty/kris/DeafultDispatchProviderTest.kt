package ch.difty.kris

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test

internal class DeafultDispatchProviderTest {

    private val sut = DefaultDispatcherProvider

    @Test
    internal fun defaultProviderShouldBeDefault() {
        sut.default shouldBe Dispatchers.Default
    }

    @Test
    internal fun mainProviderShouldBeMain() {
        sut.main shouldBe Dispatchers.Main
    }

    @Test
    internal fun unconfinedProviderShouldBeUnconfined() {
        sut.unconfined shouldBe Dispatchers.Unconfined
    }

    @Test
    internal fun ioProviderShouldBeIo() {
        sut.io shouldBe Dispatchers.IO
    }
}
