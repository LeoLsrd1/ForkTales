package com.ltu.m7019e.forktales.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ltu.m7019e.forktales.model.Recipe

/**
 * The Room database for storing Recipe entities.
 */
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    /**
     * Provides access to the data access object (DAO) for the database.
     *
     * @return An instance of RecipeDao.
     */
    abstract fun recipeDao(): RecipeDao

    companion object {
        /**
         * Volatile instance of the database to ensure visibility of changes across threads.
         */
        @Volatile
        private var Instance: RecipeDatabase? = null

        /**
         * Returns the singleton instance of the database, creating it if necessary.
         *
         * @param context The context to use for creating or opening the database.
         * @return The singleton instance of RecipeDatabase.
         */
        fun getDatabase(context: Context): RecipeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext,
                    RecipeDatabase::class.java, "recipe_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}