package com.e.roomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CourseDetailActivity : AppCompatActivity(), CoroutineScope {

    companion object{
        val COURSE_TITLE = "title"
        val COURSE_DESCRIPTION = "desc"
        val COURSE_PRICE = "price"
        val COURSE_POSITION = "position"
    }

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var courseDatabase: CourseDatabase
    private lateinit var courseDao: CourseDao

    private lateinit var courseTitle: TextView
    private lateinit var courseDescription: TextView
    private lateinit var coursePrice: TextView

    private lateinit var courseRemove: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        job = Job()

        courseDatabase = DatabaseProvider.getCourseDatabase(applicationContext)
        courseDao = courseDatabase.courseDao()

        courseTitle = findViewById(R.id.TV_detail_course_title)
        courseDescription = findViewById(R.id.TV_detail_course_description)
        coursePrice = findViewById(R.id.TV_detail_course_price)

        val title = intent.getStringExtra(COURSE_TITLE)
        val description = intent.getStringExtra(COURSE_DESCRIPTION)
        val price = intent.getStringExtra(COURSE_PRICE)
        val position = intent.getStringExtra(COURSE_POSITION)

        Toast.makeText(applicationContext, position, Toast.LENGTH_LONG).show()

        courseTitle.text = title
        courseDescription.text = description
        coursePrice.text = price

        courseRemove = findViewById(R.id.FAB_delete_course)
        courseRemove.setOnClickListener{

                launch {
                    CourseAdapter(courseDao.getAllCourse(), applicationContext).removeCourse(position!!.toInt())
                    setResult(RESULT_OK)
                    finish()
                }
        }

    }
}