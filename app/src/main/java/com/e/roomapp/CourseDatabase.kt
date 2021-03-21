package com.e.roomapp

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = arrayOf(Course::class), version = 1, exportSchema = false)
abstract class CourseDatabase: RoomDatabase() {

    abstract fun courseDao(): CourseDao
}

object DatabaseProvider{
    val databaseName: String = "course"

    @Volatile
    private var courseDatabase: CourseDatabase? = null

    fun getCourseDatabase(context: Context): CourseDatabase{
        synchronized(this){
            var instance = courseDatabase

            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                    CourseDatabase::class.java,
                    databaseName)
                    .fallbackToDestructiveMigration()
                    .build()


                courseDatabase = instance
            }

            return instance
        }

    }
}