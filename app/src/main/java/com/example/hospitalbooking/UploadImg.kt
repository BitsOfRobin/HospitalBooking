package com.example.hospitalbooking

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class UploadImg : AppCompatActivity() {
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_img)

        val selectImgBtn=findViewById<Button>(R.id.selectImageBtn)
        val uploadImgBtn=findViewById<Button>(R.id.uploadImageBtn)

        selectImgBtn.setOnClickListener {
            selectImage()

        }

        uploadImgBtn.setOnClickListener {

            uploadImage()
        }



    }

    private fun uploadImage() {
       val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Uploading File....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
        val name=findViewById<EditText>(R.id.dtName)
        val dtname=name.text
        var docName="Mr $dtname"
        val storageReference=FirebaseStorage.getInstance().getReference("Img/$docName.jpg")
        storageReference.putFile(ImageUri).addOnSuccessListener {
            firebaseImg.setImageURI(null)
            if(progressDialog.isShowing)progressDialog.dismiss()
            Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show()
            val intent= Intent(this,DoctorInformation::class.java)
            intent.putExtra("DoctorName", docName)
            startActivity(intent)


        }.addOnFailureListener{

            if(progressDialog.isShowing)progressDialog.dismiss()
        }

    }

    private fun selectImage() {

        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if(requestCode==100&& resultCode== RESULT_OK)
            {
                ImageUri=data?.data!!
                val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
                firebaseImg.setImageURI(ImageUri)

            }


    }
}