package com.example.fitnessapp.gymific.repository

import androidx.lifecycle.LiveData
import com.ersiver.gymific.util.EspressoIdlingResource.wrapEspressoIdlingResource
import com.example.fitnessapp.gymific.db.WorkoutCategoryDao
import com.example.fitnessapp.gymific.db.WorkoutDao
import com.example.fitnessapp.gymific.model.Workout
import com.example.fitnessapp.gymific.model.WorkoutCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val categoryDao: WorkoutCategoryDao
) {
    fun getWorkouts(): Flow<List<Workout>> =
        wrapEspressoIdlingResource {
            workoutDao.getWorkouts().flowOn(Dispatchers.IO)
        }

    fun getWorkout(id: Int): LiveData<Workout> =
        wrapEspressoIdlingResource {
            workoutDao.getWorkout(id)
        }

    suspend fun update(workout: Workout) {
        wrapEspressoIdlingResource {
            withContext(Dispatchers.IO) {
                workoutDao.update(workout)
            }
        }
    }

    fun getCategories(): Flow<List<WorkoutCategory>> =
        wrapEspressoIdlingResource {
            categoryDao.getCategories().flowOn(Dispatchers.IO)
        }
}