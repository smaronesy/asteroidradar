package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val database = getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroids = asteroidRepository.asteroids
    val pod = asteroidRepository.picOfDay

    init {
        viewModelScope.launch {
            asteroidRepository.refreshPicOfTheDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    fun onAsteroidClicked(codeName: Asteroid){
        _navigateToAsteroidDetails.value = codeName
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetails.value = null
    }

}