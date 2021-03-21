package com.e.roomapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CourseDao {

    @Insert
    suspend fun addCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("SELECT * FROM course")
    suspend fun getAllCourse(): List<Course>

}