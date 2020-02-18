package io.dume.dume.firstTimeUser.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import io.dume.dume.R
import io.dume.dume.firstTimeUser.*
import io.dume.dume.poko.MiniUser
import io.dume.dume.student.DashBoard.Fragment.JobBoard.StudentJobBoardFragment
import io.dume.dume.student.grabingLocation.GrabingLocationActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.getAddress
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), View.OnClickListener, IPickResult {


    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private val LOCATION_REQUEST_CODE = 2222
    private var userLocation: GeoPoint? = null
    var action: String? = "null"
    private lateinit var parent: ForwardFlowHostActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run { viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java) }
                ?: throw Throwable("invalid activity")
        parent = activity as ForwardFlowHostActivity
        navController = Navigation.findNavController(view)
        initalize()
        initObservers()
    }

    private fun initalize() {
        register_dp.setOnClickListener(this)
        register_location.setOnClickListener(this)

    }


    fun setAvatar(uri: Uri) {
        viewModel.avatar.postValue(uri)
        context?.let {
            Glide.with(it).load(uri).apply(RequestOptions().override(100, 100).placeholder(R.drawable.avatar)).into(register_dp)
        }
    }

    private fun validate(): MiniUser? {
        if (register_name.text.isNullOrEmpty()) {
            register_wrapper.error = "Name must not be empty"
            return null
        } else if (register_email.text.isNullOrEmpty()) {
            register_mail_wrapper.error = "Name must not be empty"
            return null
        } else if (register_location.text.isNullOrEmpty() || userLocation == null) {
            register_location_container.error = "Location must be chosen"
            return null
        }
        var lastName = ""
        for ((i, parts) in register_name.text.toString().split(" ").withIndex()) if (i != 0) lastName += "$parts "

        return MiniUser(
                name = register_name.text.toString(),
                birth_date = if (register_birth_date.text.isNullOrEmpty()) "N/A" else register_birth_date.text.toString(),
                mail = register_email.text.toString(),
                nid = if (register_nid.text.isNullOrEmpty()) null else register_nid.text.toString().toLong(),
                parmanent_location = userLocation!!,
                avatar = null,
                accoount_major = viewModel.role.value!!.flow,
                phone_number = viewModel.phoneNumber.value!!,
                first_name = register_name.text.toString().split(" ")[0],
                last_name = lastName,
                obligated_user = null,
                obligation = false,
                imei = DumeUtils.getImei(context),
                isEducated = false
        )
    }

    private fun flush(msg: String?) {
        msg?.let {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }

    private fun initObservers() {
        viewModel.scan.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                register_name.setText(it.name)
                register_nid.setText(it.nid.toString())
                register_birth_date.setText(it.birth_date.replace("Date of Birth", ""))
            }
        })
        viewModel.success.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                parent.hideProgress()
                if (viewModel.role.value == Role.STUDENT) {
                    viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.POSTJOB)
                    val args = Bundle()
                    args.putString(StudentJobBoardFragment.DESTINATION, "forward")
                    navController.navigate(R.id.action_registerFragment_to_postJobFragment, args)

                } else {
                    viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.QUALIFICATION)
                    navController.navigate(R.id.action_registerFragment_to_qualificationFragment)
                }
            }
        })
        viewModel.failure.observe(this, Observer { it?.let { flush(it.error); parent.hideProgress() } })



        parent.onRegisterButtonClick {
            validate()?.let {
                parent.showProgress()
                viewModel.register(it)
            }
        }
    }

    private fun setCurrentAddress(geoPoint: GeoPoint) {
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
            R.id.register_location -> {
                startActivityForResult(Intent(context, GrabingLocationActivity::
                class.java).setAction("fromPPA"), LOCATION_REQUEST_CODE)
            }
            R.id.register_dp -> {
                PickImageDialog.build(PickSetup()).setOnPickResult(this).show(fragmentManager)
            }
        }
    }

    /**
     * Dialog that promts to pick an profile image to save
     * */
    override fun onPickResult(r: PickResult) {
        if (r.error == null) {
            setAvatar(r.uri)
        } else {
            Log.e("debug", "error -> ${r.error}")
        }
    }

}
