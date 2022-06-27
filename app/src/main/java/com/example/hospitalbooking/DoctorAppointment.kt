package com.example.hospitalbooking



//import com.example.hospitalsmartt.databinding.ActivityMainBinding
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*



class DoctorAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userNum:Int=0
    private var docDetail:String?=null
    private var arrayDel = ArrayList<String>()
    private val CHANNEL_ID="channel_id_example_01"
    private  val notificationId=101
    //    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointment)



//        writeUser()
        readUser()
        deleteUser()
        refresh()
        createNoti()


    }
    private fun timeToNoti(time:String)
    {

        var dateInString = time
        dateInString.replace(" ", "-")


        if (dateInString[0].toString().toInt() < 10&&dateInString[1].toString().contains(" ")) {
            dateInString = "0$dateInString"

        }
        else
        {

            dateInString=dateInString
        }
        val calendarDate = Calendar.getInstance().time


//        val utc = TimeZone.getTimeZone("UTC")
//        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
//        val destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
//        sourceFormat.timeZone =utc

//
//        val convertedDate = sourceFormat.parse(dateInString)
//        destFormat.format(convertedDate)


        val formatter = SimpleDateFormat("dd-MMM-yyyy")
        val date = formatter.parse(dateInString.replace(" ","-"))
        if (calendarDate.after(date)){

            sendNotifi()
            }

        }


    private fun writeUser(){

        val tempHolder = intent.getStringExtra("DoctorName")
//        Toast.makeText(this, "Enter the firebase${tempHolder.toString()} ", Toast.LENGTH_SHORT).show()
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
        Toast.makeText(this,"Enter getData",Toast.LENGTH_SHORT).show()
        val arraylist=ArrayList<String>()
        val arraylistPro=ArrayList<String>()
        //        val docView=findViewById<RecyclerView>(R.id.Rview)
//        val docView=findViewById<ListView>(R.id.Rview)
        //        val txt=findViewById<TextView>(R.id.txtV)
        //        val name=findViewById<TextView>(R.id.txtName)
        //        val pro=findViewById<TextView>(R.id.txtPro)


        val user= hashMapOf(
            "doctorAppoint" to tempHolder,
            "user" to "user$userNum"



        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("user")?.document( "user"+userNum.toString())?.set(user)?.addOnSuccessListener {


            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add user",Toast.LENGTH_SHORT).show()
            }






//        userNum+=1









    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun readUser() {
        val doctor = FirebaseAuth.getInstance().currentUser
        docDetail = doctor?.uid
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylist = ArrayList<String>()
        val arraylistPro = ArrayList<String>()
        val arraylistUser = ArrayList<String>()
        var arraylistDocName= ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user=" "
        var doc=" "
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
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
        docRef?.whereEqualTo("user",user)?.get()?.addOnSuccessListener {



            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())
//                arraylist.add(document.data.toString())

//                var user=document.get("user").toString()


//                val userGoogle = Firebase.auth.currentUser
//                userGoogle.let {
//                    // Name, email address, and profile photo Url
////                    val name = user.displayName
//                    if (userGoogle != null) {
//                        user = userGoogle.displayName.toString()
//                    }
//
//                    else{
//
//                        user=" NOne"
//                    }
//                    val photoUrl = user.photoUrl
//
//                    // Check if user's email is verified
//                    val emailVerified = user.isEmailVerified
//
//                    // The user's ID, unique to the Firebase project. Do NOT use this value to
//                    // authenticate with your backend server, if you have one. Use
//                    // FirebaseUser.getToken() instead.
//                    val uid = user.uid
//                }
                var docName=document.get("docName").toString()
                if(docName!=null)
                {
                    arraylistDocName.add(docName)

                }



                 doc=document.get("doctorAppoint").toString()
                timeToNoti(doc)
//                var dateInString = "2020-05-02"
//                var simpleFormat =  DateTimeFormatter.ISO_DATE;
//                var convertedDate = LocalDate.parse(dateInString, simpleFormat)
//
//                val calendarDate= Calendar.getInstance().time
//                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//                val currentDate = sdf.format(Date())
//
//
//                if(currentDate>(convertedDate.toString()))
//                {
//                    val txt=findViewById<TextView>(R.id.txtAppoint)
//                    txt.setTextColor(androidx.appcompat.R.color.error_color_material_dark)
//
//                }


                arraylistUser.add(user)
                if(user ==null){
                    arraylist.add("No records found")

                }
                else
                {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")

                    arraylistAppointment.add(AppointmentDetail(user,docName,doc))
                    arrayDel.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                }


//                Toast.makeText(
//                    this,
//                    "Enter the firebase${document.data.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Toast.makeText(
//                    this,
//                    "Enter the firebase id ${document.id.toString()} ",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

//            val spinDocName=findViewById<Spinner>(R.id.spinnerDoc)
//            val arrDocName=ArrayAdapter(this, android.R.layout.simple_list_item_1,arraylistDocName )
//            spinDocName.adapter=arrDocName
//            spinDocName.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    arraylist.add("User: $user\n Appointed Doctor:${arraylistDocName.elementAt(p2)}\n Appointment Detail: $doc\n")
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                    TODO("Not yet implemented")
//                }
//
//
//            }


            val arr=listCustomAdapter(this,arraylistAppointment)




//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
            docView.adapter = arr








//            docView.setOnItemClickListener { adapterView, view, i, l ->
//                val intent= Intent(this,userLogin::class.java)
////                intent.putExtra("DoctorName", tempListViewClickedValue)
//                startActivity(intent)
//
//
//            }




//                val tempListViewClickedValue = arraylist[i].toString()

//                Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()





        }

//        docView.setOnItemClickListener { adapterView, view, i, l ->
//
//
//////           mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")
////            val updates = hashMapOf<String,FieldValue>(
////            "user" to FieldValue.delete(),
////            "doctorAppoint" to FieldValue.delete()
////            )
////
////            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.update(updates as Map<String, Any>)?.addOnCompleteListener {
////
////
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
////
////
////            }
//
////            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.delete()?.addOnSuccessListener {
////
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
////
////            }
//            var user=arraylistUser[i]
//
//            val docRef = mFirebaseDatabaseInstance!!.collection("$user").document("user$userNum")
//
//// Remove the 'capital' field from the document
//            val updates = hashMapOf<String, Any>(
//                "user" to FieldValue.delete(),
//                "doctorAppoint" to FieldValue.delete()
//            )
//
//            docRef.update(updates).addOnCompleteListener {
//
//                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
//
//
//
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//
//            }


        val btn=findViewById<Button>(R.id.btnNext)

        btn.setOnClickListener() {
//            val tempListViewClickedValue = arraylistName[i].toString()+" "+arraylistPro[i].toString()+" " +arraylistTime[i].toString()
            val intent= Intent(this,FilterAppointment::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
            btn.context.startActivity(intent)
//            Toast.makeText(this, "Enter the click listener${i.toString()} ", Toast.LENGTH_SHORT).show()



        }










    }

    private fun deleteUser(){

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val docView=findViewById<ListView>(R.id.listDocAppoint)
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
        docView.setOnItemClickListener { adapterView, view, i, l ->


////           mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")
//            val updates = hashMapOf<String,FieldValue>(
//            "user" to FieldValue.delete(),
//            "doctorAppoint" to FieldValue.delete()
//            )
//
//            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.update(updates as Map<String, Any>)?.addOnCompleteListener {
//
//
//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//
//            }

//            mFirebaseDatabaseInstance?.collection("user")?.document("user$userNum")?.delete()?.addOnSuccessListener {
//
//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
            Toast.makeText(this, "Success ${arrayDel.elementAt(i)}delete the user ", Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Appointment Alert")
            builder.setMessage("Are you sure to cancel this appointment?")


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()



                val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDel.elementAt(i)}")

// Remove the 'capital' field from the document
                val updates = hashMapOf<String, Any>(
                    "user" to FieldValue.delete(),
                    "doctorAppoint" to FieldValue.delete(),
                    "docName" to FieldValue.delete()
                )

                docRef.update(updates).addOnCompleteListener {

                    Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()

                }

                docRef.collection("userAppointment").document("${arrayDel.elementAt(i)}")
                    .delete()
                    .addOnSuccessListener {  Toast.makeText( this,"${arrayDel.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()




            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }


            builder.show()


//            val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDel.elementAt(i)}")
//
//// Remove the 'capital' field from the document
//            val updates = hashMapOf<String, Any>(
//                "user" to FieldValue.delete(),
//                "doctorAppoint" to FieldValue.delete(),
//                "docName" to FieldValue.delete()
//            )
//
//            docRef.update(updates).addOnCompleteListener {
//
//                Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()
//
//            }
//
//            docRef.collection("userAppointment").document("${arrayDel.elementAt(i)}")
//                .delete()
//                .addOnSuccessListener {  Toast.makeText( this,"${arrayDel.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
//                .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }
//
////                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

            readUser()
            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




//        var arraylist = ArrayList<String>()
//        arraylist= arrayListOf(" ")
//        val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist)
//        docView.adapter = arr
    }



    private fun createNoti()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            val name="Hospital Appointmnet"
            val txt="Your Appointment is approaching"
            val imp= NotificationManager.IMPORTANCE_DEFAULT
            val channel= NotificationChannel(CHANNEL_ID,name,imp).apply {
                description=txt
            }

            val notiManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notiManager.createNotificationChannel(channel)


        }

    }

    private fun sendNotifi()
    {
        val builder= NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.appointment)
            .setContentText("Your Appointment is past")
            .setContentTitle("Hospital Appointment")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){

            notify(notificationId,builder.build())
        }

    }



}




