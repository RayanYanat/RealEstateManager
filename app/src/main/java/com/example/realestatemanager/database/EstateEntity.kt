package com.example.realestatemanager.database

import android.content.ContentValues
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.realestatemanager.utils.toFRDate
import java.util.*

@Entity(tableName = "estateInfo")
data class EstateEntity(

    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "surface") var surface: Int,
    @ColumnInfo(name = "nb piece") var nbPiece: String,
    @ColumnInfo(name = "nb bathroom") var nbBathroom: String,
    @ColumnInfo(name = "nbBedroom") var nbBedroom: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "pointOfInterest") var pointOfInterest: String?,
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "entry date") var entryDate: Date,
    @ColumnInfo(name = "date of sale") var dateOfSale: Date?,
    @ColumnInfo(name = "agent name") var agentName: String,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "Photo") var photo: String?

) {
    constructor() : this("",0,0,"","","","","","","",Date(),null,"","","")

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0


    fun fromContentValues(values: ContentValues): EstateEntity {
        val estate = EstateEntity()
        if (values.containsKey("type")) estate.type = values.getAsString("type")
        if (values.containsKey("price")) estate.price = values.getAsInteger("price")
        if (values.containsKey("surface")) estate.surface = values.getAsInteger("surface")
        if (values.containsKey("nb piece")) estate.nbPiece = values.getAsString("nb piece")
        if (values.containsKey("nb bathroom")) estate.nbBathroom =
            values.getAsString("nb bathroom")
        if (values.containsKey("nbBedroom")) estate.nbBedroom = values.getAsString("nbBedroom")
        if (values.containsKey("description")) estate.description = values.getAsString("description")
        if (values.containsKey("pointOfInterest")) estate.pointOfInterest =
            values.getAsString("pointOfInterest")
        if (values.containsKey("status")) estate.status = values.getAsString("status")
        if (values.containsKey("entry date")) estate.entryDate =
            values.getAsLong("entryDate").toFRDate()
        if (values.containsKey("date of sale")) if (values.getAsLong("date of sale") == 0L) estate.dateOfSale =
            null else estate.dateOfSale = values.getAsLong("date of sale").toFRDate()
        if (values.containsKey("city")) estate.city = values.getAsString("city")
        if (values.containsKey("agent name")) estate.agentName = values.getAsString("agent name")
        if (values.containsKey("Photo")) estate.photo = values.getAsString("Photo")


        Log.e("EstateFromContentValues", "Estate : $estate")
        return estate
    }
}

