package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import java.util.*

@Entity(tableName = "asteroid_details")
data class DatabaseAsteroidEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "code_name")
    val codeName: String = "",

    @ColumnInfo(name = "close_approach_date")
    var closeApproachDate: String = "",

    @ColumnInfo(name = "absolute_magnitude")
    var absoluteMagnitude: Double = -1.0,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double = -1.0,

    @ColumnInfo(name = "relative_velocity")
    var relativeVelocity: Double = -1.0,

    @ColumnInfo(name = "distance_from_earth")
    var distanceFromEarth: Double = -1.0,

    @ColumnInfo(name = "is_potentially_hazardous")
    var isPotentiallyHazardous: Boolean = false
)

fun List<DatabaseAsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseObject(): Array<DatabaseAsteroidEntity> {
    return map {
        DatabaseAsteroidEntity(
            id = it.id,
            codeName = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
