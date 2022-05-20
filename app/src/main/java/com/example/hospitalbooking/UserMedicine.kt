package com.example.hospitalbooking

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import java.util.ArrayList

class UserMedicine : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userId=" "
    private lateinit var ImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)

//        readMedicine()



        val selectImgBtn=findViewById<Button>(R.id.btnRet)
        val uploadImgBtn=findViewById<Button>(R.id.uploadImageBtn)

        selectImgBtn.setOnClickListener {
            selectImage()

        }

        uploadImgBtn.setOnClickListener {
//
            uploadImage()
        }
    }

    private fun uploadImage() {


        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
//        val name=findViewById<EditText>(R.id.dtName)
//        val dtname=name.text
//        var docName=dtname.toString()
//        docName.replace(" ","")
//
//        val letter:Boolean=true
        var user=" "
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

        regMedicine()


//        if(letter)
//        {
//            docName="Dr $docName"
//            val progressDialog= ProgressDialog(this)
//            progressDialog.setMessage("Uploading File....")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//            val storageReference= FirebaseStorage.getInstance().getReference("medImg/medi.jpg")
//            storageReference.putFile(ImageUri).addOnSuccessListener {
//                firebaseImg.setImageURI(null)
//                if(progressDialog.isShowing)progressDialog.dismiss()
//                Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show()
//                val intent= Intent(this,MedicineViewCustomer::class.java)
////                intent.putExtra("medicineCount", i)
//                startActivity(intent)
//
//
//            }.addOnFailureListener{
//
//                if(progressDialog.isShowing)progressDialog.dismiss()
//            }

//
//        }
//
//        else{
//
//            Toast.makeText(this,"Doctor Name consists NON alphabet",Toast.LENGTH_SHORT).show()
//
//
//        }

    }

    private fun selectImage() {

        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)


    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100&& resultCode== RESULT_OK)
        {
            ImageUri=data?.data!!
            val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
            firebaseImg.setImageURI(ImageUri)

        }


    }

//    private fun readMedicine() {
//
////        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
////        firebaseImg.setImageURI()
//
//        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//        val image: InputImage
//        var elementText=" "
////        image = InputImage.fromFilePath(this,ImageUri)
//        try {
//            image = InputImage.fromFilePath(this,ImageUri)
//            val result = recognizer.process(image)
//                .addOnSuccessListener { visionText ->
//                    // Task completed successfully
//                    // ...
//                }
//                .addOnFailureListener { e ->
//                    // Task failed with an exception
//                    // ...
//                }
//
//            val resultText = result.result
//            for (block in resultText.textBlocks) {
//                val blockText = block.text
//                val blockCornerPoints = block.cornerPoints
//                val blockFrame = block.boundingBox
//                for (line in block.lines) {
//                    val lineText = line.text
//                    val lineCornerPoints = line.cornerPoints
//                    val lineFrame = line.boundingBox
//                    for (element in line.elements) {
//                        elementText = element.text
//                        val elementCornerPoints = element.cornerPoints
//                        val elementFrame = element.boundingBox
//                    }
//                }
//            }
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//
//
//
//
//        val medText=findViewById<TextView>(R.id.medName)
//
//        medText.text=elementText
//
//    }



    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }


    private fun regMedicine() {

//        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
//        firebaseImg.setImageURI()
        val medText=findViewById<TextView>(R.id.medName)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromFilePath(this, ImageUri)
        var mediText=" "
        var resultText=" "
//        image = InputImage.fromFilePath(this,ImageUri)
        try {
//            image = InputImage.fromFilePath(this,ImageUri)
            val result = recognizer.process(image)
            result.addOnSuccessListener {
                // Task completed successfully
                resultText = it.text.replace("\n"," ")

                if(resultText.length>30)
                {
                    mediText=resultText.substring(0,30)
                }

                else{

                    mediText=resultText.substring(0,resultText.length)
                }

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
        mFirebaseDatabaseInstance?.collection("medicine")?.document("$user")?.set(user)?.addOnSuccessListener {


//            Toast.makeText(this,"Successfully added user ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to add user",Toast.LENGTH_SHORT).show()
            }






//        userNum+=1









    }


}