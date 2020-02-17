package io.dume.dume

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.dume.dume.student.pojo.BaseAppCompatActivity

class TestActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

    }
}
