package com.example.hospitalbooking

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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


        val btn=findViewById<Button>(R.id.updateBtn)



        btn.setOnClickListener {

            val dtpro=docPro.text
            val pro=dtpro.toString()
            val rateFrequency=0.0
//        pro=pro.replace(" ","")
            val letter:Boolean=isLetters(pro)
            mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
            val doctorName=docName.toString()
            if(letter&&pro!=" "&&pro!="")
            {
                val doc= hashMapOf(

                    "name" to doctorName,
                    "pro" to pro,
                    "rateFrequency" to rateFrequency



                )
//        val  doc =doctor?.uid

//


                mFirebaseDatabaseInstance?.collection("doctor")?.document( "$doctorName")?.set(doc)?.addOnSuccessListener {


                    Toast.makeText(this,"Successfully added doctor",Toast.LENGTH_SHORT).show()


                }
                    ?.addOnFailureListener {

                        Toast.makeText(this,"Failed to add doctor", Toast.LENGTH_SHORT).show()
                    }

                val intent= Intent(this,MainPage::class.java)
                intent.putExtra("DoctorName", docName)
                startActivity(intent)

            }

            else{

                Toast.makeText(this,"Professional consists of NON alphabet",Toast.LENGTH_SHORT).show()

            }

        }


    }



    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }





}