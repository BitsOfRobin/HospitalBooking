package com.example.hospitalbooking.AdminManagementOnAppointment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalbooking.Adapter.FeedbackReviewAdapter
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.DoctorInformationManagement.SummarizeReportViewModel
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.KotlinClass.appointmentViewModel
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import android.Manifest

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



    private lateinit var toggle:ActionBarDrawerToggle

    //private lateinit var mapView: MapView
    private lateinit var appointmentStoring:appointmentViewModel
    private lateinit var calendarTimePickerViewModel: CalendarTimePickerViewModel
    private val doctorAppointmentList = ArrayList<String>()
    private val checkUserList = ArrayList<String>()

    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val doctorName = intent.getStringExtra("DoctorName").toString()
        val doctorSpecial = intent.getStringExtra("DoctorPro").toString()
        //val doctorLocation = intent.getStringExtra("hospital").toString()
        //val docName = findViewById<TextView>(R.id.docNameLocation)
        //val docSpecialist = findViewById<TextView>(R.id.docSpecialistLocation)

        //docName.setText(doctorName)
        //docSpecialist.setText(docProLocate)
//        val docNameLocate = intent.getStringExtra("DoctorName").toString()
        //val docProLocate = intent.getStringExtra("DoctorPro").toString()

        getDoctorAppointment(doctorName)
        //mapLocationAPI(savedInstanceState, doctorName, docProLocate)
//        val buttonGoogleMap = findViewById<Button>(R.id.btn_map)
//        val doctorName = intent.getStringExtra("DoctorName").toString()
//        val hospitalName=hospitaLocation(doctorName)
//
//            val intent=Intent(this,HospitalLocation::class.java)
//            intent.putExtra("HospitalName",hospitalName)
//            startActivity(intent)


//        val doctorName = intent.getStringExtra("DoctorName").toString()
//        val btn=findViewById<Button>(R.id.btn_map)
//        btn.setOnClickListener {
//            val hospitalName=hospitaLocation(doctorName)
//
//            val intent=Intent(this,HospitalLocation::class.java)
//            intent.putExtra("HospitalName",hospitalName)
//            startActivity(intent)
//
//        }




//        }


//Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show()
//        }
        setContentView(R.layout.activity_calendar_time_picker)
        pickDate()
