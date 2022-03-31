package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay
import java.util.*

@Entity(tableName = "picture_of_the_day_details")
data class DatabasePicOfDayEntity(
    @PrimaryKey
    var url: String = "",

    @ColumnInfo(name = "date")
    val date: Int = Calendar.DATE,

    @ColumnInfo(name = "media_type")
    val mediaType: String = "",

    @ColumnInfo(name = "title")
    var title: String = ""
)

fun asDomainModel(databasePicOfDayEntity: DatabasePicOfDayEntity): PictureOfDay {
    return PictureOfDay(databasePicOfDayEntity.mediaType, databasePicOfDayEntity.title, databasePicOfDayEntity.url)
}

fun asDatabaseObject(pictureOfDay: PictureOfDay): DatabasePicOfDayEntity {
    return DatabasePicOfDayEntity(pictureOfDay.url, Calendar.DATE, pictureOfDay.mediaType, pictureOfDay.title)
}
