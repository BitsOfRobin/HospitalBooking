package com.example.hospitalbooking.MedicineOCR

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.GoogleLogInForAdminAndUser.Profile
import com.example.hospitalbooking.MainActivity
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.R
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class UserMedicine : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private var userId=" "
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var ImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_medicine)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Patient Medicine Recognize")

        showNavBar()

        val selectImgBtn=findViewById<Button>(R.id.btnRet)
        val uploadImgBtn=findViewById<Button>(R.id.uploadImageBtn)
        val image = findViewById<ImageView>(R.id.firebaseImage)

        selectImgBtn.setOnClickListener {
            selectImage()

        }

        image.setOnClickListener {
            selectImage()

        }

        var user=" "
        val userGoogle = Firebase.auth.currentUser
        userGoogle.let {
            // Name, email address, and profile photo Url
//                    val name = user.displayName
            if (userGoogle != null) {
                user = userGoogle.displayName.toString()
                naviImg(userGoogle!!.photoUrl,user)
            } else {

                user = " NOne"
            }

        }


        uploadImgBtn.setOnClickListener {
//


            try{
                uploadImage()
            }


            catch (e:UninitializedPropertyAccessException){
                Toast.makeText(this,"You did not attach any photo for medicine OCR",Toast.LENGTH_SHORT).show()

            }





//            uploadImage()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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
                naviImg(userGoogle!!.photoUrl,user)
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




        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        val options = arrayOf("Select from gallery", "Take a photo")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> selectFromGallery()
                1 -> takePhoto()
            }
        }
        builder.show()




//
//        val intent= Intent()
//        intent.type="image/*"
//        intent.action=Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent,100)






    }







    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                200)
        } else {
            // Permission is already granted, start the camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 200)
        }





    }

    private fun selectFromGallery() {


        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)



    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val firebaseImg= findViewById<ImageView>(R.id.firebaseImage)
        if(requestCode==100&& resultCode== RESULT_OK)
        {
            ImageUri=data?.data!!

            firebaseImg.setImageURI(ImageUri)

        }

        else if (requestCode == 200 && resultCode == RESULT_OK) {

            val bitmap=data?.extras?.get("data") as Bitmap
            ImageUri=bitmapToUri(bitmap,this)


            firebaseImg.setImageURI(ImageUri)


            // Do something with the photo, such as display it in an ImageView
        }



    }


    private fun bitmapToUri(bitmap: Bitmap, context: Context): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Camera Capture", null)
        return Uri.parse(path)
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

//        val bitmapFile =
//            File(image.toString())
//        val bitmap = BitmapFactory.decodeFile(bitmapFile.toString())
//        val cache=com.example.hospitalbooking.KotlinClass.MyCache()
//        cache.saveBitmapToCahche("a",bitmap)


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
        val newMediTxt=mediText.replace("/","")
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
            "userMedicine" to newMediTxt,
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

                R.id.nav_BookAppoint -> {
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)

                }

                R.id.nav_viewAppoint -> {
                    val intent = Intent(this, DoctorAppointment::class.java)
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