package com.example.hospitalbooking

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentSelect : AppCompatActivity() {
    //    private var arraylistTime = ArrayList<String>()
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    private var docName = " "
    var nameNtime: String = " "
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_select)

        val user = validAppoint()
        createNoti()

//        appointSelect()
    }


    private fun readUser(): String {
        val arraylist = ArrayList<String>()
        var userId = " "
        val docRef = mFirebaseDatabaseInstance?.collection("ValidUser")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {


                userId = document.get("userId").toString()

                if (userId == null) {
                    arraylist.add("No records found")

                }

            }


        }

        return userId
    }

    private fun validAppoint(): String {


        var user = " "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                appointSelect()
            } else {

                val intent = Intent(this, UserLogin::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
                startActivity(intent)
            }

        }


        return user
    }


    private fun appointSelect() {
        var arraylistTime = ArrayList<String>()
        arraylistTime.clear()
        val tempHolder = intent.getStringExtra("DoctorName")
        val docSpin = findViewById<Spinner>(R.id.spinDocAppoint)
        docName = tempHolder.toString()
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

//        val arraylistTime2= ArrayList<String>()
        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        val calendarDate = Calendar.getInstance().time
        docRef?.whereEqualTo("name", tempHolder)?.get()?.addOnSuccessListener {


            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                val time = document.get("Time").toString()
                val time2 = document.get("Time2").toString()

                var dateInString = time.replace(" ", "-")


                if (dateInString[0].toString().toInt() < 10) {
                    dateInString = "0$dateInString"

                }
                val calendarDate = Calendar.getInstance().time




                val formatter = SimpleDateFormat("dd-MMM-yyyy")
                val date = formatter.parse(dateInString)
                if (calendarDate.before(date) ) {
                    arraylistTime.add(time)
                }
                else{

                    arraylistTime.add("appointment past")
                }



                var dateInString2 = time2.replace(" ", "-")


                if (dateInString2[0].toString().toInt() < 10) {
                    dateInString2 = "0$dateInString2"

                }
//                val calendarDate2 = Calendar.getInstance().time




//                val formatter = SimpleDateFormat("dd-MMM-yyyy")
                val date2 = formatter.parse(dateInString2)
                if (calendarDate.before(date2) ) {
                    arraylistTime.add(time2)
                }

                else{

                    arraylistTime.add("appointment past")
                }


//                    if (time.contains("0") || time2.contains("0")) {
//
//                    arraylistTime.add(time2)
//
//                }
//





//                var time2 = document.get("Time2") as com.google.firebase.Timestamp
//                val date2 = time2.toDate()
//
////                if(calendarDate.before(date2))
////                {
//                arraylistTime.add(date2.toString())

//                }


            }


        }


        var appointedTime = " "

//        arraylistTime= arrayListOf(arraylistTime.toString())
        arraylistTime.add(0, "Choose Your Appointment")
//        Toast.makeText(this, "arraTime=$arraylistTime", Toast.LENGTH_SHORT).show()
//        for(i in arraylistTime.indices)
//        {
//            if(arraylistTime[i].isEmpty())
//            {
//                arraylistTime.remove(arraylistTime[i])
//            }
//
//        }
//        Toast.makeText(this, "arraTime2=$arraylistTime", Toast.LENGTH_SHORT).show()
        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arraylistTime)
        docSpin.adapter = arr

        val btn = findViewById<Button>(R.id.btn)

        val txt = findViewById<TextView>(R.id.txtV)
        var checkBtn: Boolean = true
//            val str=docSpin.selectedItem.toString()
//            val str=arraylistTime[i].toString()
//        val str=docSpin.selectedIte()
//        btn.isClickable=false

        var deleteTime = 0
        docSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (arraylistTime[p2] != null) {
                    txt.textSize = 20f
                    txt.text = "Selected: " + arraylistTime[p2]
                    appointedTime = arraylistTime[p2]

//                    nameNtime = tempHolder +"\n" +arraylistTime[p2].toString()
//                    val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
                    deleteTime = p2
                    btn.isEnabled = true

                    checkBtn = true

                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

                checkBtn = false
                btn.isClickable = false

            }


        }
