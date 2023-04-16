package com.example.hospitalbooking.DoctorInformationManagement

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.File

class EditDoctorProfile : AppCompatActivity() {
    private lateinit var ImageUri: Uri

    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private val arrayListHos=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor_profile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Update Doctor Profile")

        val selectImgBtn=findViewById<Button>(R.id.btnRet)

        selectImgBtn.setOnClickListener {
            selectImage()
        }

        getDoctorHos()
        setDocPic()
    }

    private fun uploadImage() {
        val firebaseImg= findViewById<ImageView>(R.id.ImgMed)
        val name=findViewById<TextView>(R.id.dtName)
        val userGoogle = Firebase.auth.currentUser
        var dtname=""
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                dtname = userGoogle.displayName.toString()
//                name.text=dtname
            }

        }

        var docName=dtname
//        docName.replace(" ","")

        val letter:Boolean=isLetters(docName)

        if(letter&&docName!=" "&&docName!="")
        {
            val cache= MyCache()
            docName="Dr $docName"
            val progressDialog= ProgressDialog(this)
            progressDialog.setMessage("Uploading File....")
            progressDialog.setCancelable(false)
            progressDialog.show()
            if(progressDialog.isShowing)progressDialog.dismiss()
            val storageReference= FirebaseStorage.getInstance().getReference("Img/$docName.jpg")
            storageReference.putFile(ImageUri).addOnCompleteListener {
                firebaseImg.setImageURI(null)
                if(progressDialog.isShowing)progressDialog.dismiss()
                val imgBitmap= MediaStore.Images.Media.getBitmap(contentResolver, ImageUri)
                cache.saveBitmapToCahche(docName,imgBitmap)
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
            val firebaseImg= findViewById<ImageView>(R.id.ImgMed)
            firebaseImg.setImageURI(ImageUri)
        }
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

    private fun setDocPic() {
        val docName = "Dr " + getGoogleName()

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

            try{
                uploadImage()
            }

            catch (e:UninitializedPropertyAccessException){
                Toast.makeText(this,"You did not attach any photo",Toast.LENGTH_SHORT).show()

            }

            val dtpro=docPro.text
            val pro=dtpro.toString()
            val rateFrequency=0.0F
            val hosTxt=docHospital.text.toString()

            if(hosTxt.isNotEmpty()||hosTxt.isNotBlank()){
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
        }
    }

    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())
    }

    private fun getGoogleName(): String {
        var dtname=""
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                dtname = userGoogle.displayName.toString()

            }
        }
        return dtname
    }

    private fun naviImg(photoUrl: Uri?, loginUser: String) {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val headerImage = headerView.findViewById<ImageView>(R.id.nav_header_image)
        val headerTxtView = headerView.findViewById<TextView>(R.id.nav_header_textView)
        Picasso.get().load(photoUrl).into(headerImage);
        headerTxtView.text=loginUser
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}