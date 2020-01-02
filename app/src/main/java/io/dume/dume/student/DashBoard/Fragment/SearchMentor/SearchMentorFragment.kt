package io.dume.dume.student.DashBoard.Fragment.SearchMentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.dume.dume.R
import io.dume.dume.student.grabingLocation.GrabingLocationActivity
import io.dume.dume.student.homePage.HomePageActivity
import io.dume.dume.student.homePage.HomePageContract
import io.dume.dume.student.homePage.HomePageModel
import io.dume.dume.student.homePage.HomePagePresenter
import io.dume.dume.student.homePage.adapter.RecentSearchAdapter
import io.dume.dume.student.homePage.adapter.RecentSearchData
import io.dume.dume.student.pojo.SearchDataStore
import io.dume.dume.student.profilePage.ProfilePageActivity
import io.dume.dume.student.searchLoading.SearchLoadingActivity
import io.dume.dume.teacher.crudskill.CrudSkillActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.getEndOFNest
import io.dume.dume.util.NetworkUtil
import kotlinx.android.synthetic.main.fragment_search_mentor.*
import kotlinx.android.synthetic.main.fragment_search_mentor.view.*
import java.util.*

class SearchMentorFragment : Fragment(), HomePageContract.View, View.OnClickListener {

    private val TAG = "HomePageActivity"
    private lateinit var documentSnapshot: DocumentSnapshot
    private lateinit var mySnackbar: Snackbar
    private var recentSearchAdapter: RecentSearchAdapter? = null
    private lateinit var recently_searched: Map<String, Map<String, Any>>
    private lateinit var filterBottomSheetDialog: BottomSheetDialog
    private lateinit var filterRootView: View
    private var enamSnackbar: Snackbar? = null
    private lateinit var selectedUnis: MutableList<String>
    private lateinit var checkedUnis: BooleanArray
    private lateinit var selectedDegrees: MutableList<String>
    private lateinit var checkedItems: BooleanArray
    private lateinit var prefs: SharedPreferences
    private lateinit var searchDataStore: SearchDataStore
    private lateinit var presenter: HomePagePresenter
    private lateinit var student_container: View
    private lateinit var recent_search_rv: RecyclerView
    private lateinit var model: HomePageModel

    fun inititalizeVariable(root: View) {
        model = HomePageModel(activity, context)
        presenter = HomePagePresenter(context, this, model)
        presenter.homePageEnqueue()
        enamSnackbar = Snackbar.make(root.student_container, "Replace with your own action", Snackbar.LENGTH_LONG)
        student_container = root.student_container
        this.recent_search_rv = root.recent_search_recycler
        student_search.setOnClickListener(this)
        student_search_filter.setOnClickListener(this)
        prefs = context!!.getSharedPreferences("filter", Context.MODE_PRIVATE)
        filterBottomSheetDialog = BottomSheetDialog(context!!)
        filterRootView = this.layoutInflater.inflate(R.layout.filter_bottom_sheet_dialogue, null)
        filterBottomSheetDialog.setContentView(filterRootView)
        searchDataStore = SearchDataStore.getInstance()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inititalizeVariable(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_search_mentor, container, false)
        return root
    }

    override fun onClick(p0: View?) {
        //presenter.onViewIntracted(p0)
    }

    protected fun isProfileOK(): Boolean {
        var percentage = 0

        if (documentSnapshot != null) {
            val beh = documentSnapshot.get("pro_com_%") as String
            percentage = Integer.parseInt(beh)
            if (percentage >= 90) {
                return true
            }

        }
        flush("Profile should be at least 90% completed")
        val snackString = "Profile only $percentage% complete"
        showPercentSnak(snackString, "GO TO PROFILE")
        return false

    }

    override fun findView() {

    }


    override fun init() {

    }


    override fun configHomePage() {

    }

