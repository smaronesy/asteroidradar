package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the Asteroid class with Room.
 */
@Dao
interface AsteroidDatabaseDao {

    /**
     * Asteroid Details SQL Calls
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = DatabaseAsteroidEntity::class)
    suspend fun insert(asteroid: DatabaseAsteroidEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = DatabaseAsteroidEntity::class)
    fun insertAll(vararg asteroid: DatabaseAsteroidEntity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param asteroid new value to write
     */
    @Update(entity = DatabaseAsteroidEntity::class)
    suspend fun update(asteroid: DatabaseAsteroidEntity)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from asteroid_details WHERE id = :key")
    suspend fun get(key: Long): DatabaseAsteroidEntity?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM asteroid_details")
    suspend fun clear()

    /**
     * Selects and returns all rows in the table,
     *s
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM asteroid_details ORDER BY close_approach_date DESC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroidEntity>>

    /**
     * Picture of Day SQL calls
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = DatabasePicOfDayEntity::class)
    suspend fun insert(pod: DatabasePicOfDayEntity)

    @Query("SELECT * FROM picture_of_the_day_details ORDER BY date DESC LIMIT 1")
    fun getPicOfDay(): LiveData<DatabasePicOfDayEntity>

}
