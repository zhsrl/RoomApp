package com.e.roomapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CourseAdapter(
    val courseList: List<Course>? = null,
    val context: Context,
    var itemClick: ((View) -> Unit)? = null
): RecyclerView.Adapter<CourseAdapter.ViewHolder>() , CoroutineScope{

    private var courseDatabase: CourseDatabase = DatabaseProvider.getCourseDatabase(context)
    private var courseDao: CourseDao

    private var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        courseDao = courseDatabase.courseDao()
        job = Job()
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById(R.id.TV_course_name)
        var description: TextView = view.findViewById(R.id.TV_course_description)
        var price: TextView = view.findViewById(R.id.TV_course_price)



        fun bind(course: Course){
            name.text = course.courseName
            description.text = course.courseDescription
            price.text = course.coursePrice.toString()

            itemView.setOnLongClickListener {
                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show()
                launch {
                    removeCourse(adapterPosition)
                }

                false
            }

//            itemView.setOnClickListener{
//                val intent = Intent(context, CourseDetailActivity::class.java)
//                intent.putExtra(CourseDetailActivity.COURSE_TITLE, course.courseName)
//                intent.putExtra(CourseDetailActivity.COURSE_DESCRIPTION, course.courseDescription)
//                intent.putExtra(CourseDetailActivity.COURSE_PRICE, course.coursePrice)
//                intent.putExtra(CourseDetailActivity.COURSE_POSITION, adapterPosition.toString())
//
//
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.course_model, null, false)

        itemClick.let {
            view.setOnClickListener(it)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(courseList!![position])
    }

    override fun getItemCount(): Int {
        return courseList!!.size
    }

    suspend fun removeCourse(position: Int){
        val course = courseList?.get(position)
        (courseList as MutableList).remove(course)
        notifyDataSetChanged()
        courseDao.deleteCourse(course!!)
    }


}