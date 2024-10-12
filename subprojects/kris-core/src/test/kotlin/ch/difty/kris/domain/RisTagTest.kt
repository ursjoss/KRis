package ch.difty.kris.domain

import org.amshove.kluent.shouldContainAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RisTagTest {

    @Test
    @Suppress("LongMethod")
    fun description() {
        RisTag.entries.map { it.description } shouldContainAll
            listOf(
                "Type of reference",
                "First Author",
                "Secondary Author",
                "Tertiary Author",
                "Subsidiary Author",
                "Abstract",
                "Author Address",
                "Accession Number",
                "Author",
                "Location in Archives",
                "This field maps to T2 for all reference types except for Whole Book and Unpublished Work references.",
                "Custom 1",
                "Custom 2",
                "Custom 3",
                "Custom 4",
                "Custom 5",
                "Custom 6",
                "Custom 7",
                "Custom 8",
                "Caption",
                "Call Number",
                "CP - This field can contain alphanumeric characters.",
                "Title of unpublished reference",
                "Place Published",
                "Date",
                "Name of Database",
                "DOI",
                "Database Provider",
                "Editor",
                "End Page",
                "Edition",
                "Reference ID",
                "Issue number",
                "Periodical name: user abbreviation 1",
                "Alternate Title",
                "Periodical name: standard abbreviation",
                "Journal/Periodical name: full format",
                "Journal/Periodical name: full format",
                "Keywords",
                "Link to PDF",
                "Link to Full-text",
                "Related Records",
                "Image(s)",
                "Language",
                "Label",
                "Website Link",
                "Miscellaneous 1 (often Number)",
                "Miscellaneous 2",
                "Miscellaneous 3 (often Type of Work)",
                "Notes",
                "Abstract",
                "Number of Volumes",
                "Original Publication",
                "Publisher",
                "Publishing Place",
                "Publication year (YYYY)",
                "Reviewed Item",
                "Research Notes",
                "Reprint Edition",
                "Section",
                "ISBN/ISSN",
                "Start Page",
                "Short Title",
                "Primary Title",
                "Secondary Title (journal title, if applicable)",
                "Tertiary Title",
                "Translated Author",
                "Title",
                "Translated Title",
                "User definable 1",
                "User definable 2",
                "User definable 3",
                "User definable 4",
                "User definable 5",
                "URL",
                "Volume number",
                "Published Standard number",
                "Primary Date",
                "Access Date",
                "End of Reference",
            )
    }


    @Test
    fun settingRisType() {
        val rr = RisRecord()
        RisTag.TY.setInto(rr, RisType.BOOK)
        assertThat(rr.type).isEqualTo(RisType.BOOK)
    }

    @Test
    fun settingMultipleAuthors_setsThemInOrder() {
        val rr = RisRecord()
        mapOf(
            RisTag.A1 to RisRecord::firstAuthors,
            RisTag.A2 to RisRecord::secondaryAuthors,
            RisTag.A3 to RisRecord::tertiaryAuthors,
            RisTag.A4 to RisRecord::subsidiaryAuthors,
            RisTag.AU to RisRecord::authors,
        ).forEach { (risTag: RisTag, getter: (RisRecord) -> List<String>) ->
            risTag.setInto(rr, "Bond, J.")
            risTag.setInto(rr, "Duck, D.")
            assertThat(getter(rr)).containsAll(listOf("Bond, J.", "Duck, D."))
        }
    }

    @Test
    fun settingStringValues() {
        val rr = RisRecord()
        mapOf(
            RisTag.AB to RisRecord::abstr,
            RisTag.AD to RisRecord::authorAddress,
            RisTag.AN to RisRecord::accessionNumber,
            RisTag.AV to RisRecord::archivesLocation,
            RisTag.BT to RisRecord::bt,
            RisTag.C1 to RisRecord::custom1,
            RisTag.C2 to RisRecord::custom2,
            RisTag.C3 to RisRecord::custom3,
            RisTag.C4 to RisRecord::custom4,
            RisTag.C5 to RisRecord::custom5,
            RisTag.C6 to RisRecord::custom6,
            RisTag.C7 to RisRecord::custom7,
            RisTag.C8 to RisRecord::custom8,
            RisTag.CA to RisRecord::caption,
            RisTag.CN to RisRecord::callNumber,
            RisTag.CP to RisRecord::cp,
            RisTag.CT to RisRecord::unpublishedReferenceTitle,
            RisTag.CY to RisRecord::placePublished,
            RisTag.DA to RisRecord::date,
            RisTag.DB to RisRecord::databaseName,
            RisTag.DO to RisRecord::doi,
            RisTag.DP to RisRecord::databaseProvider,
            RisTag.ED to RisRecord::editor,
            RisTag.EP to RisRecord::endPage,
            RisTag.ET to RisRecord::edition,
            RisTag.ID to RisRecord::referenceId,
            RisTag.IS to RisRecord::issue,
            RisTag.J1 to RisRecord::periodicalNameUserAbbrevation,
            RisTag.J2 to RisRecord::alternativeTitle,
            RisTag.JA to RisRecord::periodicalNameStandardAbbrevation,
            RisTag.JF to RisRecord::periodicalNameFullFormatJF,
            RisTag.JO to RisRecord::periodicalNameFullFormatJO,
            RisTag.LA to RisRecord::language,
            RisTag.LB to RisRecord::label,
            RisTag.LK to RisRecord::websiteLink,
            RisTag.M1 to RisRecord::miscellaneous1,
            RisTag.M2 to RisRecord::miscellaneous2,
            RisTag.M3 to RisRecord::miscellaneous3,
            RisTag.N1 to RisRecord::notes,
            RisTag.N2 to RisRecord::abstr2,
            RisTag.NV to RisRecord::numberOfVolumes,
            RisTag.PB to RisRecord::publisher,
            RisTag.PP to RisRecord::publishingPlace,
            RisTag.PY to RisRecord::publicationYear,
            RisTag.RI to RisRecord::reviewedItem,
            RisTag.RN to RisRecord::researchNotes,
            RisTag.RP to RisRecord::reprintEdition,
            RisTag.SE to RisRecord::section,
            RisTag.SN to RisRecord::isbnIssn,
            RisTag.SP to RisRecord::startPage,
            RisTag.ST to RisRecord::shortTitle,
            RisTag.T1 to RisRecord::primaryTitle,
            RisTag.T2 to RisRecord::secondaryTitle,
            RisTag.T3 to RisRecord::tertiaryTitle,
            RisTag.TA to RisRecord::translatedAuthor,
            RisTag.TI to RisRecord::title,
            RisTag.TT to RisRecord::translatedTitle,
            RisTag.U1 to RisRecord::userDefinable1,
            RisTag.U2 to RisRecord::userDefinable2,
            RisTag.U3 to RisRecord::userDefinable3,
            RisTag.U4 to RisRecord::userDefinable4,
            RisTag.U5 to RisRecord::userDefinable5,
            RisTag.UR to RisRecord::url,
            RisTag.VL to RisRecord::volumeNumber,
            RisTag.VO to RisRecord::publisherStandardNumber,
            RisTag.Y1 to RisRecord::primaryDate,
            RisTag.Y2 to RisRecord::accessDate,
        ).forEach { (risTag: RisTag, getter: (RisRecord) -> String?) ->
            risTag.setInto(rr, risTag.name)
            assertThat(getter(rr)).isEqualTo(risTag.name)
        }
    }

    @Test
    fun settingMultipleKeywords_setsThemInOrder() {
        val rr = RisRecord()
        mapOf(
            RisTag.KW to RisRecord::keywords,
            RisTag.L1 to RisRecord::pdfLinks,
            RisTag.L2 to RisRecord::fullTextLinks,
            RisTag.L3 to RisRecord::relatedRecords,
            RisTag.L4 to RisRecord::images,
        ).forEach { (risTag: RisTag, getter: (RisRecord) -> List<String>) ->
            risTag.setInto(rr, "${risTag}1")
            risTag.setInto(rr, "${risTag}2")
            assertThat(getter(rr)).containsAll(listOf("${risTag}1", "${risTag}2"))
        }
    }

    @Test
    fun erShouldSetNothing() {
        val rr = RisRecord()
        RisTag.ER.setInto(rr, "Whatever")
        assertThat(rr.toString()).isEqualTo(RisRecord().toString())
    }

    @Test
    fun erShouldBeLastElement() {
        assertThat(RisTag.entries.last()).isEqualTo(RisTag.ER)
    }

}
