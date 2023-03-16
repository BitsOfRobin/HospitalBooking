package com.example.hospitalbooking.DoctorInformationManagement

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.R
import com.example.hospitalbooking.databinding.ActivityDoctorAppointmentBinding
import com.example.hospitalbooking.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.File

class DoctorInformation : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_information)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Doctor Info")
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
        val docHospital=findViewById<EditText>(R.id.currentHospital)


        val btn=findViewById<Button>(R.id.updateBtn)



        btn.setOnClickListener {

            val dtpro=docPro.text
            val  hospital=docHospital.text.toString()
            val pro=dtpro.toString()
            val rateFrequency=0.0F
//        pro=pro.replace(" ","")
            val letter:Boolean=isLetters(pro)
            val validaHos=isLetters(hospital)
            mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
            val doctorName=docName.toString()
            val errTextPro=findViewById<TextView>(R.id.errPro)
            val errTextHos=findViewById<TextView>(R.id.errHos)
            if(!letter||pro.isEmpty()||pro.isBlank()){

                Toast.makeText(this,"Profession contain non alphabet or empty",Toast.LENGTH_LONG).show()
                errTextPro.text = "Profession contain non alphabet or empty"

            }

            else{

                errTextPro.text=" "
            }

            if(!validaHos||hospital.isBlank()||hospital.isEmpty()){


                Toast.makeText(this,"Hospital contain non alphabet or empty",Toast.LENGTH_LONG).show()
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

                val intent= Intent(this, MainPage::class.java)
                intent.putExtra("DoctorName", docName)
                startActivity(intent)

            }

            else{

                Toast.makeText(this,"Both Inputs consists of NON alphabet",Toast.LENGTH_SHORT).show()

            }

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


}