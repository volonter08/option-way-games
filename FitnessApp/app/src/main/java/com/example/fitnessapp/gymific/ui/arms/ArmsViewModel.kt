package com.example.fitnessapp.gymific.ui.arms

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.fitnessapp.gymific.model.Workout
import com.example.fitnessapp.gymific.repository.WorkoutRepository
import com.example.fitnessapp.gymific.util.ARMS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class ArmsViewModel @ViewModelInject constructor(repository: WorkoutRepository) : ViewModel() {

    init {
        Timber.i("Init ArmsViewModel")
    }

    private val armsUiModelFlow: Flow<ArmsUiModel> = repository.getWorkouts().map { workouts ->
        val arms = workouts.filter { workout ->
            workout.category.contains(ARMS, true)
        }
        ArmsUiModel(arms)
    }

    val armsUiModel: LiveData<ArmsUiModel> = armsUiModelFlow.asLiveData()

    data class ArmsUiModel(val armsWorkouts: List<Workout>)
}