    //Need to work here after salah
    override fun initRecentSearchRecycler(documentSnapshot: DocumentSnapshot) {
        val recentSearchData = ArrayList<RecentSearchData>()
        val preIdentifyOne = documentSnapshot.getString("next_rs_write")
        recently_searched = documentSnapshot.get("recent_search") as Map<String, Map<String, Any>>
        if (recently_searched != null && recently_searched.size > 0) {
            for ((key, value) in recently_searched) {
                val recentSearchDataCurrent = RecentSearchData()
                var primaryText = ""
                var secondaryText = ""
                var temp: String? = ""
                val queryListName = value["query_list_name"] as List<String>
                val queryList = value["query_list"] as List<String>
                recentSearchDataCurrent.categoryName = queryList[0]
                for (i in queryListName.indices) {
                    if (getEndOFNest().contains(queryListName[i])) {
                        primaryText = primaryText + queryListName[i]
                        break
                    }
                }
                val jizz = value["jizz"] as Map<String, Any>
                temp = jizz[primaryText] as String
                primaryText = "$primaryText: $temp / "
                temp = value["package_name"] as String
                if (temp != null) {
                    if (temp == SearchDataStore.REGULAR_DUME) {
                        temp = "Monthly Tutor"
                    } else if (temp == SearchDataStore.INSTANT_DUME) {
                        temp = "Weekly Tutor"
                    } else {
                        temp = "Coaching"
                    }
                }
                primaryText = primaryText + temp
                recentSearchDataCurrent.primaryText = primaryText
                temp = jizz["Gender"] as String
                secondaryText = secondaryText + temp
                temp = jizz["Salary"] as String
                secondaryText = "$secondaryText / $temp"
                recentSearchDataCurrent.secondaryText = secondaryText
                recentSearchDataCurrent.identify = key
                recentSearchData.add(recentSearchDataCurrent)
            }
        }
        recentSearchAdapter = object : RecentSearchAdapter(context, recentSearchData, preIdentifyOne) {
            override fun OnItemClicked(v: View, position: Int, identify: String) {
                val clickedSearchData: Map<String, Any> = recently_searched[identify]!!
                val anchorPoint = clickedSearchData["anchor_point"] as GeoPoint
                searchDataStore.setAnchorPoint(LatLng(anchorPoint.latitude, anchorPoint.longitude))
                searchDataStore.setPackageName(clickedSearchData["package_name"] as String)
                searchDataStore.setJizz(clickedSearchData["jizz"] as Map<String, Any>)
                searchDataStore.setQueryList(clickedSearchData["query_list"] as List<String>)
                searchDataStore.setQueryListName(clickedSearchData["query_list_name"] as List<String>)
                searchDataStore.setForWhom(clickedSearchData["for_whom"] as Map<String, Any>)
                searchDataStore.setPreferredDays(clickedSearchData["preferred_days"] as Map<String, Any>)
                searchDataStore.setStartDate(clickedSearchData["start_date"] as Map<String, Any>)
                searchDataStore.setStartTime(clickedSearchData["start_time"] as Map<String, Any>)
                val intent = Intent(context, SearchLoadingActivity::class.java)
                intent.action = "from_HPA"
                startActivity(intent)
            }
        }
        recent_search_recycler.setAdapter(recentSearchAdapter)
        recent_search_recycler.setLayoutManager(LinearLayoutManager(context))
    }


    override fun onSwitchAccount() {

    }

     fun onCenterCurrentLocation() {


    }

    override fun gotoProfilePage() {
        startActivity(Intent(context, ProfilePageActivity::class.java))
    }

    override fun gotoGrabingLocationPage() {
        val chooseLocationRadio1 = prefs.getBoolean("chooseLocationRadio", true)
        if (chooseLocationRadio1) {
            startActivity(Intent(context, GrabingLocationActivity::class.java).setAction("HomePage"))
        } else {
            val current_address = documentSnapshot.getGeoPoint("current_address")
            if (current_address != null) {
                if (Objects.requireNonNull(current_address).latitude != 84.9 && current_address.longitude != 180.0) {
                    searchDataStore.setAnchorPoint(LatLng(current_address.latitude, current_address.longitude))
                }
                startActivity(Intent(context, CrudSkillActivity::class.java).setAction(DumeUtils.STUDENT))
            } else {
                startActivity(Intent(context, GrabingLocationActivity::class.java).setAction("HomePage"))
            }
        }
    }

