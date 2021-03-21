package com.e.roomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "course")
data class Course(

    @ColumnInfo(name = "name")
    var courseName: String,

    @ColumnInfo(name = "description")
    var courseDescription: String,

    @ColumnInfo(name = "price")
    var coursePrice: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var courseId: Int = 0
)