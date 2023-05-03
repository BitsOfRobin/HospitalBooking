package com.example.hospitalbooking.DoctorInformationManagement

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.R
import com.google.android.gms.maps.model.LatLng
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
        supportActionBar!!.setTitle("Doctor Profile")

        val docJobText = findViewById<EditText>(R.id.dtPro)
        val docLocationText = findViewById<EditText>(R.id.dtHos)
        val firebaseImg = findViewById<ImageView>(R.id.ImgMed)

        val editProfile = findViewById<Button>(R.id.edit_Btn)
        val updateProfile = findViewById<Button>(R.id.updateBtn)
        val cancelUpdate = findViewById<Button>(R.id.cancelBtn)
        val autoCompleteHospital= findViewById<AutoCompleteTextView>(R.id.autoCurrentHospital)
        val selectImgBtn=findViewById<Button>(R.id.btnRet)
        val navHospital = findViewById<TextView>(R.id.map)
        var errMsgPro = findViewById<TextView>(R.id.errPro)
        var errMsgHosp = findViewById<TextView>(R.id.errHos)

        val userGoogle = Firebase.auth.currentUser
        var dtname=""
        var hospital = ""
        var doctorSpecialist = ""
        userGoogle.let {
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
            }

        }

        editProfile.setOnClickListener {
            // Make text fields editable
            docJobText.isEnabled = true
            autoCompleteHospital.isEnabled = true
            docLocationText.isEnabled = true
            firebaseImg.isEnabled = true

            // Hide the "Edit" button and show the "Update" and "Cancel" buttons
            editProfile.visibility = View.GONE
            updateProfile.visibility = View.VISIBLE
            cancelUpdate.visibility = View.VISIBLE
            selectImgBtn.visibility = View.VISIBLE
            autoCompleteHospital.visibility = View.VISIBLE
            navHospital.visibility = View.GONE
            errMsgHosp.visibility = View.VISIBLE
            errMsgPro.visibility = View.VISIBLE

            firebaseImg.setOnClickListener {
                selectImage()
            }

            selectImgBtn.setOnClickListener {
                selectImage()
            }
        }

        navHospital.setOnClickListener {

            navigateHospitalGM(docLocationText.text.toString())
        }

        cancelUpdate.setOnClickListener {
            val storageReference= FirebaseStorage.getInstance().getReference("Img/$dtname.jpg")
            storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                // Successfully downloaded data to bytes
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                firebaseImg.setImageBitmap(bitmap)
            }.addOnFailureListener {
                // Handle any errors
            }

            val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
            documentRef.whereEqualTo("name", dtname)
                .get().addOnSuccessListener {
                    for(document in it){
                        hospital = document.get("hospital").toString()
                        doctorSpecialist = document.get("pro").toString()
                    }
                    docJobText.setText(doctorSpecialist)
                    docLocationText.setText(hospital)

                    navHospital.setOnClickListener {
                        navigateHospitalGM(hospital)
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
                }

            // Make text fields editable
            docJobText.isEnabled = false
            autoCompleteHospital.isEnabled = false
            docLocationText.isEnabled = false
            firebaseImg.isEnabled = false

            // Remove error message if there are no changes
            errMsgHosp.text = ""
            errMsgPro.text = ""

            // Hide the "Edit" button and show the "Update" and "Cancel" buttons
            editProfile.visibility = View.VISIBLE
            updateProfile.visibility = View.GONE
            cancelUpdate.visibility = View.GONE
            selectImgBtn.visibility = View.GONE
            autoCompleteHospital.visibility = View.GONE
            navHospital.visibility = View.VISIBLE
            errMsgHosp.visibility = View.GONE
            errMsgPro.visibility = View.GONE
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
            if (userGoogle != null) {
                dtname = userGoogle.displayName.toString()
            }
        }

        var docName=dtname
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

    @SuppressLint("SetTextI18n")
    private fun setDocPic() {
        val docName = "Dr " + getGoogleName()

        val cache=MyCache()

        val bitmap=cache.retrieveBitmapFromCache(docName)
        val imgDoct = findViewById<ImageView>(R.id.ImgMed)
        imgDoct.setImageBitmap(bitmap)

        val docInfo=findViewById<TextView>(R.id.dotName)
        docInfo.text=docName

        val docPro=findViewById<EditText>(R.id.dtPro)

        val docHospital=findViewById<EditText>(R.id.dtHos)

        var hospital = ""
        var doctorSpecialist = ""

        // Retrieve User ID and pass to variable display the value
        val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
        documentRef.whereEqualTo("name", docName)
            .get().addOnSuccessListener {
                for(document in it){
                    hospital = document.get("hospital").toString()
                    doctorSpecialist = document.get("pro").toString()
                }
                docPro.setText(doctorSpecialist)
                docHospital.setText(hospital)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }

        val autoCompleteHospital= findViewById<AutoCompleteTextView>(R.id.autoCurrentHospital)
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
                val input = docHospital.text.toString()
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
                Toast.makeText(this,"You maintain old image",Toast.LENGTH_SHORT).show()

            }

            val dtpro=docPro.text
            val pro=dtpro.toString()
            val hosTxt=docHospital.text.toString()

            if(docHospital.text.toString().isNotEmpty()||docHospital.text.toString().isNotBlank()){
                hospital=hosTxt
            }
            val letter:Boolean=isLetters(pro)
            val validaHos:Boolean=isLetters(hospital)

            mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
            val doctorName=docName

            if(!letter||pro.isEmpty()||pro.isBlank()){
                Toast.makeText(this,"Profession contain non alphabet or empty",Toast.LENGTH_LONG).show()
                errTextPro.text = "Profession contain non alphabet or empty"
            }
            else{
                errTextPro.text=" "
            }

            if(!validaHos||docHospital.text.toString().isBlank()||docHospital.text.toString().isEmpty()){
                Toast.makeText(this,"Hospital contain non alphabet or empty",Toast.LENGTH_LONG).show()
//                Toast.makeText(this,"$hosTxt",Toast.LENGTH_LONG).show()
                errTextHos.text="Hospital contain non alphabet or empty"
            }
            else{
                errTextHos.text=" "
            }

            if(letter&&pro!=" "&&pro!="" &&validaHos&&docHospital.text.toString().isNotBlank()&&docHospital.text.toString().isNotEmpty())
            {
                val doc= hashMapOf(
                    "name" to doctorName,
                    "pro" to pro,
                    "hospital" to hospital
                )
                mFirebaseDatabaseInstance?.collection("doctor")?.document(doctorName)?.update(
                    doc as Map<String, Any>
                )?.addOnSuccessListener {
                    Toast.makeText(this,"Successfully edit profile information",Toast.LENGTH_SHORT).show()
                }
                    ?.addOnFailureListener {
                        Toast.makeText(this,"Failed to edit profile information", Toast.LENGTH_SHORT).show()
                    }

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Doctor Profile Updated")
                    .setMessage("Your doctor profile has been updated. Press OK navigate to profile page")
                    .setPositiveButton("OK") { _, _ ->
                        // Navigate to the page
                        val intent = Intent(this, EditDoctorProfile::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .create()
                dialog.show()
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

    fun navigateHospitalGM(hospitalLocation: String){
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(hospitalLocation, 1)
        if (addresses.isNotEmpty()) {
            val latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
            val uri = "geo:${latLng.latitude},${latLng.longitude}?q=${latLng.latitude},${latLng.longitude}($hospitalLocation)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        } else {
            // Handle case where no location is found
            Toast.makeText(this, "No hospital location can found", Toast.LENGTH_SHORT).show()
        }
    }
}