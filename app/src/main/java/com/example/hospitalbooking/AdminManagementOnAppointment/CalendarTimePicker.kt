package com.example.hospitalbooking.AdminManagementOnAppointment

import android.app.AlertDialog
import com.example.hospitalbooking.KotlinClass.MyCache
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.example.hospitalbooking.KotlinClass.appointmentViewModel
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.log

class CalendarTimePicker : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0
    var realDate=" "



    private lateinit var appointmentStoring:appointmentViewModel







    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_time_picker)
        pickDate()
//        checkAppointmentBooked()

    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)


    }

    private fun pickDate() {

        val btnTime = findViewById<Button>(R.id.btn_timePicker)
//        val btnEndTime = findViewById<Button>(R.id.btnEnd)
        val btnup1 = findViewById<Button>(R.id.btnup1)
//        val btnup2 = findViewById<Button>(R.id.btnup2)


        val doctorName = intent.getStringExtra("DoctorName")

        val cache= MyCache()
        val img= doctorName?.let { cache.retrieveBitmapFromCache(it) }
        val docImg=findViewById<ImageView>(R.id.docImg)
        docImg.setImageBitmap(img)

        var loginUser=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.email.toString()
            }

            else{

                loginUser=" NOne"
            }
//
        }

//        if(loginUser.contains("@student.tarc")) {

            val tvTime = findViewById<TextView>(R.id.tv_textTime)

            btnTime.setOnClickListener {

                getDateTimeCalendar()
                DatePickerDialog(this, this, year, month, day).show()
//                checkAppointmentBooked()
//                updateDoc()


//                tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"
            }

            btnup1.setOnClickListener {
//                checkAppointmentBooked()


                if(realDate==" "){

                    Toast.makeText(this,"No Date is entered",Toast.LENGTH_LONG).show()
                    tvTime.text="No Date is entered"
                }

                else{

                    if (!validateDateTime()) {
                        tvTime.text = "The Selected Date is passed today Date"

                    } else {
//                        updateDoc()
//                        tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"
                        checkAppointmentBooked()
                    }

                }





            }
//            val endTime = findViewById<TextView>(R.id.EndTime)
//
//
//            btnEndTime.setOnClickListener {
//                getDateTimeCalendar()
//
//                DatePickerDialog(this, this, year, month, day).show()
//
//
//
//
//                endTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"
//            }

//        btnup2.setOnClickListener {
//            if(realDate==" "){
//
//                Toast.makeText(this,"No Date is entered",Toast.LENGTH_LONG).show()
//                endTime.text="No Date is entered"
//            }
//
//            else{
//
//                if (!validateDateTime()) {
//                    endTime.text = "The Selected Date is passed today Date"
//
//                } else {
//                    endTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"
//                    setEndTime()
//                }
//            }
//
//
//
//
//        }

//        }

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        savedDay = p3
        savedMonth = p2+1
        savedYear = p1

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2
//        val tvTime = findViewById<TextView>(R.id.tv_textTime)
//        val endTime = findViewById<TextView>(R.id.EndTime)
//        tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"
//        endTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"





        var dateString = "$savedMonth/$savedDay/$savedYear"
        var timeString="$savedHour:$savedMinute"
        var totalDate="$savedMonth/$savedDay/$savedYear $savedHour:$savedMinute"







        when {
            savedHour<10 && savedMinute<10-> {
                totalDate="$savedMonth/$savedDay/$savedYear 0$savedHour:0$savedMinute"
            }
            savedHour<10 -> {
                totalDate="$savedMonth/$savedDay/$savedYear 0$savedHour:$savedMinute"
            }
            savedMinute<10 -> {
                totalDate="$savedMonth/$savedDay/$savedYear $savedHour:0$savedMinute"
            }




        }

        val dateTime = LocalDateTime.parse(totalDate, DateTimeFormatter.ofPattern("M/d/y HH:mm"))
//        val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/y"))
//        val time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:m"))
         realDate=dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))

