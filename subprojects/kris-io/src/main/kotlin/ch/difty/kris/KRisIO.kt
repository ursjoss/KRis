@file:Suppress("TooManyFunctions")

package ch.difty.kris

import ch.difty.kris.domain.RisRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer
import java.util.stream.Stream

/**
 * Convenience methods offering to directly work with IO methods.
 */
public object KRisIO {

    //region:process -> RISFile lines -> RisRecords

    /**
     * Converts the RISFile lines provided by the reader into a list of [RisRecord]s.
     * May throw an [IOException] if the reader fails to deliver lines
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun process(reader: Reader, dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> = runBlocking(dispatcher) {
        val lineFlow = BufferedReader(reader).lineSequence().asFlow()
        KRis.process(lineFlow).toList()
    }

    /**
     * Converts the RISFile lines in the provided [File] into a list of [RisRecord]s.
     * May throw an [IOException] if the file cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun process(file: File, dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
        process(file.bufferedReader(), dispatcher)

    /**
     * Converts the RISFile lines from the file with the provided path into a list of [RisRecord]s.
     * May throw an [IOException] if the file cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun process(filePath: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
        process(File(filePath).bufferedReader(), dispatcher)

    /**
     * Converts the RISFile lines provided by the [InputStream] into a list of [RisRecord]s.
     * May throw an [IOException] if the stream cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun process(inputStream: InputStream, dispatcher: CoroutineDispatcher = Dispatchers.IO): List<RisRecord> =
        process(inputStream.bufferedReader(), dispatcher)

    /**
     * Converts the RISFile lines provided by the reader into a stream of [RisRecord]s.
     * May throw an [IOException] if the reader fails to deliver lines
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun processToStream(reader: Reader, dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
        runBlocking(dispatcher) {
            val lineFlow = BufferedReader(reader).lineSequence().asFlow()
            Stream.builder<RisRecord>().apply {
                KRis.process(lineFlow).onEach { add(it) }.lastOrNull()
            }.build()
        }

    /**
     * Converts the RISFile lines in the provided [File] into a stream of [RisRecord]s.
     * May throw an [IOException] if the file cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun processToStream(file: File, dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
        processToStream(file.bufferedReader(), dispatcher)

    /**
     * Converts the RISFile lines from the file with the provided path into a stream of [RisRecord]s.
     * May throw an [IOException] if the file cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun processToStream(filePath: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
        processToStream(File(filePath).bufferedReader(), dispatcher)

    /**
     * Converts the RISFile lines provided by the [InputStream] into a stream of [RisRecord]s.
     * May throw an [IOException] if the stream cannot be read successfully.
     * or a [KRisException] if the lines cannot be parsed successfully.
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    public fun processToStream(inputStream: InputStream, dispatcher: CoroutineDispatcher = Dispatchers.IO): Stream<RisRecord> =
        processToStream(inputStream.bufferedReader(), dispatcher)

    //endregion

    //region: build -> RisRecords -> RISFile lines

    /**
     * Converts a list of [RisRecord]s into a list of [String]s in RIS file format, dumping them into the
     * provided [Writer]. Optionally accepts a list of names of RisTags defining a sort order for
     * the RisTags in the file.
     */
    @JvmStatic
    @JvmOverloads
    public fun export(
        records: List<RisRecord>,
        sort: List<String> = emptyList(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        writer: Writer,
    ) {
        writer.use { w ->
            runBlocking(dispatcher) {
                KRis.build(records.asFlow(), sort)
                    .buffer(onBufferOverflow = BufferOverflow.SUSPEND)
                    .collect { line ->
                        w.write(line)
                    }
            }
        }
    }

    /**
     * Converts a list of [RisRecord]s into a list of [String]s in RIS file format,
     * writing them into the provided [File]. Optionally accepts a list of names of RisTags defining
     * a sort order for the RisTags in the file.
     */
    @JvmStatic
    @JvmOverloads
    public fun export(
        records: List<RisRecord>,
        sort: List<String> = emptyList(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        file: File,
    ) {
        FileWriter(file).use { fileWriter ->
            export(records, sort, dispatcher, fileWriter)
        }
    }

    /**
     * Converts a list of [RisRecord]s into a list of [String]s in RIS file format, writing them into
     * the provided [OutputStream]. Optionally accepts a list of names of RisTags defining a sort order
     * for the RisTags in the file.
     */
    @JvmStatic
    @JvmOverloads
    public fun export(
        records: List<RisRecord>,
        sort: List<String> = emptyList(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        out: OutputStream,
    ) {
        OutputStreamWriter(out).use { writer ->
            export(records, sort, dispatcher, writer)
        }
    }

    /**
     * Converts a list of [RisRecord]s into a list of [String]s in RIS file format, writing them into file with
     * the specified path if possible.
     * Optionally accepts a list of names of RisTags defining a sort order for the RisTags in the file.
     */
    @JvmStatic
    @JvmOverloads
    public fun export(
        records: List<RisRecord>,
        sort: List<String> = emptyList(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        filePath: String,
    ) {
        FileOutputStream(filePath).use {
            export(records, sort, dispatcher, it)
        }
    }

    //endregion
}
