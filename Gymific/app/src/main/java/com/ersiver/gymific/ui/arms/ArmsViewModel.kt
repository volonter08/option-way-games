package com.ersiver.gymific.ui.arms

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ersiver.gymific.model.Workout
import com.ersiver.gymific.repository.WorkoutRepository
import com.ersiver.gymific.util.ARMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArmsViewModel @Inject constructor(repository: WorkoutRepository) : ViewModel() {

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