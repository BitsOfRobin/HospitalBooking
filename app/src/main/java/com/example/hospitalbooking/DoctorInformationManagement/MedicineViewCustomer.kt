package com.example.hospitalbooking.DoctorInformationManagement

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MedicineViewCustomer : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userNum:Int=0
    private var docDetail:String?=null
    private lateinit var ImageUri: Uri
    private lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_view_customer)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Patient Medicine Info")
        readMedi()
        showNavBar()

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun readMedi() {

        var loginUser=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.displayName.toString()
                naviImg(userGoogle!!.photoUrl,loginUser)

            }

            else{

                loginUser=" NOne"
            }
//
        }
//        var i = intent.getStringExtra("medicineCount")
        val btn=findViewById<Button>(R.id.updateBtn)
        btn.setOnClickListener {


        val fireb = Firebase.storage.reference.child("medImg/medi.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
        val localfile = File.createTempFile("tempImage", "jpg")
        var bitmap: Bitmap
        fireb.getFile(localfile).addOnSuccessListener {
//                 bitmap=BitmapFactory.decodeFile(file.absolutePath)
//                imageArr.add(bitmap)
            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            val imgDoct = findViewById<ImageView>(R.id.ImgMed)
            imgDoct.setImageBitmap(bitmap)

            Toast.makeText(this,"Success to retrieve",Toast.LENGTH_SHORT).show()
            regMedicine(bitmap)


        }
            .addOnFailureListener{

                Toast.makeText(this,"Failed to retrieve",Toast.LENGTH_SHORT).show()
            }

        }



    }

    private fun regMedicine(bitmap: Bitmap) {

//        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
//        firebaseImg.setImageURI()
        val medText=findViewById<TextView>(R.id.dotName)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)
        var mediText=" "
        var resultText=" "
//        image = InputImage.fromFilePath(this,ImageUri)
        try {
//            image = InputImage.fromFilePath(this,ImageUri)
                val result = recognizer.process(image)
                    result.addOnSuccessListener {
                // Task completed successfully
                    resultText = it.text.replace("\n"," ")

                    mediText=resultText.substring(0,30)
                    Toast.makeText(this, resultText,Toast.LENGTH_SHORT).show()



                    medText.text=resultText
                    writeMedi(mediText)
//                    for (block in it.textBlocks) {
//                        val blockText = block.text
//                        val blockCornerPoints = block.cornerPoints
//                        val blockFrame = block.boundingBox
//                        for (line in block.lines) {
////                            lineText = line.text
//                            val lineCornerPoints = line.cornerPoints
//                            val lineFrame = line.boundingBox
//                            for (element in line.elements) {
//                                elementText = element.text
//                                val elementCornerPoints = element.cornerPoints
//                                val elementFrame = element.boundingBox
//                            }
//                        }
//                    }
                    // ...
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }



        } catch (e: IOException) {
            e.printStackTrace()
        }






    }

    private fun writeMedi(mediText:String){


        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val arraylist= ArrayList<String>()
        val arraylistPro= ArrayList<String>()

        var loginUser=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                loginUser = userGoogle.displayName.toString()
            }

            else{

                loginUser=" NOne"
            }
//
        }
//        val loginUser=readUser()
        val user= hashMapOf(
            "userMedicine" to mediText,
            "user" to loginUser,




        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("medicine")?.document( "$user")?.set(user)?.addOnSuccessListener {


//            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add user",Toast.LENGTH_SHORT).show()
            }






//        userNum+=1









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