//


            btn.setOnClickListener() {

                if (appointedTime.contains("0")) {
                    val user = validAppoint()
                    val checkUser = checkAppoint(appointedTime, tempHolder.toString(), user)

//                    val truth: Boolean = checkUser==user||checkUser==" "
////                    var invalid=checkUser[0]+"*****"
//
//
//                                if (truth) {
//                                    val builder = AlertDialog.Builder(this)
//                                    builder.setTitle("Confirm Appointment")
//                                    builder.setMessage("Are you sure to appoint the Doctor?")
//
//
//                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                        Toast.makeText(
//                                            applicationContext,
//                                            android.R.string.yes, Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        writeUser(appointedTime)
//                                        val intent = Intent(this, DoctorAppointment::class.java)
//                                        arraylistTime.remove(arraylistTime[deleteTime])
//            //                intent.putExtra("DoctorName", nameNtime)
//                                        startActivity(intent)
//
//
//
//                                    }
//
//                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                        Toast.makeText(
//                                            applicationContext,
//                                            android.R.string.no, Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//
//                                    builder.show()
//                                    true
//
//
//                                    sendNotifi()
//                                }
//
//
//                            else{
//
//
//                                        Toast.makeText(
//                                            this,
//                                            "The appointment is booked by other user,$checkUser ",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//
////
//
//                                }


                }

                else{
                    Toast.makeText(
                        this,
                        "No input detected ",
                        Toast.LENGTH_SHORT
                    ).show()

                }
//
//                writeUser(appointedTime)
                }




        }

        private fun createNoti() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val name = "Hospital Appointmnet"
                val txt = "Your Appointment is approaching"
                val imp = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, imp).apply {
                    description = txt
                }

                val notiManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notiManager.createNotificationChannel(channel)


            }

        }

        private fun sendNotifi() {
            val builder =
                NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.appointment)
                    .setContentText("Your Appointment is booked")
                    .setContentTitle("Hospital Appointment")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {

                notify(notificationId, builder.build())
            }

        }


        private fun writeUser(appointTime: String) {

//        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
            mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

            val arraylist = ArrayList<String>()
            val arraylistPro = ArrayList<String>()
            //        val docView=findViewById<RecyclerView>(R.id.Rview)
//        val docView=findViewById<ListView>(R.id.Rview)
            //        val txt=findViewById<TextView>(R.id.txtV)
            //        val name=findViewById<TextView>(R.id.txtName)
            //        val pro=findViewById<TextView>(R.id.txtPro)

            var loginUser = " "
            val userGoogle = Firebase.auth.currentUser
            userGoogle.let {
                // Name, email address, and profile photo Url
//                    val name = user.displayName
                if (userGoogle != null) {
                    loginUser = userGoogle.displayName.toString()
                } else {

                    loginUser = " NOne"
                }
//                    val photoUrl = user.photoUrl
//
//                    // Check if user's email is verified
//                    val emailVerified = user.isEmailVerified
//
//                    // The user's ID, unique to the Firebase project. Do NOT use this value to
//                    // authenticate with your backend server, if you have one. Use
//                    // FirebaseUser.getToken() instead.
//                    val uid = user.uid
            }
//        val loginUser=readUser()
            val user = hashMapOf(
                "doctorAppoint" to appointTime,
                "user" to loginUser,
                "docName" to docName


            )
//        val  doc =doctor?.uid

//
            mFirebaseDatabaseInstance?.collection("userAppointment")?.document("$user")?.set(user)
                ?.addOnSuccessListener {


//            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

                }
                ?.addOnFailureListener {

                    Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                }


//        userNum+=1


        }


        private fun checkAppoint(time: String, docName: String, currentUser: String): String {

            val appointmentList = ArrayList<String>()
            val userList = ArrayList<String>()
            var checkUser=" "
            val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")

            docRef?.whereEqualTo("docName", docName)?.get()?.addOnSuccessListener {


                for (document in it) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val appointment = document.get("doctorAppoint").toString()
                    val user = document.get("user").toString()
                    appointmentList.add(appointment)
                    userList.add(user)
//                    Toast.makeText(this,"ap1=$appointmentList",Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this,"ap2=$userList",Toast.LENGTH_SHORT).show()

                }


                for (i in appointmentList.indices) {
                    if (appointmentList[i].contains(time)) {

                        checkUser=userList[i]
//                        Toast.makeText(this,userList.elementAt(i).toString(),Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this,"iscurrent$checkUser",Toast.LENGTH_SHORT).show()

                    }

                }
                val star=ArrayList<String>()

                val len=checkUser.length
                for(i in 0..len)
                {
                    star.add(i,"*")

                }




                val astri=star.joinToString("")
                val truth: Boolean = checkUser==currentUser||checkUser==" "
//                    var invalid=checkUser[0]+"*****"


                if (truth) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirm Appointment")
                    builder.setMessage("Are you sure to appoint the Doctor?")


                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            applicationContext,
                            android.R.string.yes, Toast.LENGTH_SHORT
                        ).show()

                        writeUser(time)
                        val intent = Intent(this, DoctorAppointment::class.java)
//                        arraylistTime.remove(arraylistTime[deleteTime])
                        //                intent.putExtra("DoctorName", nameNtime)
                        startActivity(intent)



                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            applicationContext,
                            android.R.string.no, Toast.LENGTH_SHORT
                        ).show()
                    }

                    builder.show()
                    true


                    sendNotifi()
                }


                else{



                    val taken= checkUser[0]+"$astri"
                    Toast.makeText(
                        this,
                        "The appointment is booked by other user,$taken",
                        Toast.LENGTH_SHORT
                    ).show()


//

                }

            }

                ?.addOnFailureListener {
                    Toast.makeText(this,"Failed to get appointment",Toast.LENGTH_SHORT).show()

                }


//            Toast.makeText(this,"is$checkUser",Toast.LENGTH_SHORT).show()
            return checkUser
        }

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val txt=findViewById<TextView>(R.id.textView)
//            txt.text=arraylistTime[p2].toString()
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        val txt=findViewById<TextView>(R.id.textView)
//        txt.text="Please select 1 item from spinner"
//    }

}