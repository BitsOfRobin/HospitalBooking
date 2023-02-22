package com.example.hospitalbooking.BookingAppointment

import com.example.hospitalbooking.KotlinClass.MyCache
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.*
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.UserLogin
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AppointmentSelect : AppCompatActivity() {
    //    private var arraylistTime = ArrayList<String>()
    private lateinit var toggle:ActionBarDrawerToggle
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101
    private var docName = " "
    var nameNtime: String = " "
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_select)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Choose Appointment")
        val user = validAppoint()
        createNoti()
        showNavBar()
//        appointSelect()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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
        val img=findViewById<ImageView>(R.id.imageSelect)
        val cache= MyCache()
        img.setImageBitmap(cache.retrieveBitmapFromCache(tempHolder.toString()))
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
                val rating=document.get("rating")
                val rateFrequency=document.get("rateFrequency")
                val ratingBr=findViewById<RatingBar>(R.id.ratingBar)
                ratingBr.isClickable=false
                if(rating!=null){


                    retrieveStars(rating as Double,ratingBr,rateFrequency as Double)

                }
                else{
                    ratingBr.visibility=View.GONE

                }
                var dateInString = time.replace(" ", "-")
                val detect=dateInString.indexOf(",")
                val sub1=dateInString.substring(0,detect)
                val sub2=dateInString.substring(detect+2,dateInString.length)
                var properDate= "$sub1,$sub2"

//                if (properDate[0].toString().toInt() < 10&&properDate[1].toString().toInt()<0) {
//                    properDate = "0$properDate"
//
//                }
                val calendarDate = Calendar.getInstance().time




                val formatter = SimpleDateFormat("dd-MMM-yyyy,HH:mm:ss")

                val date = formatter.parse(properDate)
                if (calendarDate.before(date) ) {
                    arraylistTime.add(time)
                }
                else{

                    arraylistTime.add("appointment past")
                }



                var dateInString2 = time2.replace(" ", "-")
                val detect2=dateInString2.indexOf(",")

                val sub1T=dateInString2.substring(0,detect2)
                val sub2T=dateInString2.substring(detect2+2,dateInString2.length)
                var properDate2= "$sub1T,$sub2T"

                if (properDate2[0].toString().toInt() < 10) {
                    properDate2 = "0$properDate2"

                }
//                val calendarDate2 = Calendar.getInstance().time




//                val formatter = SimpleDateFormat("dd-MMM-yyyy")
                val date2 = formatter.parse(properDate2)
                if (calendarDate.before(date2) ) {
                    arraylistTime.add(time2)
                }

                else{

                    arraylistTime.add("appointment past")
                }





            }


        }


        var appointedTime = " "

//        arraylistTime= arrayListOf(arraylistTime.toString())
        arraylistTime.add(0, "Choose Your Appointment")

        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_checked, arraylistTime)
        docSpin.adapter = arr

        val btn = findViewById<Button>(R.id.btn)

        val txt = findViewById<TextView>(R.id.txtV)
        var checkBtn: Boolean = true


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

    private fun retrieveStars(rating: Double, ratingBr: RatingBar, rateFrequency: Double) {
        ratingBr.rating= (rating/rateFrequency).toFloat()
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


            mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

            val arraylist = ArrayList<String>()
            val arraylistPro = ArrayList<String>()


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


                }


                for (i in appointmentList.indices) {
                    if (appointmentList[i].contains(time)) {

                        checkUser=userList[i]


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





    private fun showNavBar(){


        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view=findViewById<NavigationView>(R.id.nav_view)
        toggle= ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_BookAppoint -> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }




                R.id.nav_Pres -> {
                    val intent = Intent(this, PrescriptionDisplay::class.java)
                    startActivity(intent)

                }
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
                R.id.nav_viewAppoint -> {
                    val intent = Intent(this, DoctorAppointment::class.java)
                    startActivity(intent)

                }
                R.id.nav_medicineRecord -> {
                    val  intent = Intent(this, MedicineRecord::class.java)
                    startActivity(intent)

                }
                R.id.nav_OCR -> {
                    val intent = Intent(this, UserMedicine::class.java)
                    startActivity(intent)
                }





            }


            true

        }





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true


        }


        return super.onOptionsItemSelected(item)
    }
}