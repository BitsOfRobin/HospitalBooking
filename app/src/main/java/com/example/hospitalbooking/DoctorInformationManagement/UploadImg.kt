package com.example.hospitalbooking.DoctorInformationManagement

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.hospitalbooking.R
import com.google.firebase.storage.FirebaseStorage

class UploadImg : AppCompatActivity() {
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_img)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Upload Image")
        val selectImgBtn=findViewById<Button>(R.id.btnRet)
        val uploadImgBtn=findViewById<Button>(R.id.uploadImageBtn)

        selectImgBtn.setOnClickListener {
            selectImage()

        }

        uploadImgBtn.setOnClickListener {

                    try{
                        uploadImage()
                    }


                    catch (e:UninitializedPropertyAccessException){
                            Toast.makeText(this,"You did not attach any photo",Toast.LENGTH_SHORT).show()

                    }


        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun uploadImage() {

        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
        val name=findViewById<EditText>(R.id.dtName)
        val dtname=name.text
        var docName=dtname.toString()
//        docName.replace(" ","")

        val letter:Boolean=isLetters(docName)

        if(letter&&docName!=" "&&docName!="")
        {
            docName="Dr $docName"
            val progressDialog=ProgressDialog(this)
            progressDialog.setMessage("Uploading File....")
            progressDialog.setCancelable(false)
            progressDialog.show()
            if(progressDialog.isShowing)progressDialog.dismiss()
            val storageReference=FirebaseStorage.getInstance().getReference("Img/$docName.jpg")
            storageReference.putFile(ImageUri).addOnCompleteListener {
                firebaseImg.setImageURI(null)
                if(progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this,"Uploaded",Toast.LENGTH_SHORT).show()
                val intent= Intent(this, DoctorInformation::class.java)
                intent.putExtra("DoctorName", docName)
                startActivity(intent)


            }.addOnFailureListener{

                if(progressDialog.isShowing)progressDialog.dismiss()
            }


        }

        else{

            Toast.makeText(this,"Doctor Name consists NON alphabet",Toast.LENGTH_SHORT).show()


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



    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }
}