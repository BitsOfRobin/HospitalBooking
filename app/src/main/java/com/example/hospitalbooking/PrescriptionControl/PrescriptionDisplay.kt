package com.example.hospitalbooking.PrescriptionControl

import android.content.ContentValues
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
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.*
import com.example.hospitalbooking.Adapter.ListCustomAdapterForPrescription
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.KotlinClass.AppointmentDetail
import com.example.hospitalbooking.KotlinClass.Prescription
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PrescriptionDisplay : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private lateinit var toggle:ActionBarDrawerToggle
    private var arraylistDocName = ArrayList<String>()
    private var arraylistDocNameForSearch = ArrayList<String>()
    private var arraylistUser=ArrayList<String>()
    private var arraylistMedi=ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_display)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Prescription")

        arraylistMedi.clear()
        arraylistDocNameForSearch.clear()
        arraylistUser.clear()
        var userG = ""
        val userGoogle = Firebase.auth.currentUser
        var email=""
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                userG = userGoogle.displayName.toString()
                email=userGoogle.email.toString()
                naviImg(userGoogle!!.photoUrl,userG)

            }

            else {
//
                userG = " NOne"
            }

        }

        if(email.contains("@student.tar")){

            showAllPresNurse()
        }

        else{
            showPrescription()


        }









        showNavBar()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPrescription() {
        val searchUser=findViewById<SearchView>(R.id.searchUser)
        searchUser.visibility=View.GONE
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylistPres = ArrayList<Prescription>()
        val arraylistPro = ArrayList<String>()
//        val arraylistUser = ArrayList<String>()

        val arrayForSearch = ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = " "
        var userG = " "
        var medicine1 = " "
        var medicine2 = " "
        var dos1=" "
        var dos2=" "
        var pricePerdos1=" "
        var pricePerdos2=" "
        var totalPrice=" "
        var paymentStatus=" "


//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.presListCheck)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                userG = userGoogle.displayName.toString()


            }

            else {
//
                userG = " NOne"
            }

        }

//        var userName = intent.getStringExtra("userName").toString()
//
//        Toast.makeText(this, userName,Toast.LENGTH_SHORT).show()
//        if(userName.contains("     ")){
//
//            user=userG
//        }
//
//        else{
//
//            user=userName
//        }
        user=userG
        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.whereEqualTo("user",user)?.get()?.addOnSuccessListener {


            var docName = it.documents

    //                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName != " " && docName != "") {
                    arraylistDocName.add(docName)

                }



                medicine1 = document.get("medicine1").toString()
                medicine2 = document.get("medicine2").toString()

                dos1 = document.get("dosage1").toString()
                dos2 = document.get("dosage2").toString()
                pricePerdos1= document.get("priceMed1").toString()
                pricePerdos2= document.get("priceMed2").toString()
                totalPrice= document.get("totalPriceMed").toString()


                user = document.get("user").toString()
                paymentStatus= document.get("paymentStatus").toString()





//                arraylistUser.add(user)

    //                dos1.toColorInt()
    //                if (user == null) {
    //                    arraylist.add("No records found")
    //
    //                } else {

                if (docName.contains("Dr")&&medicine1!="null"&&paymentStatus=="unpaid") {
    //                    arraylistPres.add("User:$user\nAppointed Doctor:$docName\n Medicine Detail:$medicine1 dosage=$dos1 ,$medicine2 dosage=$dos2\n\n")
                    val medi = "$medicine1\n$dos1 mg \n\n" +
                            "RM $pricePerdos1 \n\n" +
                            "$medicine2\n$dos2 mg\n\n"+
                            "RM $pricePerdos2 \n\n" +
                                    "Total= RM $totalPrice \n\n"
                    arraylistPres.add(Prescription(user, docName, medi, 0F))
//                    arraylistMedi.add(medi)


                }


    //                }


            }



            val arr = ListCustomAdapterForPrescription(this,arraylistPres)




            docView.adapter = arr
//            searchUser(arr)
        }?.addOnFailureListener{

                Toast.makeText(this,"Fail to retrieve data",Toast.LENGTH_SHORT).show()
        }



//            var rating=0.0
            docView.setOnItemClickListener { adapterView, view2, i, l ->

                val builder = AlertDialog.Builder(this)

                val inflater = layoutInflater
                val view:View = inflater.inflate(R.layout.rating_dialog, null)

                val ratingBar = view.findViewById<RatingBar>(R.id.ratingBarInput)

                builder.setView(view)

                builder.setPositiveButton("Submit") { dialog, which ->
                    val rating = ratingBar.rating
                    calStar(rating,arraylistDocName[i])
//                    getRate(arraylistDocName[i],rating)
                }

                builder.setNegativeButton("Cancel") { dialog, which ->

                    Toast.makeText(this,"Thanks",Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()

//                ratingDialog(i)





//                val intent= Intent(this,PopUpWindow::class.java)
//                intent.putExtra("docName", arraylistDocName[i])
//                intent.putExtra("rating",rating)

//                val extras = Bundle()
//                extras.putDouble("rating",rating)

//                startActivity(intent)

            }




    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun ratingDialog(i: Int) {

        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.rating_dialog, null)

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBarInput)

        builder.setView(view)

        builder.setPositiveButton("Submit") { dialog, which ->
            val rating = ratingBar.rating
            val dialog = builder.create()
            dialog.show()
            getRate(arraylistDocName[i],rating)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->

            Toast.makeText(this,"Thanks",Toast.LENGTH_SHORT).show()
        }






    }
