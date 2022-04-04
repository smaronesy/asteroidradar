package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.Filter
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val database = getInstance(application)
    private var asteroidRepository = AsteroidRepository(database)

    val asteroids = asteroidRepository.asteroids
    val pod = asteroidRepository.picOfDay

    init {
        asteroidRepository.filter.value = Filter.SAVED
        viewModelScope.launch {
            asteroidRepository.refreshPicOfTheDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    fun updateAsteroidWithFilter(filter: Filter) {
        asteroidRepository.filter.value = filter
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