//        val timestamp = java.sql.Timestamp.valueOf(realDate)




        Toast.makeText(this,"time$realDate",Toast.LENGTH_SHORT).show()


    }


    private fun validateDateTime(): Boolean {


        val now = Calendar.getInstance().time

        val formatter = SimpleDateFormat("dd MMM yyyy,HH:mm:ss")

        val date = formatter.parse(realDate)
        return date.after(now)
    }

    private fun checkAppointmentBooked(){
        val tvTime = findViewById<TextView>(R.id.tv_textTime)
        val loginUser=findGoogleUser()
        val doctorName = intent.getStringExtra("DoctorName").toString()
        if(savedHour in 1..9 ){


            tvTime.text="The doctor is on the way to clinic"


        }
        else if(savedHour==12&&minute>-1){

            tvTime.text="The doctor is on lunch time"

        }
        else if(savedHour >=17&&minute>-1){
            tvTime.text="The doctor is after office hours"

        }

        else {
            appointmentStoring = ViewModelProviders.of(this)[appointmentViewModel::class.java]
           checkAppoint(realDate, doctorName, loginUser)
//            Toast.makeText(this,"F$checkUser",Toast.LENGTH_LONG).show()

            tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"



        }






    }


    private fun updateDoc()
    {
        val doctorName = intent.getStringExtra("DoctorName")

        val mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val doctor= hashMapOf(
            "Time" to realDate




        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("doctor")?.document("$doctorName").update("Time",realDate) .addOnSuccessListener {


            Toast.makeText(this,"Successfully update doctor ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to update doctor",Toast.LENGTH_SHORT).show()
            }







    }

    private fun setEndTime()
    {
        val doctorName = intent.getStringExtra("DoctorName")

        val mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val doctor= hashMapOf(
            "Time2" to realDate




        )
        mFirebaseDatabaseInstance?.collection("doctor")?.document("$doctorName").update("Time2",realDate) .addOnSuccessListener {


            Toast.makeText(this,"Successfully update doctor ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to update doctor",Toast.LENGTH_SHORT).show()
            }



    }





    private fun writeUser(appointTime: String) {

        val doctorName = intent.getStringExtra("DoctorName")
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val loginUser=findGoogleUser()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()

//        val docName = intent.getStringExtra("DoctorName")

//        val loginUser=readUser()
        val user = hashMapOf(
            "doctorAppoint" to appointTime,
            "user" to loginUser,
            "docName" to doctorName


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


    private fun checkAppoint(time: String, docName: String, currentUser: String) {

        val appointmentList = ArrayList<String>()
        val userList = ArrayList<String>()
        var checkUser=""
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        var truth=true
        docRef?.whereEqualTo("docName", docName)?.get()?.addOnSuccessListener {


            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                val appointment = document.get("doctorAppoint").toString()
                val user = document.get("user").toString()
                appointmentList.add(appointment)
                userList.add(user)


            }


            for (i in appointmentList.indices) {
                if (appointmentList[i].contains(time) ) {

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
             truth= checkUser==currentUser||checkUser==""
//                    var invalid=checkUser[0]+"*****"


            if (truth) {

            Toast.makeText(this,"c$truth",Toast.LENGTH_LONG).show()
//                dialogToWriteUser()
                appointmentBuffer(truth)

//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Confirm Appointment")
//                builder.setMessage("Are you sure to appoint the Doctor?")
//
//
//                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                    Toast.makeText(
//                        applicationContext,
//                        android.R.string.yes, Toast.LENGTH_SHORT
//                    ).show()
//
//                    writeUser(time)
//                    val intent = Intent(this, DoctorAppointment::class.java)
////                        arraylistTime.remove(arraylistTime[deleteTime])
//                    //                intent.putExtra("DoctorName", nameNtime)
//                    startActivity(intent)
//
//
//
//                }
//
//                builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                    Toast.makeText(
//                        applicationContext,
//                        android.R.string.no, Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                builder.show()
//                true


//                sendNotifi()
            }


           else {

                appointmentBuffer(truth)
//                Toast.makeText(this,"c$truth",Toast.LENGTH_LONG).show()
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
                truth=false
            }


//            Toast.makeText(this,"is$checkUser",Toast.LENGTH_SHORT).show()

    }

    private fun appointmentBuffer(truth:Boolean){


        val tvTime = findViewById<TextView>(R.id.tv_textTime)
        var failDay = 0
        var failHour = 0
        var failMinute = 0
        var failMonth =0
        var failYear =0

//        appointmentStoring.failDay = savedDay
//        appointmentStoring.failHour = savedHour
//        appointmentStoring.failMinute = savedMinute
//        appointmentStoring.failMonth =savedMonth
//        appointmentStoring.failYear = savedYear

        if (!truth) {

            appointmentStoring.failDay = savedDay
            appointmentStoring.failHour = savedHour
            appointmentStoring.failMinute = savedMinute
            appointmentStoring.failMonth =savedMonth
            appointmentStoring.failYear = savedYear




        }
        else {
//                dialogToWriteUser()
            failDay = appointmentStoring.failDay
            failHour = appointmentStoring.failHour
            failMinute = appointmentStoring.failMinute
            failMonth = appointmentStoring.failMonth
            failYear = appointmentStoring.failYear
            val bufferTime=failMinute+30
            Toast.makeText(this,"F$failDay$failHour$failMinute$failMonth$failYear",Toast.LENGTH_LONG).show()
            if (failDay == savedDay && failMonth == savedMonth && failYear == savedYear
                && failHour == savedHour && savedMinute <bufferTime
            ) {

                Toast.makeText(this,"please make appointment after 30 mins ",Toast.LENGTH_LONG).show()
                tvTime.text="please make appointment after 30 mins from $failHour:$failMinute  $failDay/$failMonth/$failYear"
            }

            else{

                dialogToWriteUser()

            }




        }

    }









    private fun dialogToWriteUser(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Appointment")
                builder.setMessage("Are you sure to appoint the Doctor?")


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()

                    writeUser(realDate)
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



    }

    private fun findGoogleUser (): String {


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


        return loginUser
    }



}