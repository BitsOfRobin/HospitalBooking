package com.example.hospitalbooking.UserAppointmentManagement

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalbooking.Adapter.listCustomAdapter
import com.example.hospitalbooking.KotlinClass.AppointmentDetail
import com.example.hospitalbooking.KotlinClass.ModalFormMain
import com.example.hospitalbooking.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DoctorAppointmentViewModel:ViewModel() {

    private lateinit var mFirebaseDatabaseInstance: FirebaseFirestore
    val _arrCurrentAppoint= MutableLiveData<List<AppointmentDetail>>()
    val arrCurrentAppoint: LiveData<List<AppointmentDetail>>
        get() = _arrCurrentAppoint
    val _arrPastAppoint= MutableLiveData<List<AppointmentDetail>>()
    val arrPastAppoint: LiveData<List<AppointmentDetail>>
        get() = _arrPastAppoint

    private var docDetail:String?=null
    var arrayDel = ArrayList<String>()
     var arrayDelPast = ArrayList<String>()

    val arraylistAppointment = ArrayList<AppointmentDetail>()
     val arraylistPastAppointment = ArrayList<AppointmentDetail>()



    init {

        readUser()

    }


     fun timeToNoti(time:String) :Date{


        val dateInString=time.replace(" ", "-")



        val calendarDate = Calendar.getInstance().time





        val formatter = SimpleDateFormat("dd-MMM-yyyy,HH:mm:ss")


        val detect=dateInString.indexOf(",")
        val sub1=dateInString.substring(0,detect)
        val sub2=dateInString.substring(detect+2,dateInString.length)
        var properDate= "$sub1,$sub2"



//        if (properDate[0].toString().toInt() < 10&&properDate[1].toString().toInt()<0) {
//            properDate = "0$properDate"
//
//        }
        val date = formatter.parse(properDate)







            return date
//            sendNotifi(properDate)



    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
     fun readUser() {
        val doctor = FirebaseAuth.getInstance().currentUser
        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName = ArrayList<String>()

        var user = " "
        var doc = " "
        arraylistPastAppointment.clear()
        arraylistAppointment.clear()
        arrayDelPast.clear()
        arrayDel.clear()
//        val docView=findViewById<RecyclerView>(R.id.Rview)

        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()

            } else {

                user = " NOne"
            }

        }
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef.whereEqualTo("user", user).get().addOnSuccessListener {


            var docName = it.documents

    //                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())


                var docName = document.get("docName").toString()
                if (docName != null) {
                    arraylistDocName.add(docName)

                }



                doc = document.get("doctorAppoint").toString()

                if (doc != " ") {


                    timeToNoti(doc)
                }


                arraylistUser.add(user)
                if (user == null) {
                    arraylist.add("No records found")

                } else {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")

                    var dateInString = doc.replace(" ", "-")


                    val calendarDate = Calendar.getInstance().time
                    val detect = dateInString.indexOf(",")
                    val sub1 = dateInString.substring(0, detect)
                    val sub2 = dateInString.substring(detect + 2, dateInString.length)
                    dateInString = "$sub1,$sub2"


                    val formatter = SimpleDateFormat("dd-MMM-yyyy,HH:mm:ss")
                    val date = formatter.parse(dateInString)









                    if (calendarDate.before(date)) {

                        arraylistAppointment.add(AppointmentDetail(user, docName, doc))

                        arrayDel.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                    } else {

                        arraylistPastAppointment.add(AppointmentDetail(user, docName, doc))
                        arrayDelPast.add("{docName=$docName, doctorAppoint=$doc, user=$user}")

                    }


                }


            }


        }


        _arrPastAppoint.value=arraylistPastAppointment
        _arrCurrentAppoint.value=arraylistAppointment

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteUser(i:Int){

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val userGoogle = Firebase.auth.currentUser
        var user=" "
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
            } else {

                user = " NOne"
            }
        }



                val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDel.elementAt(i)}")

// Remove the 'capital' field from the document
                val updates = hashMapOf<String, Any>(
                    "user" to FieldValue.delete(),
                    "doctorAppoint" to FieldValue.delete(),
                    "docName" to FieldValue.delete()
                )

                docRef.update(updates).addOnCompleteListener {



                }

                docRef
                    .delete()
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {  }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


                arraylistAppointment.removeAt(i)
                _arrCurrentAppoint.value=arraylistAppointment
//                refreshAppoint()

            }













    @RequiresApi(Build.VERSION_CODES.O)
     fun deleteUserPast(i:Int){

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val userGoogle = Firebase.auth.currentUser
        var user=" "
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
            } else {

                user = " NOne"
            }
        }





                val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDelPast.elementAt(i)}")

// Remove the 'capital' field from the document
                val updates = hashMapOf<String, Any>(
                    "user" to FieldValue.delete(),
                    "doctorAppoint" to FieldValue.delete(),
                    "docName" to FieldValue.delete()
                )

                docRef.update(updates).addOnCompleteListener {



                }

                docRef
                    .delete()
                    .addOnSuccessListener {   }
                    .addOnFailureListener {  }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
                arraylistPastAppointment.removeAt(i)
                _arrPastAppoint.value=arraylistPastAppointment
//                refreshAppoint()

            }






        }







