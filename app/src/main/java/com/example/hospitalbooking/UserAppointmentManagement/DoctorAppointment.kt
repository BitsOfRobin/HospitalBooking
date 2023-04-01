package com.example.hospitalbooking.UserAppointmentManagement



//import com.example.hospitalsmartt.databinding.ActivityMainBinding
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hospitalbooking.*
import com.example.hospitalbooking.Adapter.listCustomAdapter
import com.example.hospitalbooking.BookingAppointment.Feedback
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.KotlinClass.AppointmentDetail
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class DoctorAppointment : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private lateinit var toggle:ActionBarDrawerToggle
    private var userNum:Int=0
    private var docDetail:String?=null
    private var arrayDel = ArrayList<String>()
    private var arrayDelPast = ArrayList<String>()
    private val CHANNEL_ID="channel_id_example_01"
    private  val notificationId=101
    private val arraylistAppointment = ArrayList<AppointmentDetail>()
    private val arraylistPastAppointment = ArrayList<AppointmentDetail>()


    //    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointment)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("View Appointment")

        showNavBar()
//        writeUser()
        readUser()
//        deleteUser()
//        deleteUserPast()

        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
        docView.setOnScrollListener(object :  AbsListView.OnScrollListener {
            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {

            }

            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
                val topRowVerticalPosition =
                    if (p0 == null || p0.getChildCount() === 0) 0 else p0.getChildAt(
                        0
                    ).getTop()
                swipe.isEnabled = topRowVerticalPosition >= 0
            }

        }
            
        )




        refresh()
        createNoti()





    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun timeToNoti(time:String)
    {


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






        if (calendarDate.before(date)){

            sendNotifi(properDate)
            }

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

        var user=" "
        var doc=" "
        arraylistPastAppointment.clear()
        arraylistAppointment.clear()
        arrayDelPast.clear()
        arrayDel.clear()
//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.listDocAppoint)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                naviImg(userGoogle!!.photoUrl,user)
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


                var docName=document.get("docName").toString()
                if(docName!=null)
                {
                    arraylistDocName.add(docName)

                }



                 doc=document.get("doctorAppoint").toString()

                if(doc!=" "){


                    timeToNoti(doc)
                }


                arraylistUser.add(user)
                if(user ==null){
                    arraylist.add("No records found")

                }
                else
                {
                    arraylist.add("User:$user\nAppointed Doctor:$docName\nAppointment Detail:$doc\n\n")

                    var dateInString=doc.replace(" ", "-")



                    val calendarDate = Calendar.getInstance().time
                    val detect=dateInString.indexOf(",")
                    val sub1=dateInString.substring(0,detect)
                    val sub2=dateInString.substring(detect+2,dateInString.length)
                     dateInString= "$sub1,$sub2"




                    val formatter = SimpleDateFormat("dd-MMM-yyyy,HH:mm:ss")
                    val date = formatter.parse(dateInString)









                    if (calendarDate.before(date)){

                        arraylistAppointment.add(AppointmentDetail(user,docName,doc))
                        arrayDel.add("{docName=$docName, doctorAppoint=$doc, user=$user}")
                    }

                    else{

                        arraylistPastAppointment.add(AppointmentDetail(user,docName,doc))
                        arrayDelPast.add("{docName=$docName, doctorAppoint=$doc, user=$user}")

                    }












                }



            }




            val arr= listCustomAdapter(this,arraylistAppointment)
            val arrPast= listCustomAdapter(this,arraylistPastAppointment)





            val linearCurrent=findViewById<LinearLayout>(R.id.currentList)
            val linearPast=findViewById<LinearLayout>(R.id.PastList)
//            linearCurrent.visibility =View.GONE

            val tabCurrent=findViewById<TabLayout>(R.id.currentAppoint)
            val tabLayout=findViewById<TabLayout>(R.id.appointmentLayout)

            val docViewPast=findViewById<ListView>(R.id.listPastAppoint)

            val tabPast=findViewById<TabLayout>(R.id.PastAppoint)

            tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        if(tab.position==0){
                            linearCurrent.visibility =View.GONE
//                            arr.notifyDataSetChanged()
                            docView.adapter = arr

                            linearCurrent.visibility =View.VISIBLE
//                            linearCurrent.visibility =View.GONE
                            deleteUser()
//                            arr.notifyDataSetChanged()
//                            docView.adapter = arr
//                            linearCurrent.visibility =View.VISIBLE
//


                        }
                        else if(tab.position==1){
                            linearCurrent.visibility =View.GONE
//                            arrPast.notifyDataSetChanged()
                            docView.adapter=arrPast

                            linearCurrent.visibility =View.VISIBLE
                            deleteUserPast()
//



                        }

                    }


                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                    linearCurrent.visibility =View.GONE
