package io.dume.dume.student.pojo


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.jaeger.library.StatusBarUtil
import io.dume.dume.R
import io.dume.dume.broadcastReceiver.MyConnectivityHandler
import io.dume.dume.broadcastReceiver.NetworkChangeReceiver
import io.dume.dume.customView.HorizontalLoadView
import io.dume.dume.util.MyApplication
import io.dume.dume.util.NetworkUtil
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

open class BaseAppCompatActivity() : AppCompatActivity(), MyConnectivityHandler {
    protected open lateinit var context: Context
    protected lateinit var activity: Activity
    protected lateinit var searchDataStore: SearchDataStore
    private lateinit var decor: View
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var baseCoordinator: CoordinatorLayout
    private lateinit var dialog: Dialog
    private var loadView: HorizontalLoadView? = null
    @JvmField
    protected var snackBar: Snackbar? = null

    protected lateinit var rootView: View
    @JvmField
    protected var mDensity = 0f



    fun setActivityContext(context: Context, i: Int) {
        this.context = context
        fromFlag = i
        activity = context as Activity
        rootView = context.window.decorView.findViewById(android.R.id.content)
        createNetworkCheckSnackbar()
        searchDataStore = SearchDataStore.getInstance()
        mDensity = resources.displayMetrics.density
        init()
        findLoadView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onStart() {
        super.onStart()
        val status = NetworkUtil.getConnectivityStatusString(context)
        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            if (snackBar != null) {
                snackBar!!.show()
            }
        } else {
            if (snackBar != null) {
                snackBar!!.dismiss()
            }
            Thread(Runnable {
                try {
                    val socket: Socket = Socket()
                    socket.connect(InetSocketAddress("8.8.8.8", 53), 2500)
                    if (snackBar != null) {
                        snackBar!!.dismiss()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    if (snackBar != null) {
                        snackBar!!.show()
                    }
                }
            }).start()
        }
    }

    override fun onPause() {
        super.onPause()
        MyApplication.activityPaused() // On Pause notify the Application
    }

    override fun onResume() {
        super.onResume()
        MyApplication.activityResumed() // On Resume notify the Application
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create an IntentFilter instance.
        val intentFilter = IntentFilter()
        // Add network connectivity change action.
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        // Set broadcast receiver priority.
        intentFilter.priority = 100
        // Create a network change broadcast receiver.
        networkChangeReceiver = NetworkChangeReceiver()
        //setting the listener for the network
        networkChangeReceiver.setNetworkListener(this)
        // MiniUser the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun pause() {
        if (snackBar != null) {
            snackBar!!.show()
        }
    }

    override fun resume() {
        if (snackBar != null) {
            snackBar!!.dismiss()
        }
    }

    override fun onError(e: Exception) {
        Log.e(TAG, "onNetworkError: Error function called$e")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (fromFlag == 1112) {
                val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
                drawerLayout.openDrawer(GravityCompat.START, true)
                return true
            } else {
                super.onBackPressed()
            }
        } else if (id == R.id.support) {
            dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (fromFlag == 1112) {
            menuInflater.inflate(R.menu.support_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun createNetworkCheckSnackbar() {
        baseCoordinator = rootView.findViewById(R.id.parent_coor_layout)
        snackBar = Snackbar.make(baseCoordinator, "Replace with your own action", Snackbar.LENGTH_INDEFINITE)
        val layout = snackBar!!.view as Snackbar.SnackbarLayout
        val textView = layout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.visibility = View.INVISIBLE
        val inflater = LayoutInflater.from(context)
        val snackView = inflater.inflate(io.dume.dume.R.layout.custom_snackbar_layout, null)
        val textViewStart = snackView.findViewById<TextView>(R.id.custom_snackbar_text)
        textViewStart.setText(io.dume.dume.R.string.noNet_snack_text)
        textViewStart.setTextColor(Color.WHITE)
        layout.setPadding(0, 0, 0, 0)
        layout.setBackgroundColor(ContextCompat.getColor((context), R.color.snackbar_red))
        val parentParams = layout.layoutParams as CoordinatorLayout.LayoutParams
        parentParams.height = (36 * (resources.displayMetrics.density)).toInt()
        //can set those things like that ..
        //parentParams.gravity = Gravity.TOP;
        layout.layoutParams = parentParams
        layout.addView(snackView, 0)
        val retryBtn = snackView.findViewById<LinearLayout>(io.dume.dume.R.id.snackbar_btn)
        retryBtn.setOnClickListener(View.OnClickListener { v ->
            val imageView = v.findViewById<ImageView>(io.dume.dume.R.id.custom_snackbar_btnImage)
            if (imageView.visibility == View.INVISIBLE) {
                imageView.visibility = View.VISIBLE
            }
            val d = imageView.drawable
            if (d is Animatable) {
                (d as Animatable).start()
            }
            val status = NetworkUtil.getConnectivityStatusString(context)
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                if (snackBar != null) {
                    snackBar!!.show()
                }
            } else {
                if (snackBar != null) {
                    snackBar!!.dismiss()
                }
                Thread(Runnable {
                    try {
                        val socket: Socket = Socket()
                        socket.connect(InetSocketAddress("8.8.8.8", 53), 2500)
                        // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
                        if (snackBar != null) {
                            snackBar!!.dismiss()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        if (snackBar != null) {
                            snackBar!!.show()
                        }
                    }
                }).start()
            }
        })
    }

    fun settingStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity.window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucent(activity, 50)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
        //lollipop only support white status bar icon color so have to fix it by the status bar util library
        //by making translucent status bar and test thoroughly
        //StatusBarUtil.setTranslucent(this, 100);
        // StatusBarUtil.setTransparent(this);
        //StatusBarUtil.setColor(Activity activity, int color)
    }

    //make transparent status bar icon color dark
    fun setDarkStatusBarIcon() {
        decor = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            StatusBarUtil.setDarkMode(activity)
            StatusBarUtil.setTranslucent(activity, 50)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    //make transparent status bar icon color light
    fun setLightStatusBarIcon() {
        decor = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.systemUiVisibility = 0
        } else {
            StatusBarUtil.setLightMode(activity)
            StatusBarUtil.setTranslucent(activity, 50)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    fun makeFullScreen() {
        val decorView = activity.window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    fun onSignOut() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().signOut()
            // Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // startActivity(intent);
            // finish();
        }
    }

    fun findLoadView() {
        try {
            loadView = rootView!!.findViewById(R.id.loadView)
        } catch (e: Exception) {
            Log.d(TAG, "findLoadView: " + e.message)
        }
    }

    open fun showProgress() {
        if (loadView!!.visibility == View.INVISIBLE || loadView!!.visibility == View.GONE) {
            loadView!!.visibility = View.VISIBLE
        }
        if (!loadView!!.isRunningAnimation) {
            loadView!!.startLoading()
        }
    }

    open fun hideProgress() {
        if (loadView!!.isRunningAnimation) {
            loadView!!.stopLoading()
        }
        if (loadView!!.visibility == View.VISIBLE) {
            loadView!!.visibility = View.INVISIBLE
        }
    }

    open fun flush(msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        val v: TextView? = toast.view.findViewById<View>(R.id.message) as TextView
        if (v != null) v.gravity = Gravity.CENTER
        toast.show()
    }

    fun initSupportDialog() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
        val customLayout = layoutInflater.inflate(R.layout.support_dialog_layout, null, false)
        materialAlertDialogBuilder.setView(customLayout)
        dialog = materialAlertDialogBuilder.create()
        try {
            val adminChat = customLayout.findViewById<Button>(R.id.adminChatBtn)
            val supportCall = customLayout.findViewById<Button>(R.id.hotlineBtn)
            adminChat.setOnClickListener{
                dialog.dismiss()
            }
            supportCall.setOnClickListener{
                dialog.dismiss()
                val u = Uri.parse("tel:" + "01303464617")
                val i = Intent(Intent.ACTION_DIAL, u)
                context.startActivity(i)
            }
        } catch (npe: NullPointerException) {
            npe.printStackTrace()
        }
    }

    //initializing activities based on there flag
    private fun init() {
        if (fromFlag == 1112) {
            initSupportDialog()
            settingStatusBarTransparent()
        }
        setDarkStatusBarIcon()
    }

    companion object {
        private val TAG = "CustomStuAppCompatActiv"
        @JvmField
        protected var fromFlag = 0
        private fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
            val win = activity!!.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}