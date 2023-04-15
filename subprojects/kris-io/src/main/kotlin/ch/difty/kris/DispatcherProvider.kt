package ch.difty.kris

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("InjectDispatcher") // :-)
/**
 * Interface giving access to the
 */
public interface DispatcherProvider {
    public val default: CoroutineDispatcher get() = Dispatchers.Default
    public val main: CoroutineDispatcher get() = Dispatchers.Main
    public val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
    public val io: CoroutineDispatcher get() = Dispatchers.IO
}

public object DefaultDispatcherProvider : DispatcherProvider