//    private fun setColorText(str: String): SpannableString {
//
//        val docPro=findViewById<TextView>(R.id.docPro)
//
//
//        val yellow = ForegroundColorSpan(Color.YELLOW)
//        val spannableString = SpannableString(str)
//
//
//
//        spannableString.setSpan(yellow,
//            0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
////        val num=arraylistPro[position].length
//
//        Toast.makeText(this,"$spannableString",Toast.LENGTH_SHORT).show()
//
//        return spannableString
////        docPro.text = spannableString.toString()
//
//    }



//
//    private fun getRate( i:Int){
//
//        val rating=findViewById<RatingBar>(R.id.ratingBarInput)
//
//
//        val docList = findViewById<ListView>(R.id.presListCheck)
//        var rate:Float= 0F
//
//
//
//
//            rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
//
//                ratingBar.rating=fl
//                rate=fl
//
//
//            }
//
//
//
//        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//
//
//
//
//
//
//
//            val ratingStar= hashMapOf(
//                "ratingStart" to rate,
//
//
//
//
//            )
////        val  doc =doctor?.uid
//
////
//            mFirebaseDatabaseInstance?.collection("doctor")?.document(arraylistDocName[i])?.set(ratingStar)
//                ?.addOnSuccessListener {
//
//
//            Toast.makeText(this,"Successfully rate  doctor ",Toast.LENGTH_SHORT).show()
//
//                }
//                ?.addOnFailureListener {
//
//                    Toast.makeText(this, "Failed to rate doctor", Toast.LENGTH_SHORT).show()
//                }
//
//
////        userNum+=1
//
//
//
//
//
//    }


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


    private fun showAllPresNurse() {

        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
//        Toast.makeText(this, "Enter read user", Toast.LENGTH_SHORT).show()
        val arraylistPres = ArrayList<Prescription>()
        val arraylistPro = ArrayList<String>()
//        val arraylistUser = ArrayList<String>()

        val arrayForSearch = ArrayList<String>()
        val arraylistAppointment = ArrayList<AppointmentDetail>()
        var user = ""
        var userG = ""
        var medicine1 = ""
        var medicine2 = ""
        var dos1=""
        var dos2=""
        var pricePerdos1=" "
        var pricePerdos2=" "
        var totalPrice=" "

//        val docView=findViewById<RecyclerView>(R.id.Rview)
        val docView = findViewById<ListView>(R.id.presListCheck)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                userG = userGoogle.displayName.toString()


            }

            else {
//
                userG = " NOne"
            }

        }

//        var userName = intent.getStringExtra("userName").toString()
//
//        Toast.makeText(this, userName,Toast.LENGTH_SHORT).show()
//        if(userName.contains("     ")){
//
//            user=userG
//        }
//
//        else{
//
//            user=userName
//        }

        val docRef = mFirebaseDatabaseInstance?.collection("userAppointment")
        docRef?.get()?.addOnSuccessListener {


            var docName = it.documents

//                }

            for (document in it) {
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                arraylistPro.add(document.id.toString())

                var docName = document.get("docName").toString()
                if (docName.isNotBlank() && docName.isNotEmpty()&&docName.contains("Dr")) {
                    arraylistDocNameForSearch.add(docName)

                }



                medicine1 = document.get("medicine1").toString()
                medicine2 = document.get("medicine2").toString()

                dos1 = document.get("dosage1").toString()
                dos2 = document.get("dosage2").toString()

                user = document.get("user").toString()
                pricePerdos1= document.get("priceMed1").toString()
                pricePerdos2= document.get("priceMed2").toString()
                totalPrice= document.get("totalPriceMed").toString()



                if ( user.isNotBlank() &&  user.isNotEmpty()&&user!="null") {
                    arraylistUser.add(user)
                }




//                dos1.toColorInt()
//                if (user == null) {
//                    arraylist.add("No records found")
//
//                } else {

                if (docName.contains("Dr")) {
//                    arraylistPres.add("User:$user\nAppointed Doctor:$docName\n Medicine Detail:$medicine1 dosage=$dos1 ,$medicine2 dosage=$dos2\n\n")
//                    val medi = "$medicine1\n$dos1 mg \n\n$medicine2\n$dos2 mg\n\n"


                    val medi = "$medicine1\n$dos1 mg \n\n" +
                            "RM $pricePerdos1 \n\n" +
                            "$medicine2\n$dos2 mg\n\n"+
                            "RM $pricePerdos2 \n\n" +
                            "Total= RM $totalPrice \n\n"


                    arraylistPres.add(Prescription(user, docName, medi, 0F))
                    arraylistMedi.add(medi)
//                    Toast.makeText(this,"$medi",Toast.LENGTH_SHORT).show()
                }


//                }


            }
             val arr = ListCustomAdapterForPrescription(this,arraylistPres)



//            Toast.makeText(this,"$arraylistUser",Toast.LENGTH_SHORT).show()
            docView.adapter = arr
            searchUser(arr)

        }?.addOnFailureListener{

            Toast.makeText(this,"Fail to retrieve data",Toast.LENGTH_SHORT).show()
        }


