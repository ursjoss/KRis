package ch.difty.kris

import ch.difty.kris.domain.RisRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.util.stream.Stream

/**
 * Converts the RISFile lines provided by the [Reader] as receiver into a list of RisRecords.
 * May throw an [IOException] if the reader fails to deliver lines or a [KRisException]
 * if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun Reader.process(dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
    KRisIO.process(this, dispatcher)

/**
 * Converts the RISFile lines in the [File] provided as receiver into a list of RisRecords.
 * May throw an [IOException] if the file cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun File.process(dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
    KRisIO.process(this, dispatcher)

/**
 * Converts the RISFile lines from the file with the path provided as receiver into a list of RisRecords.
 * May throw an [IOException] if the file cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun String.process(dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
    KRisIO.process(this, dispatcher)

/**
 * Converts the RISFile lines provided by the [InputStream] as receiver  into a list of RisRecords.
 * May throw an [IOException] if the stream cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun InputStream.process(dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
    KRisIO.process(this, dispatcher)

/**
 * Converts the RISFile lines provided by the [Reader] as receiver into a stream of RisRecords.
 * May throw an [IOException] if the reader fails to deliver lines or a [KRisException]
 * if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun Reader.processToStream(dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
    KRisIO.processToStream(this, dispatcher)

/**
 * Converts the RISFile lines in the [File] provided as receiver into a stream of RisRecords.
 * May throw an [IOException] if the file cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun File.processToStream(dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
    KRisIO.processToStream(this, dispatcher)

/**
 * Converts the RISFile lines from the file with the path provided as receiver into a stream of RisRecords.
 * May throw an [IOException] if the file cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun String.processToStream(dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
    KRisIO.processToStream(this, dispatcher)

/**
 * Converts the RISFile lines provided by the [InputStream] as receiver into a stream of RisRecords.
 * May throw an [IOException] if the stream cannot be read successfully,
 * or a [KRisException] if the lines cannot be parsed successfully.
 */
@JvmOverloads
public fun InputStream.processToStream(dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
    KRisIO.processToStream(this, dispatcher)
