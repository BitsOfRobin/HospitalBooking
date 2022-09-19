package com.example.hospitalbooking

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException
import java.util.ArrayList

class MedicineViewCustomer : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userNum:Int=0
    private var docDetail:String?=null
    private lateinit var ImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_view_customer)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Patient Medicine Info")
        readMedi()


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




}