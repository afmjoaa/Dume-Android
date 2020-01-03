package io.dume.dume.util

import android.content.Context
import android.content.SharedPreferences

class StateManager private constructor() {

    fun setValue(key: String, value: Any) {

        if (value is String) {
            edit!!.putString(key, value)
        } else if (value is Int) {
            edit!!.putInt(key, value)
        } else if (value is Boolean) {
            edit!!.putBoolean(key, value)
        } else if (value is Float) {
            edit!!.putFloat(key, value)
        } else if (value is Long) {
            edit!!.putLong(key, value)
        }
        edit!!.apply()
    }

    //get replacement
    fun sharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

    //current state in the forward flow
    fun setTeacherCurrentState(state: String) {
        edit!!.putString(TEACHER_POSITION, state)
        edit!!.apply()
    }

    fun setStudentCurrentState(state: String) {
        edit!!.putString(STUDENT_POSITION, state)
        edit!!.apply()
    }

    fun setRole(role: String) {
        edit!!.putString(ROLE, role)
        edit!!.apply()
    }


    fun setFirstTimeUser(state: Boolean?) {
        edit!!.putBoolean(FIRST_TIME_USER, state!!)
        edit!!.apply()
    }


    companion object {

        lateinit var sharedPreferences: SharedPreferences
        private var edit: SharedPreferences.Editor? = null
        public val FIRST_TIME_USER = "firstTimeUser"
        public val TEACHER_POSITION = "teacherCurrentState"
        public val STUDENT_POSITION = "studentCurrentState"
        public val ROLE = "role"

        fun getInstance(context: Context): StateManager {
            sharedPreferences = context.getSharedPreferences("state", Context.MODE_PRIVATE)
            edit = sharedPreferences!!.edit()
            return StateManager()
        }
    }
}
