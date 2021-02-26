package com.example.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.database.RoomDb
import com.example.realestatemanager.provider.EstateProvider
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.*

class ContentProviderTest {

    private lateinit var mContentResolver: ContentResolver

    private val ESTATE_ID:Long = 1
    private val ESTATE_ID_2:Long = 9999

    @Before
    fun setUp(){

        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,RoomDb::class.java)
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted(){
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateProvider().URI_ESTATE, ESTATE_ID_2),null,null,null,null)
        assertNotNull(cursor)
        assertEquals(0, cursor?.count)
        cursor?.close()
    }

    @Test
    fun insertAndGetItem(){
        // ADDING DEMO ESTATE
        mContentResolver.insert(EstateProvider().URI_ESTATE, generateEstate())

        // TEST
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateProvider().URI_ESTATE, ESTATE_ID), null,null,null,null)
        assertNotNull(cursor)
        assertEquals(1,cursor?.count)
        assertEquals(true,cursor?.moveToFirst())
        assertEquals("Adrien",cursor?.getString(cursor.getColumnIndexOrThrow("estateAgent")))
    }


    private fun generateEstate():ContentValues{
        val values = ContentValues()
        values.put("type","house")
        values.put("price",150000)
        values.put("surface",150)
        values.put("nb piece","5")
        values.put("nb bathroom","1")
        values.put("nbBedroom","3")
        values.put("description","estate")
        values.put("address","1 rue denton")
        values.put("pointOfInterest","school")
        values.put("city","Marseille")
        values.put("status","available")
        values.put("entryDate", Date().time)
        values.put("date of sale",0L)
        values.put("agent name","Adrien")
        return values
    }
}