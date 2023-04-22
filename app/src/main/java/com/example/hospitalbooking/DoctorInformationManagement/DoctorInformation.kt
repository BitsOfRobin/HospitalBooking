package com.example.hospitalbooking.DoctorInformationManagement

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.BookingAppointment.MainPageViewModel
import com.example.hospitalbooking.BookingAppointment.MainPageViewModelFactory
import com.example.hospitalbooking.R
//import com.example.hospitalbooking.databinding.ActivityDoctorAppointmentBinding
//import com.example.hospitalbooking.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.File

class DoctorInformation : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private val arrayListHos=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_information)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Info")
        getDoctorHos()
        setDocPic()

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    private fun setDocPic() {
        val docName = intent.getStringExtra("DoctorName")

        val fireb = Firebase.storage.reference.child("Img/$docName.jpg")
//            val fireb=FirebaseStorage.getInstance().getReference("/Img")
        val localfile = File.createTempFile("tempImage", "jpg")
        var bitmap: Bitmap
        fireb.getFile(localfile).addOnSuccessListener {
//                 bitmap=BitmapFactory.decodeFile(file.absolutePath)
//                imageArr.add(bitmap)
            bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            val imgDoct = findViewById<ImageView>(R.id.ImgMed)
            imgDoct.setImageBitmap(bitmap)

        }


        val docInfo=findViewById<TextView>(R.id.dotName)
        docInfo.text=docName

        val docPro=findViewById<EditText>(R.id.dtPro)
        val docHospital=findViewById<EditText>(R.id.dtHos)


        val autoCompleteHospital= findViewById<AutoCompleteTextView>(R.id.autoCurrentHospital)
        var hospital=""
        var hospitalTxt=""
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListHos)
        autoCompleteHospital.setAdapter(adapter)
            autoCompleteHospital.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                hospitalTxt = parent.getItemAtPosition(position) as String

                docHospital.setText(hospitalTxt)
                hospital=hospitalTxt
            }

        val errTextPro=findViewById<TextView>(R.id.errPro)
        val errTextHos=findViewById<TextView>(R.id.errHos)
        docPro.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) {
                val input = docPro.text.toString()
                if (input.isNotEmpty()) {
                    // validate input here
                    if (!isLetters(input)) {
                        // clear error message if input is valid
                        errTextPro.text = "Profession contain non alphabet or empty"
                    } else {
                        // set error message if input is invalid
                        errTextPro.text=" "
                    }
                }
            }

        }


        docHospital.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) {
                val input =docHospital.text.toString()
                if (input.isNotEmpty()) {
                    // validate input here
                    if (!isLetters(input)) {
                        // clear error message if input is valid
                        errTextHos.text="Hospital contain non alphabet or empty"
                    } else {
                        // set error message if input is invalid
                        errTextHos.text=" "
                    }
                }
            }


        }




        val btn=findViewById<Button>(R.id.updateBtn)



        btn.setOnClickListener {

            val dtpro=docPro.text
//            val  hospital=docHospital.text.toString()
            val pro=dtpro.toString()
            val rateFrequency=0.0F
//        pro=pro.replace(" ","")

            val hosTxt=docHospital.text.toString()

            if(hosTxt.isNotEmpty()||hosTxt.isNotBlank()){
//            docHospital.setText(hosTxt)
                hospital=hosTxt

            }

            val letter:Boolean=isLetters(pro)
            val validaHos=isLetters(hospital)








            mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
            val doctorName=docName.toString()

            if(!letter||pro.isEmpty()||pro.isBlank()){

                Toast.makeText(this,"Profession contain non alphabet or empty",Toast.LENGTH_LONG).show()
                errTextPro.text = "Profession contain non alphabet or empty"

            }

            else{

                errTextPro.text=" "
            }

            if(!validaHos||hospital.isBlank()||hospital.isEmpty()){


                Toast.makeText(this,"Hospital contain non alphabet or empty",Toast.LENGTH_LONG).show()
//                Toast.makeText(this,"$hosTxt",Toast.LENGTH_LONG).show()
                errTextHos.text="Hospital contain non alphabet or empty"
            }
            else{
                errTextHos.text=" "

            }



            if(letter&&pro!=" "&&pro!="" &&validaHos&&hospital.isNotBlank()&&hospital.isNotEmpty())
            {
                val doc= hashMapOf(

                    "name" to doctorName,
                    "pro" to pro,
                    "rateFrequency" to rateFrequency,
                    "hospital" to hospital



                )
//        val  doc =doctor?.uid

//


                mFirebaseDatabaseInstance?.collection("doctor")?.document( "$doctorName")?.set(doc)?.addOnSuccessListener {


                    Toast.makeText(this,"Successfully added doctor",Toast.LENGTH_SHORT).show()


                }
                    ?.addOnFailureListener {

                        Toast.makeText(this,"Failed to add doctor", Toast.LENGTH_SHORT).show()
                    }

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Doctor Profile Created")
                    .setMessage("Your doctor profile has been created. Press OK navigate to homepage")
                    .setPositiveButton("OK") { _, _ ->
                        // Navigate to the page
                        val intent= Intent(this, MainPage::class.java)
                        intent.putExtra("DoctorName", docName)
                        startActivity(intent)
                        finish()
                    }
                    .create()
                dialog.show()


            }

//            else{
//
//                Toast.makeText(this,"Both Inputs consists of NON alphabet",Toast.LENGTH_SHORT).show()
//
//            }

        }


    }



    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }


    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser




    }

    private fun getDoctorHos(){


        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()


        val docRef = mFirebaseDatabaseInstance?.collection("doctor")
        docRef?.get()?.addOnSuccessListener {
           arrayListHos.clear()


            for(document in it){


                val hospital = document.get("hospital").toString()
                arrayListHos.add(hospital)

            }

        }
            ?.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }


    }






}