package com.ersiver.gymific.ui.cardio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ersiver.gymific.model.Workout
import com.ersiver.gymific.repository.WorkoutRepository
import com.ersiver.gymific.util.CARDIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CardioViewModel @Inject constructor(repository: WorkoutRepository) : ViewModel() {

    init {
        Timber.i("CardioViewModel init")
    }

    private var cardioUiModelFlow: Flow<CardioUiModel> = repository.getWorkouts().map { list ->
        val cardio = list.filter { workout ->
            workout.category.contains(CARDIO, true)
        }
        CardioUiModel(cardio)
    }

    val cardioUiModel: LiveData<CardioUiModel> = cardioUiModelFlow.asLiveData()

    /**
     * Wraps the list of workouts that needs to be displayed in the UI.
     */
    data class CardioUiModel(val workouts: List<Workout>)
}
