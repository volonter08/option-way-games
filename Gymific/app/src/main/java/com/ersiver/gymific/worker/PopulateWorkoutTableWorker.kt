package com.ersiver.gymific.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ersiver.gymific.R
import com.ersiver.gymific.db.GymificDatabase
import com.ersiver.gymific.model.Workout
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.reflect.TypeToken
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

@HiltWorker
class PopulateWorkoutTableWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val database: GymificDatabase
) :
    CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.resources.openRawResource(R.raw.workouts).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val type = object : TypeToken<List<Workout>>() {}.type
                    val workoutList: List<Workout> = Gson().fromJson(jsonReader, type)
                    database.workoutDao.insertAll(workoutList)
                }
            }
            Timber.i("Populate workout-table request succeed")
            Result.success()
        } catch (e: Exception) {
            Timber.i("Populate workout-table request failed. ${e.message}")
            Result.failure()
        }
    }
}