package com.example.fitnessapp.gymific.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.fitnessapp.gymific.model.Workout
import com.example.fitnessapp.gymific.model.WorkoutCategory
import com.example.fitnessapp.gymific.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class HomeViewModel @ViewModelInject constructor(
    repository: WorkoutRepository
) : ViewModel() {
    init {
        Timber.i("HomeViewModel init")
    }

    private val recommendedUiFlow: Flow<RecommendedUiModel> =
        repository.getWorkouts().map { workouts ->
            val recommended = workouts.filter {
                it.isRecommended
            }
            RecommendedUiModel(recommended)
        }


    val recommendedUiModel: LiveData<RecommendedUiModel> = recommendedUiFlow.asLiveData()

    private val categoriesFlow: Flow<CategoryUiModel> =
        repository.getCategories().map { categories ->
            CategoryUiModel(categories)
        }

    val categoriesUiModel: LiveData<CategoryUiModel> = categoriesFlow.asLiveData()

    data class RecommendedUiModel(
        val workouts: List<Workout>
    )

    data class CategoryUiModel(
        val categories: List<WorkoutCategory>
    )
}