//            var rating=0.0
//            docView.setOnItemClickListener { adapterView, view, i, l ->
//
//
//
//
//
//                val intent= Intent(this,PopUpWindow::class.java)
//                intent.putExtra("docName", arraylistDocName[i])
////                intent.putExtra("rating",rating)
//
////                val extras = Bundle()
////                extras.putDouble("rating",rating)
//
//                startActivity(intent)
//
//            }



        }



    private fun searchUser(arr: ListCustomAdapterForPrescription) {
        val tempList=ArrayList<Prescription>()
        val searchView=findViewById<SearchView>(R.id.searchUser)
        searchView.queryHint="search User"
        val docView = findViewById<ListView>(R.id.presListCheck)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {



                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                tempList.clear()
                if (p0 != null) {
                    if(p0.isNotEmpty()&&p0.isNotBlank()) {
                        for(i in arraylistUser.indices)
                        {
                            if(arraylistUser[i].contains(p0,true))
                            {
                                tempList.add(Prescription(arraylistUser[i],arraylistDocNameForSearch[i],arraylistMedi[i],0F))

                            }

                        }


                        dataChanged(tempList)



                    }


                    else
                    {
                        docView.adapter = arr

                    }
                }


                return false
            }



        })


//        Toast.makeText(this,"$arraylistDocNameForSearch",Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"$arraylistMedi",Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"$arraylistUser",Toast.LENGTH_SHORT).show()


    }


    private  fun dataChanged(arraylist:ArrayList<Prescription>)
    {

        val docView = findViewById<ListView>(R.id.presListCheck)
        val arr = ListCustomAdapterForPrescription(this, arraylist)
//        Toast.makeText(this,"$arraylist",Toast.LENGTH_SHORT).show()

        docView.adapter = arr
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun getRate(docName:String,rate:Float){
//        val docName = intent.getStringExtra("docName")
        val rating=findViewById<RatingBar>(R.id.ratingBarInput)

        val extras = intent.extras
//        rateStar = extras!!.getDouble("rating")

        val docList = findViewById<ListView>(R.id.presListCheck)




//        rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
//
//            ratingBar.rating=fl
//            rate= ratingBar.numStars.toFloat()
//
//
//        }
//        rate=   rating.rating
        Toast.makeText(this,"$rate",Toast.LENGTH_SHORT).show()
//        val calRating=calStar(docName.toString(),rate,docName.toString())
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()







        val ratingStar= hashMapOf(
            "ratingStar" to rate.toInt(),
            "name" to docName




        )
//        val  doc =doctor?.uid

//
//        mFirebaseDatabaseInstance?.collection("doctor")?.document(docName.toString())?.update("rating",rate)
//            ?.addOnSuccessListener {
//
//
//                Toast.makeText(this,"Successfully rate  doctor ", Toast.LENGTH_SHORT).show()
//
//            }
//            ?.addOnFailureListener {
//
//                Toast.makeText(this, "Failed to rate doctor", Toast.LENGTH_SHORT).show()
//            }


//        userNum+=1






    }

    private fun calStar( rate: Float,docName:String) {

        var rateStar= 0.0
        var rateFrequency= 0.0
        mFirebaseDatabaseInstance?.collection("doctor")?.whereEqualTo("name", docName)?.get()
            ?.addOnSuccessListener {


                for (document in it) {

                    rateStar = document.get("rating") as Double
                    rateFrequency = document.get("rateFrequency") as Double



                }


            } ?.addOnFailureListener {

                Toast.makeText(this, "Failed to retrieve rate frequency", Toast.LENGTH_SHORT).show()
            }
        var rateFrequencyUpdate=rateFrequency.toInt()
        rateFrequency++

        rateFrequency=rateFrequencyUpdate.toDouble()
        val res=rateStar + rate




        updateRating(docName,res,rateFrequency)





    }


    private fun updateRating(docName:String,res:Double,rateFrequency:Double){

        val ratingUpdate= hashMapOf(
            "rating" to res,

            "rateFrequency" to rateFrequency




        )

        val docRefUpdateRating=   mFirebaseDatabaseInstance?.collection("doctor")?.document(docName)


        docRefUpdateRating?.update(ratingUpdate as Map<String, Any>)
            ?.addOnSuccessListener {


                Toast.makeText(this,"Successfully update rating ", Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener {

                Toast.makeText(this, "Failed to update rating", Toast.LENGTH_SHORT).show()
            }

//        1*a+2*b+3*c+4*d+5*e/(5*#R)


        Toast.makeText(this,"total rating is $res",Toast.LENGTH_SHORT).show()
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