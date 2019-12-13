package io.dume.dume.firstTimeUser.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import id.zelory.compressor.Compressor
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.util.FileUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.File
import java.util.*

class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private var avatarString: String? = null
    private var outputFileUri: Uri? = null
    private val LOCATION_REQUEST_CODE = 2222
    private val IMAGE_RESULT_CODE = 3333
    private var userLocation: GeoPoint? = null
    var action: String? = "null"
    private var selectedImageUri: Uri? = null
    private var compressedImage: File? = null
    private var actualImage: File? = null
    private var genderCheckedItem = 0
    private var PResultCheckedItem = 0
    private lateinit var parent: ForwardFlowHostActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        parent = activity as ForwardFlowHostActivity
        navController = Navigation.findNavController(view)
        init()
        initObservers()
    }

    private fun init() {
        register_dp.setOnClickListener { view ->
            if (avatarString == null || avatarString == "") {
                openImageIntent()
            } else {
                val popup = PopupMenu(context!!, view)
                popup.inflate(R.menu.menu_dp_long_click)
                popup.setOnMenuItemClickListener { menuItem ->
                    val id = menuItem.itemId
                    when (id) {
                        R.id.action_update -> openImageIntent()
                        R.id.action_remove -> {
                            avatarString = null
                            compressedImage = null
                            setAvatar(null)
                            flush("Display pic Removed")
                        }
                    }
                    false
                }
                popup.show()
            }
        }

    }

    fun setAvatar(uri: String?) {
        avatarString = uri
        Glide.with(getApplicationContext()).load(uri).apply(RequestOptions().override(100, 100).placeholder(R.drawable.avatar)).into(register_dp)

    }

    private fun openImageIntent() {
        // Determine Uri of camera image to save.
        val root = File(Environment.getExternalStorageDirectory().toString() + File.separator + "MyDir" + File.separator)
        root.mkdirs()
        val fname = "stu_" + viewModel.getUserUID() + ".jpg"
        val sdImageMainDirectory = File(root, fname)
        outputFileUri = Uri.fromFile(sdImageMainDirectory)

        // Camera.
        val cameraIntents = ArrayList<Intent>()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = context!!.getPackageManager()
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component = ComponentName(packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            cameraIntents.add(intent)
        }

        // Filesystem.
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        // Chooser of filesystem options.
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Source")

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray<Parcelable>())
        startActivityForResult(chooserIntent, IMAGE_RESULT_CODE)
    }


    private fun flush(msg: String?) {
        msg?.let {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }

    private fun initObservers() {
        viewModel.scan.observe(this, Observer {
            it?.let {
                flush("NID Data Recived")
                register_name.setText(it.name)
                register_nid.setText(it.nid.toString())
                register_birth_date.setText(it.birth_date)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT_CODE) {
                val isCamera: Boolean
                if (data == null) {
                    isCamera = true
                } else {
                    val action = data.action
                    if (action == null) {
                        isCamera = false
                    } else {
                        isCamera = action == MediaStore.ACTION_IMAGE_CAPTURE
                    }
                }

                if (isCamera) {
                    selectedImageUri = outputFileUri
                } else {
                    selectedImageUri = data?.data
                }
                if (selectedImageUri != null) {
                    parent.showProgress()
                    try {
                        actualImage = FileUtil.from(context, selectedImageUri)
                    } catch (e: Exception) {
                        actualImage = null
                        e.printStackTrace()
                    }

                    Glide.with(activity!!).load(selectedImageUri).apply(RequestOptions().override(100, 100).placeholder(R.drawable.set_display_pic)).into(register_dp)
                    if (actualImage == null) {
                        compressedImage = null
                        parent.hideProgress()
                    } else {
                        compressImage(actualImage!!)
                        parent.hideProgress()
                    }
                }
                //Glide.with(this).load(selectedImageUri).apply(new RequestOptions().override(100, 100)).into(profileUserDP);
            } else if (requestCode == LOCATION_REQUEST_CODE) {
                val selectedLocation = data?.getParcelableExtra<LatLng>("selected_location")
                if (selectedLocation != null) {
                    val retrivedLocation = GeoPoint(selectedLocation.latitude, selectedLocation.longitude)

                }
            }
        }

    }

    @SuppressLint("CheckResult")
    private fun compressImage(actualImage: File) {
        Compressor(context)
                .compressToFileAsFlowable(actualImage, "student_photo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    //flush("i am here");
                    compressedImage = file

                }, { throwable ->
                    throwable.printStackTrace()
                    flush(throwable.message)
                    parent.hideProgress()
                })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.registerBtn -> {
                navController.navigate(R.id.action_registerFragment_to_qualificationFragment)
            }
        }
    }
}
