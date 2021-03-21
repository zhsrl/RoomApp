package com.e.roomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddCourseActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var courseTitle: EditText
    private lateinit var courseDescription: EditText
    private lateinit var coursePrice: EditText

    private lateinit var addCourseButton: MaterialButton

    private lateinit var courseDatabase: CourseDatabase
    private lateinit var courseDao: CourseDao

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        job = Job()

        courseDatabase = DatabaseProvider.getCourseDatabase(applicationContext)
        courseDao = courseDatabase.courseDao()

        courseTitle = findViewById(R.id.ET_ad_title)
        courseDescription = findViewById(R.id.ET_ad_description)
        coursePrice = findViewById(R.id.ET_ad_price)




        addCourseButton = findViewById(R.id.BTN_add_course)
        addCourseButton.setOnClickListener{

            launch {

                val titleTxt: String = courseTitle.text.toString().trim()
                val descriptionTxt: String = courseDescription.text.toString().trim()
                val priceTxt: String = coursePrice.text.toString().trim()

                addCourse(titleTxt, descriptionTxt, priceTxt + " KZT")
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    suspend fun addCourse(courseTitle: String, courseDescription: String, coursePrice: String){
        val course = Course(courseTitle, courseDescription, coursePrice)
        courseDao.addCourse(course)
    }
}