//                    arr.notifyDataSetChanged()
                    docView.adapter = arr

                    linearCurrent.visibility =View.VISIBLE
//                    linearCurrent.visibility =View.GONE
                    deleteUser()
//                    arr.notifyDataSetChanged()
//                    docView.adapter = arr
//                    linearCurrent.visibility =View.VISIBLE






                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        if(tab.position==0){
                            linearCurrent.visibility =View.GONE
//                            arr.notifyDataSetChanged()
                            docView.adapter = arr

                            linearCurrent.visibility =View.VISIBLE
//                            linearCurrent.visibility =View.GONE
                            deleteUser()
//                            arr.notifyDataSetChanged()
//                            docView.adapter = arr
//                            linearCurrent.visibility =View.VISIBLE


                        }
                        else if(tab.position==1){
                            linearCurrent.visibility =View.GONE
//                            arrPast.notifyDataSetChanged()
                            docView.adapter=arrPast
                            linearCurrent.visibility =View.VISIBLE
                            deleteUserPast()
//                            linearCurrent.visibility =View.GONE
//                            arrPast.notifyDataSetChanged()
//                            docView.adapter=arrPast
//                            linearCurrent.visibility =View.VISIBLE

                            docView.onItemLongClickListener =
                                AdapterView.OnItemLongClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->


                                    toComment(i)
                                    true
                            }


                        }

                    }

                }


            } )










        }











    }


    private fun toComment(i:Int){


        val intent = Intent(this, Feedback::class.java)
        intent.putExtra("DoctorName",arraylistPastAppointment.get(i).docName)
        intent.putExtra("Appointment", arraylistPastAppointment.get(i).AppointmentDetail)
        intent.putExtra("userName", arraylistPastAppointment.get(i).userName)

        startActivity(intent)





    }



    @RequiresApi(Build.VERSION_CODES.O)
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



//            Toast.makeText(this, "Success ${arrayDel.elementAt(i)}delete the user ", Toast.LENGTH_SHORT).show()

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

                docRef
                    .delete()
                    .addOnSuccessListener {  Toast.makeText( this,"${arrayDel.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()


                arraylistAppointment.removeAt(i)
                val arr= listCustomAdapter(this,arraylistAppointment)
                arr.notifyDataSetChanged()
                docView.adapter=arr
//                refreshAppoint()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }


            builder.show()





        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteUserPast(){

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



            Toast.makeText(this, "Success ${arrayDelPast.elementAt(i)}delete the user ", Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Appointment Alert")
            builder.setMessage("Are you sure to cancel this appointment?")


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()



                val docRef = mFirebaseDatabaseInstance!!.collection("userAppointment").document("${arrayDelPast.elementAt(i)}")

// Remove the 'capital' field from the document
                val updates = hashMapOf<String, Any>(
                    "user" to FieldValue.delete(),
                    "doctorAppoint" to FieldValue.delete(),
                    "docName" to FieldValue.delete()
                )

                docRef.update(updates).addOnCompleteListener {

                    Toast.makeText(this, "Success delete the user ", Toast.LENGTH_SHORT).show()

                }

                docRef
                    .delete()
                    .addOnSuccessListener {  Toast.makeText( this,"${arrayDelPast.elementAt(i)} successfully deleted!",Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener {  Toast.makeText( this,"Error deleting document",Toast.LENGTH_SHORT).show() }

//                Toast.makeText(this, "Succes delete the user ", Toast.LENGTH_SHORT).show()
                arraylistPastAppointment.removeAt(i)
                val arrPast= listCustomAdapter(this,arraylistPastAppointment)
                arrPast.notifyDataSetChanged()
                docView.adapter=arrPast
//                refreshAppoint()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }


            builder.show()




        }





    }





    @RequiresApi(Build.VERSION_CODES.O)
    private fun refresh()
    {
        val swipe=findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        swipe.setOnRefreshListener {

            val linearCurrent=findViewById<LinearLayout>(R.id.currentList)
            linearCurrent.visibility=View.GONE

            readUser()


            Toast.makeText(this, "Page is refreshed ", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false

        }




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

    private fun sendNotifi(date:String)
    {
        val builder= NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.appointment)
            .setContentText("Appointment: $date")
            .setContentTitle("Hospital Appointment")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){

            notify(notificationId,builder.build())
        }

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

}




