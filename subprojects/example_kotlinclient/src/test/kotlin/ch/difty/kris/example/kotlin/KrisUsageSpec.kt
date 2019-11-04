package ch.difty.kris.example.kotlin

import com.gmail.gcolaianni5.jris.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.style.specification.describe

private const val SKIP_REASON = "Functionality not implemented yet"

object KrisUsageSpec : Spek({

    describe("with list of strings representing two RIS records") {
        // example from wikipedia (https://en.wikipedia.org/wiki/RIS_(file_format)
        // and https://de.wikipedia.org/wiki/RIS_(Dateiformat))
        val risLines: List<String> = listOf(
            "TY  - JOUR",
            "AU  - Shannon, Claude E.",
            "PY  - 1948/07//",
            "TI  - A Mathematical Theory of Communication",
            "T2  - Bell System Technical Journal",
            "SP  - 379",
            "EP  - 423",
            "VL  - 27",
            "ER  - ",
            "TY  - JOUR",
            "TI  - Die Grundlage der allgemeinen Relativitätstheorie",
            "AU  - Einstein, Albert",
            "PY  - 1916",
            "SP  - 769",
            "EP  - 822",
            "JO  - Annalen der Physik",
            "VL  - 49",
            "ER  -"
        )


        it("can be passed to a static method returning a list of RisRecords (blocking)", skip = Skip.Yes(SKIP_REASON)) {
            JRis.process(risLines) shouldHaveSize 2
        }

        describe("converted to Flow") {
            val flowOfRisLines: Flow<String> = risLines.asFlow()

            it("can be passed to a flow operator returning a flow of RisRecords (non-blocking)", skip = Skip.Yes(SKIP_REASON)) {
                runBlocking {
                    flowOfRisLines
                        .toRisRecords()
                        .toList()
                        .shouldHaveSize(2)
                }
            }
        }

        describe("converted to a Sequence") {
            val sequenceOfRisLines: Sequence<String> = risLines.asSequence()

            it("can be passed to a sequence operator returning a sequence of RisRecords (blocking)", skip = Skip.Yes(SKIP_REASON)) {
                sequenceOfRisLines
                    .toRisRecords()
                    .toList()
                    .shouldHaveSize(2)
            }

            it("can be passed to a static method returning a list of RisRecords (blocking)", skip = Skip.No) {
                JRis.process(sequenceOfRisLines) shouldHaveSize 2
            }
        }
    }

    describe("with a list with a single RisRecord") {
        val risRecord = RisRecord(
            type = RisType.JOUR,
            authors = mutableListOf("Shannon, Claude E."),
            publicationYear = "1948/07//",
            title = "A Mathematical Theory of Communication",
            secondaryTitle = "Bell System Technical Journal",
            startPage = "379",
            endPage = "423",
            volumeNumber = " 27"
        )
        val expectedLinesInFile = 9 // including ER (End of Record)

        val risRecords = listOf(risRecord)

        it("can be passed to a static method returning a list of Strings (blocking)", skip = Skip.Yes(SKIP_REASON)) {
            JRis.export(risRecords) shouldHaveSize 9
        }

        describe("converted to Flow", skip = Skip.Yes(SKIP_REASON)) {
            val flowOfRisRecords: Flow<RisRecord> = risRecords.asFlow()

            it("can be passed to a flow operator returning a flow of Strings (non-blocking)") {
                runBlocking {
                    flowOfRisRecords
                        .toRisLines()
                        .toList()
                        .shouldHaveSize(expectedLinesInFile)
                }
            }
        }

        describe("converted to a Sequence", skip = Skip.Yes(SKIP_REASON)) {
            val sequenceOfRisRecords: Sequence<RisRecord> = risRecords.asSequence()

            it("can be passed to a sequence operator returning a sequence of Strings (blocking)") {
                sequenceOfRisRecords
                    .toRisLines()
                    .toList()
                    .shouldHaveSize(expectedLinesInFile)
            }
        }


        it("can convert risRecord to a string", skip = Skip.No) {
            JRis.build(records = risRecords) shouldEqual """TY  - JOUR
                                |AU  - Shannon, Claude E.
                                |EP  - 423
                                |PY  - 1948/07//
                                |SP  - 379
                                |T2  - Bell System Technical Journal
                                |TI  - A Mathematical Theory of Communication
                                |VL  -  27
                                |ER  - 
                                |""".trimMargin()
        }
    }
})

