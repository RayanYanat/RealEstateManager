package com.example.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.database.RoomDb

class EstateProvider : ContentProvider() {

    val AUTHORITY = "com.example.realestatemanager.provider"
    val TABLE_NAME = EstateEntity::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun onCreate(): Boolean {
return true    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (context != null){
            val index:Long = ContentUris.parseId(uri)
            val cursor = RoomDb.getAppDatabase(context!!).estateDao().getItemsWithCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null && values != null){
            Log.e("EstateContentProvider","ContentValues : $values")
            val index  = RoomDb.getAppDatabase(context!!).estateDao().insertEstate(EstateEntity().fromContentValues(values))
            val longIndex = index.toString().toLong()
            if (longIndex != 0L){
                context!!.contentResolver.notifyChange(uri,null)
                return ContentUris.withAppendedId(uri,longIndex)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalArgumentException("You can't delete anything")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        if (context != null && values != null){
            val count = RoomDb.getAppDatabase(context!!).estateDao().updateEstate(EstateEntity().fromContentValues(values))
            context!!.contentResolver.notifyChange(uri,null)
            return count.toString().toInt()
        }

        throw IllegalArgumentException("Failed to update row into $uri")
    }
}