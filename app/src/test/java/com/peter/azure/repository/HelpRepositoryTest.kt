package com.peter.azure.repository

import com.peter.azure.data.entity.Help
import com.peter.azure.data.repository.HelpRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class HelpRepositoryTest {

//    private val helpRepository = HelpRepository()
//
//    private val helpList = listOf(
//        Help(Help.Catalog.FAQ, "faq 1", "text for faq 1"),
//        Help(Help.Catalog.FAQ, "faq 2", "text for faq 2"),
//        Help(Help.Catalog.FAQ, "faq 3", "text for faq 3"),
//        Help(Help.Catalog.TUTORIAL, "TUTORIAL 4", "text for TUTORIAL 4"),
//        Help(Help.Catalog.TUTORIAL, "TUTORIAL 5", "text for TUTORIAL 5"),
//        Help(Help.Catalog.TUTORIAL, "TUTORIAL 6", "text for TUTORIAL 6"),
//        Help(Help.Catalog.FAQ, "faq 7", "text for faq 7"),
//        Help(Help.Catalog.TUTORIAL, "TUTORIAL 8", "text for TUTORIAL 8"),
//        Help(Help.Catalog.FAQ, "faq 9", "text for faq 9"),
//    )
//
//    @Test
//    fun `get help`() {
//        val expect = helpList.groupBy { it.catalog }
//
//        val jsonString = Json.encodeToString(helpList)
//        val result = helpRepository.getHelpMap(jsonString)
//
//        assertEquals(expect, result)
//    }

}