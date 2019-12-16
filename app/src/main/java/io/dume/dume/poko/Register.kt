package io.dume.dume.poko

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class Register(var name: String, var birth_date: String, var mail: String, var nid: Long?, var parmanent_location: GeoPoint, var avatar: Uri?) {


}