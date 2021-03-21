package com.e.roomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    companion object{
        val REQUEST_CODE = 1
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var addCourseButton: FloatingActionButton

    private var courseAdapter: CourseAdapter? = null
    private var courseList: List<Course> = ArrayList()

    private lateinit var courseDatabase: CourseDatabase
    private lateinit var courseDao: CourseDao

    private lateinit var contextView: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        contextView = findViewById(R.id.parent_layout)

        courseDatabase = DatabaseProvider.getCourseDatabase(applicationContext)
        courseDao = courseDatabase.courseDao()

        // Display courses
        launch {
            courseList = courseDao.getAllCourse()
            if(courseList != null){
                Snackbar.make(contextView, "Course list updated", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(contextView, "Course list is null", Snackbar.LENGTH_SHORT).show()
            }
            recycleViewInit(courseList)

            courseAdapter!!.itemClick = {
                    view ->

                val coursePosition = recyclerView.getChildAdapterPosition(view)
                val course = courseList[coursePosition]

                val intent = Intent(applicationContext, CourseDetailActivity::class.java)
                intent.putExtra(CourseDetailActivity.COURSE_TITLE, course.courseName)
                intent.putExtra(CourseDetailActivity.COURSE_DESCRIPTION, course.courseDescription)
                intent.putExtra(CourseDetailActivity.COURSE_PRICE, course.coursePrice)
                intent.putExtra(CourseDetailActivity.COURSE_POSITION, coursePosition.toString())

                startActivityForResult(intent, REQUEST_CODE)


            }

        }

        // Add course
        addCourseButton = findViewById(R.id.FAB_add_course)
        addCourseButton.setOnClickListener {
            val intent = Intent(applicationContext, AddCourseActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun recycleViewInit(list: List<Course>?){
        recyclerView = findViewById(R.id.recyclerView)
        courseAdapter = CourseAdapter(list, applicationContext)
        recyclerView.adapter = courseAdapter
        courseAdapter!!.notifyDataSetChanged()

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE){
                courseAdapter!!.notifyDataSetChanged()
            }
        }
    }



}