package com.ersiver.gymific.ui.legs

import androidx.lifecycle.*
import com.ersiver.gymific.model.Workout
import com.ersiver.gymific.repository.WorkoutRepository
import com.ersiver.gymific.util.LEGS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
@HiltViewModel
class LegsViewModel @Inject constructor(repository: WorkoutRepository) :
    ViewModel() {

    init {
        Timber.i("LegsViewModel init")
    }

    private val legsUiModelFlow: Flow<UiModel> = repository.getWorkouts().map { list ->
        val legs = list.filter { workout ->
            workout.category.contains(LEGS, true)
        }
        UiModel(legs)
    }

    val legsUiModel: LiveData<UiModel> = legsUiModelFlow.asLiveData()


    /**
     * Wraps the list of workouts that needs to be displayed in the UI.
     */
    data class UiModel(val workouts: List<Workout>)
}