    override fun flush(msg: String) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        val v = toast.getView().findViewById(android.R.id.message) as TextView
        v.gravity = Gravity.CENTER
        toast.show()
    }

    override fun showSnackBar(completePercent: String) {
        mySnackbar = Snackbar.make(student_container, "Replace with your own action", Snackbar.LENGTH_LONG)
        val layout = mySnackbar.getView() as Snackbar.SnackbarLayout
        val textView = layout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.visibility = View.INVISIBLE
        val inflater = LayoutInflater.from(context)
        val snackView = inflater.inflate(R.layout.custom_snackbar_layout_one, null)

        val textViewStart = snackView.findViewById<TextView>(R.id.custom_snackbar_text)
        textViewStart.text = "Profile only $completePercent% complete"


        layout.setPadding(0, 0, 0, 0)
        if (Integer.parseInt(completePercent) < 90) {
            layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.snackbar_yellow))
            textViewStart.setTextColor(Color.BLACK)
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.snackbar_green))
            textViewStart.setTextColor(Color.WHITE)
        }
        val parentParams = layout.layoutParams as CoordinatorLayout.LayoutParams
        parentParams.height = (36 * resources.displayMetrics.density).toInt()

        layout.layoutParams = parentParams
        layout.addView(snackView, 0)
        val status = NetworkUtil.getConnectivityStatusString(context)
        if (!mySnackbar.isShown() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            mySnackbar.show()
        }
    }


    override fun showPercentSnak(message: String, actionName: String) {
        val layout = enamSnackbar?.getView() as Snackbar.SnackbarLayout
        val textView = layout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.visibility = View.INVISIBLE
        val inflater = LayoutInflater.from(context)
        val snackView = inflater.inflate(R.layout.teachers_snakbar_layout, null)
        // layout.setBackgroundColor(R.color.red);
        val textViewStart = snackView.findViewById<TextView>(R.id.custom_snackbar_text)
        textViewStart.text = message
        val actionTV = snackView.findViewById<TextView>(R.id.actionTV)
        actionTV.setTextColor(resources.getColor(R.color.snack_action))
        actionTV.setOnClickListener { view -> startActivity(Intent(context, ProfilePageActivity::class.java)) }
        actionTV.text = actionName
        layout.setPadding(0, 0, 0, 0)
        val parentParams = layout.layoutParams as CoordinatorLayout.LayoutParams
        parentParams.height = (36 * resources.displayMetrics.density).toInt()
        layout.layoutParams = parentParams
        layout.addView(snackView, 0)
        val status = NetworkUtil.getConnectivityStatusString(context)
        enamSnackbar?.show()

    }

    override fun searchFilterClicked() {

        val mainText = filterBottomSheetDialog.findViewById<TextView>(R.id.main_text)
        val subText = filterBottomSheetDialog.findViewById<TextView>(R.id.sub_text)
        val proceedBtn = filterBottomSheetDialog.findViewById<Button>(R.id.cancel_yes_btn)
        val universityNames = filterBottomSheetDialog.findViewById<EditText>(R.id.input_uni)
        val degreeNames = filterBottomSheetDialog.findViewById<EditText>(R.id.input_degree)
        val uniCheckBox = filterBottomSheetDialog.findViewById<CheckBox>(R.id.uni_checkbox)
        val degreeCheckBox = filterBottomSheetDialog.findViewById<CheckBox>(R.id.degree_checkbox)
        val permanentRadio = filterBottomSheetDialog.findViewById<RadioButton>(R.id.permanent_radio)
        val chooseLocationRadio = filterBottomSheetDialog.findViewById<RadioButton>(R.id.choose_radio)
        val permanentLocationRadio = filterBottomSheetDialog.findViewById<RadioButton>(R.id.permanent_radio)

        //initialize this part form the shared Preference
        val uniItems = resources.getStringArray(R.array.University)
        val listItems = resources.getStringArray(R.array.Degrees)

        val editor = context?.getSharedPreferences("filter", Context.MODE_PRIVATE)!!.edit()

        val uniCheckBox1 = prefs.getBoolean("uniCheckBox", false)
        uniCheckBox!!.isChecked = uniCheckBox1

        val degreeCheckBox1 = prefs.getBoolean("degreeCheckBox", false)
        degreeCheckBox!!.isChecked = degreeCheckBox1

        val gson = Gson()
        val json = prefs.getString("selectedUnis", "")
        if (json != "") {
            val type = object : TypeToken<List<String>>() {

            }.type
            selectedUnis = gson.fromJson<MutableList<String>>(json, type)
            //setting the textview
            val item = StringBuilder()
            for (i in selectedUnis.indices) {
                item.append(selectedUnis.get(i))
                if (i != selectedUnis.size - 1) {
                    item.append(", ")
                }
            }
            universityNames!!.setText(item)
        } else {
            selectedUnis = ArrayList()
        }

        //retrieving the array
        val size = prefs.getInt("checkedUnis" + "_size", 0)
        checkedUnis = BooleanArray(uniItems.size)
        for (i in 0 until size)
            checkedUnis[i] = prefs.getBoolean("checkedUnis_$i", false)

        val json1 = prefs.getString("selectedDegrees", "")
        if (json1 != "") {
            val type1 = object : TypeToken<List<String>>() {

            }.type
            selectedDegrees = gson.fromJson<MutableList<String>>(json1, type1)
            val item = StringBuilder()
            for (i in selectedDegrees.indices) {
                item.append(selectedDegrees.get(i))
                if (i != selectedDegrees.size - 1) {
                    item.append(", ")
                }
            }
            degreeNames!!.setText(item)
        } else {
            selectedDegrees = ArrayList()
        }

        //retrieving the array
        val size1 = prefs.getInt("checkedItems" + "_size", 0)
        checkedItems = BooleanArray(listItems.size)
        for (i in 0 until size1)
            checkedItems[i] = prefs.getBoolean("checkedItems_$i", false)

        val chooseLocationRadio1 = prefs.getBoolean("chooseLocationRadio", true)
        if (chooseLocationRadio1) {
            chooseLocationRadio!!.isChecked = true
            permanentRadio!!.isChecked = false
        } else {
            chooseLocationRadio!!.isChecked = false
            permanentRadio!!.isChecked = true
        }

        if (mainText != null && subText != null && proceedBtn != null &&
                universityNames != null && degreeNames != null) {

            universityNames.setOnClickListener(object : View.OnClickListener {

                private var mUniDialog: AlertDialog? = null

                override fun onClick(view: View) {
                    val mBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    mBuilder.setTitle("Select filter universities")
                    mBuilder.setMultiChoiceItems(uniItems, checkedUnis) { dialogInterface, position, isChecked ->
                        if (isChecked) {
                            if (selectedUnis.size == 3) {
                                Toast.makeText(context, "Maximum 3 filter allowed", Toast.LENGTH_SHORT).show()
                                (mUniDialog as AlertDialog).listView.setItemChecked(position, false)
                                checkedUnis[position] = false
                            } else {
                                if (!selectedUnis.contains(uniItems[position])) {
                                    selectedUnis.add(uniItems[position])
                                }
                                checkedUnis[position] = true
                            }
                        } else {
                            if (selectedUnis.contains(uniItems[position])) {
                                selectedUnis.remove(uniItems[position])
                            }
                            checkedUnis[position] = false
                        }
                    }

                    mBuilder.setCancelable(false)
                    mBuilder.setPositiveButton("Done") { dialogInterface, which ->
                        if (selectedUnis.size > 0) {
                            val item = StringBuilder()
                            for (i in selectedUnis.indices) {
                                item.append(selectedUnis.get(i))
                                if (i != selectedUnis.size - 1) {
                                    item.append(", ")
                                }
                            }
                            universityNames.setText(item)
                            uniCheckBox.isChecked = true
                        } else {
                            universityNames.setText("")
                            uniCheckBox.isChecked = false
                        }

                        editor.putBoolean("uniCheckBox", uniCheckBox.isChecked)

                        editor.putInt("checkedUnis" + "_size", checkedUnis.size)
                        for (i in checkedUnis.indices)
                            editor.putBoolean("checkedUnis_$i", checkedUnis[i])

                        val gson = Gson()
                        val json = gson.toJson(selectedUnis)
                        editor.putString("selectedUnis", json)
                        editor.apply()
                    }

                    mBuilder.setNeutralButton("Clear all") { dialogInterface, which ->
                        for (i in checkedUnis.indices) {
                            checkedUnis[i] = false
                            selectedUnis.clear()
                            universityNames.setText("")
                            uniCheckBox.isChecked = false
                        }
                        editor.putBoolean("uniCheckBox", uniCheckBox.isChecked)

                        editor.putInt("checkedUnis" + "_size", checkedUnis.size)
                        for (i in checkedUnis.indices)
                            editor.putBoolean("checkedUnis_$i", checkedUnis[i])

                        val gson = Gson()
                        val json = gson.toJson(selectedUnis)
                        editor.putString("selectedUnis", json)
                        editor.apply()
                    }

                    mUniDialog = mBuilder.create()
                    mUniDialog!!.show()
                }
            })

            uniCheckBox.setOnClickListener { v ->
                //is chkIos checked?
                editor.putBoolean("uniCheckBox", (v as CheckBox).isChecked)
                editor.apply()
                if (v.isChecked) {
                    universityNames.performClick()
                }
            }

            degreeNames.setOnClickListener(object : View.OnClickListener {

                private var mDegreeDialog: AlertDialog? = null

                override fun onClick(view: View) {

                    val mBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    mBuilder.setTitle("Select filter degrees")
                    mBuilder.setMultiChoiceItems(listItems, checkedItems) { dialogInterface, position, isChecked ->
                        if (isChecked) {
                            if (selectedDegrees.size == 3) {
                                Toast.makeText(context, "Maximum 3 filter allowed", Toast.LENGTH_SHORT).show()
                                (mDegreeDialog as AlertDialog).listView.setItemChecked(position, false)
                                checkedItems[position] = false
                            } else {
                                if (!selectedDegrees.contains(listItems[position])) {
                                    selectedDegrees.add(listItems[position])
                                }
                                checkedItems[position] = true
                            }
                        } else {
                            if (selectedDegrees.contains(listItems[position])) {
                                selectedDegrees.remove(listItems[position])
                            }
                            checkedItems[position] = false
                        }
                    }

                    mBuilder.setCancelable(false)
                    mBuilder.setPositiveButton("Done") { dialogInterface, which ->
                        if (selectedDegrees.size > 0) {
                            val item = StringBuilder()
                            for (i in selectedDegrees.indices) {
                                item.append(selectedDegrees.get(i))
                                if (i != selectedDegrees.size - 1) {
                                    item.append(", ")
                                }
                            }
                            degreeNames.setText(item)
                            degreeCheckBox.isChecked = true
                        } else {
                            degreeNames.setText("")
                            degreeCheckBox.isChecked = false
                        }

                        editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked)

                        editor.putInt("checkedItems" + "_size", checkedItems.size)
                        for (i in checkedItems.indices)
                            editor.putBoolean("checkedItems_$i", checkedItems[i])

                        val gson1 = Gson()
                        val json1 = gson1.toJson(selectedDegrees)
                        editor.putString("selectedDegrees", json1)
                        editor.apply()
                    }

                    mBuilder.setNeutralButton("Clear all") { dialogInterface, which ->
                        for (i in checkedItems.indices) {
                            checkedItems[i] = false
                            selectedDegrees.clear()
                            degreeNames.setText("")
                            degreeCheckBox.isChecked = false
                            //mItemSelected.setText("");
                        }
                        editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked)

                        editor.putInt("checkedItems" + "_size", checkedItems.size)
                        for (i in checkedItems.indices)
                            editor.putBoolean("checkedItems_$i", checkedItems[i])

                        val gson1 = Gson()
                        val json1 = gson1.toJson(selectedDegrees)
                        editor.putString("selectedDegrees", json1)
                        editor.apply()
                    }

                    mDegreeDialog = mBuilder.create()
                    mDegreeDialog!!.show()

                }
            })

            degreeCheckBox.setOnClickListener { view ->
                //is chkIos checked?
                editor.putBoolean("degreeCheckBox", (view as CheckBox).isChecked)
                editor.apply()
                if (view.isChecked) {
                    degreeNames.performClick()
                }
            }

            chooseLocationRadio!!.setOnClickListener {
                editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked)
                editor.apply()
            }

            permanentLocationRadio!!.setOnClickListener {
                editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked)
                editor.apply()
            }

            proceedBtn.setOnClickListener {
                editor.putBoolean("uniCheckBox", uniCheckBox.isChecked)
                editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked)

                editor.putInt("checkedUnis" + "_size", checkedUnis.size)
                for (i in checkedUnis.indices)
                    editor.putBoolean("checkedUnis_$i", checkedUnis[i])

                editor.putInt("checkedItems" + "_size", checkedItems.size)
                for (i in checkedItems.indices)
                    editor.putBoolean("checkedItems_$i", checkedItems[i])

                val gson = Gson()
                val json = gson.toJson(selectedUnis)
                editor.putString("selectedUnis", json)

                val gson1 = Gson()
                val json1 = gson1.toJson(selectedDegrees)
                editor.putString("selectedDegrees", json1)

                editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked)
                editor.apply()

                filterBottomSheetDialog.dismiss()
                gotoGrabingLocationPage()
            }
        }
        filterBottomSheetDialog.show()
    }



    override fun getAvatarString(): String {
        return ""
    }

    override fun getSelfRating(): Map<String, Any> {
        return documentSnapshot.get("self_rating") as Map<String, Any>
    }

    override fun generateMsgName(last: String?, first: String?): String {
        return ""
    }

    override fun getUserName(): String {
        return ""
    }

    override fun setUserName(last: String?, first: String?) {
    }

    override fun setAvatar(avatarString: String?) {
    }

    override fun setRating(selfRating: MutableMap<String, Any>?) {
    }

    override fun setMsgName(msgName: String?) {
    }

    override fun setProfileComPercent(num: String?) {
    }

    override fun setUnreadMsg(unreadMsg: String?) {
    }

    override fun setUnreadNoti(unreadNoti: String?) {
    }

    override fun setUnreadRecords(unreadRecords: MutableMap<String, Any>?) {
    }

    override fun setAvatarForMenu(avatar: String?) {
    }

    override fun switchProfileDialog(identify: String?) {
    }

}
