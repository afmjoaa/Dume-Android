package io.dume.dume

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.firestore.FirebaseFirestore
import io.dume.dume.poko.Skill
import io.dume.dume.poko.Teacher
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        var text = ""
        FirebaseFirestore.getInstance().collection("/users/mentors/mentor_profile").limit(10).get().apply {
            addOnSuccessListener { qrySnapshot ->
                val teachers = qrySnapshot.toObjects(Teacher::class.java)

                teachers.forEachIndexed { index, teacher ->
                    teacher.custom_query_array.forEach { key ->
                        var retrivedSkill = qrySnapshot.documents[index].get(key) as Map<*, *>
                        val mapper = ObjectMapper()
                        try {
                            headsUp(retrivedSkill.get("common_query_str").toString())
                            val skill = mapper.convertValue(retrivedSkill, Skill::class.java)
                            teachers[index].skills.add(skill)
                        } catch (err: Exception) {
                            Log.e("debug", err.toString())
                        }


                    }
                    text = "$text \n ${teacher.first_name}"
                }
                test_btn.setOnClickListener {
                    teachers.forEach {
                        headsUp("Total skill " + it.skills.size)
                    }
                }
                textView.text = text
            }
        }


    }


    fun headsUp(msg: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.dume_toast, null)
        val text: TextView = layout.findViewById(R.id.text) as TextView
        text.setText(msg)
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}
