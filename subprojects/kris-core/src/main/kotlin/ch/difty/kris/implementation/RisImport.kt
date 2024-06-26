package ch.difty.kris.implementation

import ch.difty.kris.KRisException
import ch.difty.kris.TAG_SEPARATOR
import ch.difty.kris.domain.RisRecord
import ch.difty.kris.domain.RisTag
import ch.difty.kris.domain.RisType
import ch.difty.kris.truncatedTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow

private val TAG_LENGTH = RisTag.ER.name.length
private val START_INDEX_VALUE = TAG_LENGTH + TAG_SEPARATOR.length

internal object RisImport {

    /**
     * Converts a flow of [String]s, conforming to the RIS format into a flow of [RisRecord]s.
     */
    internal fun process(lineFlow: Flow<String>): Flow<RisRecord> = flow {
        var record = RisRecord()
        var previousTag: RisTag? = null

        lineFlow.filterNotNull()
            .filterNot { line -> line.isEmpty() }
            .collect { l ->
                val line = l.removeSuffix("\r\n").removeSuffix("\n").removeSuffix("\r")
                if (line.isEndOfRecord()) {
                    emit(record)
                    record = RisRecord()
                } else {
                    previousTag = record.fillFrom(line, previousTag)
                }
            }
    }

    private fun String.isEndOfRecord() = startsWith(RisTag.ER.name)

    /**
     * Fills the tag and the respective value into the [RisRecord] (receiver).
     * Returns the tag as context for parsing the next line.
     */
    private fun RisRecord.fillFrom(line: String, previousTag: RisTag?): RisTag? {
        if (line.length < START_INDEX_VALUE + 1)
            return previousTag

        val tagName = line.substring(0, TAG_LENGTH)
        val tag = RisTag.fromName(tagName)
            ?: if (previousTag == RisTag.AB) RisTag.AB
            else throw KRisException("Unable to parse tag '$tagName'")
        tag.setInto(this, tag.typeSafeValueFrom(line))
        return tag
    }

    private fun RisTag.typeSafeValueFrom(line: String): Any {
        val rawValue: String = line.substring(START_INDEX_VALUE).trim()
        return when (kClass) {
            RisType::class -> RisType.valueOf(rawValue)
            Long::class -> rawValue.toLong()
            else -> rawValue.truncatedTo(maxLength)
        }
    }
}
