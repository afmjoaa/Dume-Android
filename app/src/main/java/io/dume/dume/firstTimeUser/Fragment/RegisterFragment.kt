package io.dume.dume.firstTimeUser.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.poko.MiniUser
import io.dume.dume.student.grabingLocation.GrabingLocationActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.getAddress
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.File

class RegisterFragment : Fragment(), View.OnClickListener, IPickResult {


    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private var avatarString: Uri? = null
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
        register_dp.setOnClickListener(this)
        register_location.setOnClickListener(this)

    }

    fun setAvatar(uri: Uri) {
        avatarString = uri
        Glide.with(getApplicationContext()).load(uri).apply(RequestOptions().override(100, 100).placeholder(R.drawable.avatar)).into(register_dp)
    }


    fun validate(): MiniUser? {
        if (register_name.text.toString() == "") {
            register_name.setError("Name must not be empty")
            return null
        } else if (register_location.text.toString() == "" || userLocation == null) {
            register_location_container.setError("Location must be chosen")
            return null
        }
        var last_name = ""
        for ((i, parts) in register_name.text.toString().split(" ").withIndex()) if (i != 0) last_name += "$parts "

        return MiniUser(name = register_name.text.toString(),
                birth_date = register_birth_date.text.toString(),
                mail = register_email.text.toString(),
                nid = register_nid.text.toString().toLong(),
                parmanent_location = userLocation!!,
                avatar = avatarString.toString(),
                accoount_major = viewModel.role.value!!.flow,
                phone_number = viewModel.phoneNumber.value!!,
                first_name = register_name.text.toString().split(" ")[0],
                last_name = last_name,
                obligated_user = null,
                obligation = false,
                imei = DumeUtils.getImei(context)
        )
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

        parent.onRegisterButtonClick {
            validate()?.let {
                parent.showProgress()
                viewModel.register(it)
                Handler().postDelayed({
                    parent.hideProgress()
                }, 2000)
            }
        }
    }

    fun setCurrentAddress(geoPoint: GeoPoint) {
        userLocation = geoPoint
        val address = getAddress(context, geoPoint.latitude, geoPoint.longitude)
        register_location.setText(address)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                val selectedLocation = data!!.getParcelableExtra<LatLng>("selected_location")
                if (selectedLocation != null) {
                    val retrivedLocation = GeoPoint(selectedLocation.latitude, selectedLocation.longitude)
                    setCurrentAddress(retrivedLocation)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.registerBtn -> {
                navController.navigate(R.id.action_registerFragment_to_qualificationFragment)
            }
            R.id.register_location -> {
                startActivityForResult(Intent(context, GrabingLocationActivity::
                class.java).setAction("fromPPA"), LOCATION_REQUEST_CODE)
            }
            R.id.register_dp -> {
                PickImageDialog.build(PickSetup()).setOnPickResult(this).show(fragmentManager)
                /*   if (avatarString == null || avatarString == "") {
                       openImageIntent()
                   } else {
                       val popup = PopupMenu(context!!, v)
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
                   }*/
            }
        }
    }

    /**
     * Dialog that promts to pick an profile image to save
     * */
    public override fun onPickResult(r: PickResult) {
        if (r.error == null) {
            setAvatar(r.uri)
        } else {
            Toast.makeText(context, r.error.message, Toast.LENGTH_LONG).show()
        }
    }

}
