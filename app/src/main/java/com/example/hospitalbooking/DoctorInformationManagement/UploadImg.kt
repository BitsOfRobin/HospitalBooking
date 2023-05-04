package com.example.hospitalbooking.DoctorInformationManagement

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.AdminManagementOnAppointment.DoctorViewAppointment
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class UploadImg : AppCompatActivity() {
    private lateinit var ImageUri: Uri
    private lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_img)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Upload Doctor Image")
        val selectImgBtn=findViewById<Button>(R.id.btnRet)
        val uploadImgBtn=findViewById<Button>(R.id.uploadImageBtn)
        val image = findViewById<ImageView>(R.id.firebaseImage)
        var dtname=""

        showNavBar()
        val name=findViewById<TextView>(R.id.dtName)
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                dtname = userGoogle.displayName.toString()
                val photoUrl=userGoogle.photoUrl
                naviImg(photoUrl,dtname)
                name.text=dtname
            }

        }
        selectImgBtn.setOnClickListener {
            selectImage()

        }

        image.setOnClickListener {
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
        val name=findViewById<TextView>(R.id.dtName)
        val userGoogle = Firebase.auth.currentUser
        var dtname=""
        userGoogle.let {
                // Name, email address, and profile photo Url
//                    val name = user.displayName
                if (userGoogle != null) {
                    dtname = userGoogle.displayName.toString()
                    name.text=dtname
                }

            }

        var docName=dtname
//        docName.replace(" ","")

        val letter:Boolean=isLetters(docName)

        if(letter&&docName!=" "&&docName!="")
        {
            val cache=MyCache()
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
                val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
                firebaseImg.setImageURI(ImageUri)

            }


    }



    private fun isLetters(string: String): Boolean {
        return string.matches("^[a-zA-Z ]*$".toRegex())

//        return string.none { it !in 'A'..'Z' && it !in 'a'..'z' }
    }






    private fun showNavBar(){


        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view=findViewById<NavigationView>(R.id.nav_view)
        toggle= ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }

                R.id.nav_profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }

                R.id.nav_Delete_doctor -> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }


                R.id.nav_edit_docProfile -> {
                    val intent = Intent(this, EditDoctorProfile::class.java)
                    startActivity(intent)

                }



                R.id.nav_view_report -> {
                    val intent = Intent(this, DoctorSummarizeReport::class.java)
                    startActivity(intent)

                }

                R.id.nav_view_schedule -> {
                    val intent = Intent(this, DoctorViewAppointment::class.java)
                    startActivity(intent)

                }
                R.id.nav_OCR -> {
                    val intent = Intent(this, UserMedicine::class.java)
                    startActivity(intent)
                }

                R.id.nav_medicineRecord -> {
                    val intent = Intent(this, MedicineRecord::class.java)
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