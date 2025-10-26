package ch.difty.kris

import org.junit.jupiter.api.Test
import kotlinx.coroutines.Dispatchers
import org.assertj.core.api.Assertions.assertThat

internal class DispatcherProviderTest {

    @Test
    internal fun defaultShouldDefaultToDispatcherDefault() {
        val dp = DefaultDispatcherProvider
        assertThat(dp.default).isEqualTo(Dispatchers.Default)
    }

    @Test
    internal fun mainShouldDefaultToDispatcherMain() {
        val dp = DefaultDispatcherProvider
        assertThat(dp.main).isEqualTo(Dispatchers.Main)
    }

    @Test
    internal fun unconfinedShouldDefaultToDispatcherUnconfined() {
        val dp = DefaultDispatcherProvider
        assertThat(dp.unconfined).isEqualTo(Dispatchers.Unconfined)
    }

    @Test
    internal fun ioShouldDefaultToDispatcherIo() {
        val dp = DefaultDispatcherProvider
        assertThat(dp.io).isEqualTo(Dispatchers.IO)
    }
}
