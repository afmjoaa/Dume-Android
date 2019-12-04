package io.dume.dume.firstTimeUser

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.configAppToolBarTitle
import io.dume.dume.util.DumeUtils.configureAppbar
import kotlinx.android.synthetic.main.activity_forward_flow_host.*

class ForwardFlowHostActivity : BaseAppCompatActivity() {
    private lateinit var viewModel: ForwardFlowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forward_flow_host)
        init()

        viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        viewModel.title.observe(this, Observer {
            configAppToolBarTitle(this, it)
        })

    }

    private fun init() {
        setActivityContext(this, 1113)
        setDarkStatusBarIcon()
        configureAppbar(this, "Testing", true)
        initView()
        initListener()
    }

    private fun initView() {
        /*settingsAppbar.visibility = View.GONE
        supportActionBar?.hide()*/
    }

    private fun initListener() {

    }

    private fun hideActionBar(){
        settingsAppbar.visibility = View.GONE
        supportActionBar?.hide()
    }

    private fun showActionBar(){
        settingsAppbar.visibility = View.VISIBLE
        supportActionBar?.show()
    }
}
