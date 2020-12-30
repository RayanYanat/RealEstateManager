package com.example.realestatemanager.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estateInfo")
data class EstateEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "surface") val surface: String,
    @ColumnInfo(name = "nb piece") val nbPiece: String,
    @ColumnInfo(name = "nb bathroom") val nbBathroom: String,
    @ColumnInfo(name = "nbBedroom") val nbBedroom: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "point of interest") val pointOfInterest: String?,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "entry date") val entryDate: String,
    @ColumnInfo(name = "date of sale") val dateOfSale: String?,
    @ColumnInfo(name = "agent name") val agentName: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "Photo") val name: String?

    )