//        checkAppointmentBooked()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Choose Doctor Appointment")

        var rating = ""
        var doctorLocation = ""
        var doctorSpecialist = ""
        var numberFeedback = 0

        val docLocation = findViewById<TextView>(R.id.dtHos)
        val ratingBar = findViewById<RatingBar>(R.id.accuRate)
        val docJob = findViewById<TextView>(R.id.dtPro)
        val docName = findViewById<TextView>(R.id.dotName)
        val numRate = findViewById<TextView>(R.id.numRate)
        val navHospital = findViewById<TextView>(R.id.map)

        mFirebaseDatabaseInstance?.collection("doctor")?.whereEqualTo("name", doctorName)
            ?.get()?.addOnSuccessListener {

                for(document in it){
                    doctorLocation = document.get("hospital").toString()
                    doctorSpecialist = document.get("pro").toString()
                    rating = document.get("rateFrequency").toString()
                    numberFeedback = (document.get("numRatings") as? Long?: 0).toInt()
                }
                docLocation.text = doctorLocation
                docJob.text = doctorSpecialist
                ratingBar.rating = rating.toFloat()
                docName.text = doctorName
                numRate.text = numberFeedback.toString()

                navHospital.setOnClickListener {
                    navigateHospitalGM(doctorLocation)
                }
            }
            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
        val userName=findGoogleUser()

        showNavBar()

        calendarTimePickerViewModel = ViewModelProvider(this)[CalendarTimePickerViewModel::class.java]
        getFeedbackReview(doctorName)

    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    @RequiresApi(Build.VERSION_CODES.O)
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

                        var valid:Boolean=true
                        var check=0
                        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")



                        for(i in doctorAppointmentList.indices){

                            var dateInString=doctorAppointmentList[i]

                            if(dateInString[0].toString().toInt()<10
                                &&dateInString[1].toString()==" "){

                                dateInString="0$dateInString"
                            }




                            val dateTime = LocalDateTime.parse(dateInString, formatter)

                            val day = dateTime.dayOfMonth
                            val month = dateTime.monthValue
                            val hour = dateTime.hour
//                            val minute = dateTime.minute
                            val year = dateTime.year
//                            var hourMinus=0
//                            hourMinus=hour-savedHour
//                            Toast.makeText(this,"hour$hourMinus",Toast.LENGTH_LONG).show()






//                            if(year==savedYear&&month==savedMonth&&day==savedDay){
//                                valid=checkAppointmentBooked(doctorAppointmentList[i])
                                valid=timeToNotiAfter(dateInString,checkUserList[i])

//                            }
//                            else if(year==savedYear&&month==savedMonth&&day==savedDay&&hour<savedHour){
//                                Toast.makeText(this,"hour$hour",Toast.LENGTH_LONG).show()
////                                valid=checkAppointmentBooked(doctorAppointmentList[i])
//                                valid=timeToNotiBefore(doctorAppointmentList[i])
//                            }

//                            if(year==savedYear&&month==savedMonth&&day==savedDay&&(hour<=savedHour))
                            if(!valid){



                                check++

                            }
                        }
                        if(check==0){

                            dialogToWriteUser()
                        }
                    }
                }
            }
    }

    private fun sundayValidation(date:String):Boolean{


        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
        val date = sdf.parse(date)

//        Toast.makeText(this,"$date",Toast.LENGTH_LONG).show()
        if (date != null) {
            if(date.toString().contains("sun",true)){

                return false

            }
        }

        return true


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAppointmentBooked(time:String):Boolean{
        val tvTime = findViewById<TextView>(R.id.tv_textTime)
        val loginUser=findGoogleUser()
        val doctorName = intent.getStringExtra("DoctorName").toString()
        var valid=true
        if(savedHour in 0..9 ){


            tvTime.text="The doctor is on the way to clinic"


        }
        else if(savedHour==12&&minute>-1){

            tvTime.text="The doctor is on lunch time"

        }
        else if(savedHour >=17&&minute>-1){
            tvTime.text="The doctor is after office hours"

        }

        else {
//            appointmentStoring = ViewModelProviders.of(this)[appointmentViewModel::class.java]
//             valid=timeToNotiAfter(time)

           checkAppoint(realDate, doctorName, loginUser)
//            Toast.makeText(this,"F$checkUser",Toast.LENGTH_LONG).show()

//            tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"

            return valid

        }


        return valid



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
        mFirebaseDatabaseInstance.collection("doctor").document("$doctorName")
            .update("Time",realDate)
            .addOnSuccessListener {


                Toast.makeText(this,"Successfully update doctor ",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {

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
        mFirebaseDatabaseInstance?.collection("doctor")?.document("$doctorName")
            ?.update("Time2",realDate)
            ?.addOnSuccessListener {


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
        val userAppointmentID = hashMapOf(
            "doctorAppoint" to appointTime,
            "user" to loginUser,
            "docName" to doctorName,



        )



        val user = hashMapOf(
            "doctorAppoint" to appointTime,
            "user" to loginUser,
            "docName" to doctorName,
        "commentStatus" to "Not Commented",
            "visitStatus" to "Not Visited",


        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("userAppointment")?.document("$userAppointmentID")?.set(user)
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
//                    appointmentBuffer(truth)
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
//            appointmentBuffer(truth)
//                    var invalid=checkUser[0]+"*****"


            if (truth) {

            Toast.makeText(this,"c$truth",Toast.LENGTH_LONG).show()
//                dialogToWriteUser()
//                appointmentBuffer(truth)

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

//                appointmentBuffer(truth)
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


    private fun hideUserName(str:String):String{
        val star=ArrayList<String>()
        val currentUser=findGoogleUser()


        if(currentUser!=str){
                val len=str.length
                for(i in 0..len)
                {
                    star.add(i,"*")

                }



                val astri=star.joinToString("")
                val taken= str[0]+"$astri"
                Toast.makeText(
                    this,
                    "The appointment is booked by other user,$taken",
                    Toast.LENGTH_LONG
                ).show()
            return taken

            }
        else{

                Toast.makeText(
                    this,
                    "The appointment is booked by you already,$currentUser",
                    Toast.LENGTH_LONG
                ).show()
            return currentUser

            }


    }


    private fun getDoctorAppointment( docName: String) {


        val userList = ArrayList<String>()

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")

        docRef?.whereEqualTo("docName", docName)?.get()?.addOnSuccessListener {


            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                val appointment = document.get("doctorAppoint").toString()
                val user = document.get("user").toString()
                doctorAppointmentList.add(appointment)
                checkUserList.add(user)


            }



//

            }



            ?.addOnFailureListener {
                Toast.makeText(this,"Failed to get appointment",Toast.LENGTH_SHORT).show()

            }


//            Toast.makeText(this,"is$checkUser",Toast.LENGTH_SHORT).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeToNotiAfter(time:String,userName:String):Boolean
    {



//        val dateString = "23 Mar 2023, 10:05:00"
        val loginUser=findGoogleUser()



        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        val dateTime = LocalDateTime.parse(time, formatter)
        val day = dateTime.dayOfMonth
        val month = dateTime.monthValue
        val hour = dateTime.hour
        val minute = dateTime.minute
        val year = dateTime.year


        val tvTime = findViewById<TextView>(R.id.tv_textTime)

//        val dateString = "23 Mar 2023, 10:05:00"
//        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.ENGLISH)
//        val date = dateFormat.parse(time) // parses the date string into a Date object
//        val calendar = Calendar.getInstance() // creates a new Calendar object
//        calendar.time = date // sets the Calendar object's time to the parsed Date object
//        val day = calendar.get(Calendar.DAY_OF_MONTH) // retrieves the day information from the Calendar object
//        val month = calendar.get(Calendar.MONTH) + 1
//        val hour=calendar.get(Calendar.HOUR_OF_DAY)
//        val minute=calendar.get(Calendar.M)
//        val year=calendar.get(Calendar.YEAR)
        var bufferMinute=0
        var bufferHour=0

        var bufferHourb4=0
        var bufferMinuteb4=0
        var hourb4appointment=hour-1












        if(minute<29){

            bufferMinute=minute+30
            bufferHour=hour
        }
        else if(minute>29){

            bufferMinute= minute-30
            bufferHour=hour+1

        }

        if(savedHour in 0..8 ){


            tvTime.text="The doctor is on the way to clinic"
            return false

        }
        else if(savedHour==12&&minute>-1){

            tvTime.text="The doctor is on lunch time"
            return false
        }
        else if(savedHour >=17&&minute>-1){
            tvTime.text="The doctor is after office hours"
            return false
        }

        else if (day== savedDay && month == savedMonth && year == savedYear
        && hour== savedHour && savedMinute==minute
        )
        {
            val hideName=hideUserName(userName)
            Toast.makeText(this,"please make appointment after 30 mins ",Toast.LENGTH_LONG).show()
            tvTime.text="Appointment is already booked by other user $hideName at  $hour:$minute  $day/$month/$year"

            return false
        }


        else if(savedMinute==30||savedMinute==0){
//            tvTime.text="Appointment is booked successfully at  ${savedHour}:${savedMinute}  ${savedDay}/${savedMonth}" +
//                    "/${savedYear} by $loginUser"
            return true

        }

        else{
            tvTime.text="Appointment cannot be booked   at  ${savedHour}:${savedMinute}  ${savedDay}" +
                    "/${savedMonth}/${savedYear} as only Appointmnet start at 00 or 30"






            return false
        }



//        else if (day== savedDay && month == savedMonth && year == savedYear
//            && (bufferHour== savedHour && savedMinute<=bufferMinute ||
//                    hour== savedHour && savedMinute<minute+30)
//        )
//        {
//
//            Toast.makeText(this,"please make appointment after 30 mins ",Toast.LENGTH_LONG).show()
//            tvTime.text="compile please make appointment after 30 mins from $hour:$minute  $day/$month/$year"
//
//            return false
//        }







//        return true


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeToNotiBefore(time:String):Boolean
    {



//        val dateString = "23 Mar 2023, 10:05:00"
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        val dateTime = LocalDateTime.parse(time, formatter)
        val day = dateTime.dayOfMonth
        val month = dateTime.monthValue
        val hour = dateTime.hour
        val minute = dateTime.minute
        val year = dateTime.year


        val tvTime = findViewById<TextView>(R.id.tv_textTime)

//        val dateString = "23 Mar 2023, 10:05:00"
//        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.ENGLISH)
//        val date = dateFormat.parse(time) // parses the date string into a Date object
//        val calendar = Calendar.getInstance() // creates a new Calendar object
//        calendar.time = date // sets the Calendar object's time to the parsed Date object
//        val day = calendar.get(Calendar.DAY_OF_MONTH) // retrieves the day information from the Calendar object
//        val month = calendar.get(Calendar.MONTH) + 1
//        val hour=calendar.get(Calendar.HOUR_OF_DAY)
//        val minute=calendar.get(Calendar.M)
//        val year=calendar.get(Calendar.YEAR)
//        var bufferMinute=0
//        var bufferHour=0
//
        var bufferHourb4=0
        var bufferMinuteb4=0
        var hourb4appointment=0
        hourb4appointment=hour
//        if(minute<29){




        if(hourb4appointment>=savedHour){


            if(minute<29){

                bufferMinuteb4=savedMinute+30
                bufferHourb4=hour
            }
            else if(minute>29){

                bufferMinuteb4= +(savedMinute+30-60)
                bufferHourb4=hour+1

            }

        }

//
//        else {
//
//
//            if(minute<29){
//
//                bufferMinuteb4=savedMinute+30
//                bufferHourb4=hour
//            }
//            else if(minute>29){
//
//                bufferMinuteb4= +(savedMinute+30-60)
//                bufferHourb4=hour+1
//
//            }
//
//        }




        if(savedHour in 0..9 ){


            tvTime.text="The doctor is on the way to clinic"

            return false
        }
        else if(savedHour==12&&minute>-1){

            tvTime.text="The doctor is on lunch time"
            return false
        }
        else if(savedHour >=17&&minute>-1){
            tvTime.text="The doctor is after office hours"
            return false
        }

        else if (   bufferMinuteb4<minute+30 )
        {

            Toast.makeText(this,"please make appointment after 30 mins current booked ",Toast.LENGTH_LONG).show()
            tvTime.text="before please make appointment after 30 mins from $hour:$minute  $day/$month/$year"

            return false
        }

        else{

            Toast.makeText(this,"failed to validate hour$hourb4appointment ",Toast.LENGTH_LONG).show()
        }

        return true


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
        val tvTime=findViewById<TextView>(R.id.tv_textTime)
        val loginUser=findGoogleUser()
        builder.setTitle("Confirm Appointment")
                builder.setMessage("Are you sure to appoint the Doctor?")


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()


                    if(sundayValidation(realDate)){


                        tvTime.text="Appointment is booked successfully at  ${savedHour}:${savedMinute}  ${savedDay}/${savedMonth}" +
                                "/${savedYear} by $loginUser"


                        writeUser(realDate)
                        val intent = Intent(this, DoctorAppointment::class.java)
                        startActivity(intent)
                    }
                    else{


                        tvTime.text="This Date is Sunday, the doctor is off duty"

                    }







//                        arraylistTime.remove(arraylistTime[deleteTime])
                    //                intent.putExtra("DoctorName", nameNtime)




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
                val imgUrl=userGoogle.photoUrl
                naviImg(imgUrl,loginUser)
            } else {

                loginUser = " NOne"
            }

        }


        return loginUser
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


    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser
    }

    private fun mapLocationAPI(savedInstanceState: Bundle?, docNameLocate : String, docProLocate : String) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.hopsital_location_map, null)

        val mapView = view.findViewById(R.id.map_view) as MapView

        // Receive parameter from the function
        var hospitalName = hospitaLocation(docNameLocate)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            // do something with the googleMap object
            var latitude = 0.0
            var longitude = 0.0

            val geocoder = Geocoder(this)
            val addressList = geocoder.getFromLocationName(hospitalName, 1)
            if (addressList.isNotEmpty()) {
                latitude = addressList[0].latitude
                longitude = addressList[0].longitude

                // add marker and move camera to the building location
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(latitude, longitude))
                markerOptions.title(hospitalName)
                googleMap.addMarker(markerOptions)

                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            }
        }

        builder.setView(view)

        builder.setPositiveButton("Submit") { dialog, which ->

        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this,"Thanks",Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.show()
    }

//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }


    private fun hospitaLocation(docNameLocate : String) : String{

        //val docName = findViewById<TextView>(R.id.docNameLocation)
        //val docSpecialist = findViewById<TextView>(R.id.docSpecialistLocation)
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        //docName.setText(docNameLocate)
        //docSpecialist.setText(docProLocate)

        var hospitalName = ""

        mFirebaseDatabaseInstance!!.collection("doctor").document(docNameLocate)
            .get()
            .addOnSuccessListener {
                hospitalName = it.get("hospital").toString()
                Toast.makeText(this, "The hospital name ${hospitalName} successfully retrieved", Toast.LENGTH_SHORT).show()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Fail to retrieve", Toast.LENGTH_SHORT).show()
            }

//        val hostName = findViewById<TextView>(R.id.appointment_details_location)
//        hostName.setText(hospitalName)
        // Return hospital name value
        return hospitalName
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getFeedbackReview(docName: String) {
        val feedbackReviewView = findViewById<RecyclerView>(R.id.feedbackReviewRecycler)

        if (feedbackReviewView != null) {
            feedbackReviewView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            calendarTimePickerViewModel.feedbackReview(docName)

            calendarTimePickerViewModel.feedbackReview.observe(this) { feedbackReview ->
                val arrFeedback = FeedbackReviewAdapter(this, feedbackReview as ArrayList<feedbackReview>)
                arrFeedback.notifyDataSetChanged()
                feedbackReviewView.adapter = arrFeedback
            }
        } else {
            Toast.makeText(this, "commentView is null", Toast.LENGTH_SHORT).show()
        }
    }

    fun navigateHospitalGM(hospitalLocation: String){
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(hospitalLocation, 1)
        if (addresses.isNotEmpty()) {
            val hospitalLatLng = LatLng(addresses[0].latitude, addresses[0].longitude)
            val uri = "geo:${hospitalLatLng.latitude},${hospitalLatLng.longitude}?q=${hospitalLatLng.latitude},${hospitalLatLng.longitude}($hospitalLocation)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (currentLocation != null) {
                    val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    val results = FloatArray(1)
                    Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, hospitalLatLng.latitude, hospitalLatLng.longitude, results)
                    val distance = String.format("%.2f km", results[0] / 1000)
                    intent.putExtra("distance", distance)
                }
            }
            val distances = intent.getStringExtra("distance")
            if (distances != null) {
                Toast.makeText(this, "Distance to hospital: $distances", Toast.LENGTH_SHORT).show()
            }
            startActivity(intent)
        } else {
            // Handle case where no location is found
            Toast.makeText(this, "No hospital location can found", Toast.LENGTH_SHORT).show()
        }
    }
}