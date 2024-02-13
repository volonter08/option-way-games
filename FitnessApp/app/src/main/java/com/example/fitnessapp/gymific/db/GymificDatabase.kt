package com.example.fitnessapp.gymific.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnessapp.gymific.model.Workout
import com.example.fitnessapp.gymific.model.WorkoutCategory

@Database(entities = [Workout::class, WorkoutCategory::class], version = 1)
abstract class GymificDatabase : RoomDatabase() {
    abstract val workoutDao: WorkoutDao
    abstract val categoryDao: WorkoutCategoryDao
}