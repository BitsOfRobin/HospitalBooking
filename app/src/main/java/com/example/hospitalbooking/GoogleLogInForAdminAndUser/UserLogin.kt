package com.example.hospitalbooking.GoogleLogInForAdminAndUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hospitalbooking.*
import com.example.hospitalbooking.BookingAppointment.MainPage
import com.example.hospitalbooking.MedicineOCR.MedicineRecord
import com.example.hospitalbooking.MedicineOCR.UserMedicine
import com.example.hospitalbooking.PrescriptionControl.PrescriptionDisplay
import com.example.hospitalbooking.UserAppointmentManagement.DoctorAppointment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class UserLogin : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null
    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var firebaseAuth:FirebaseAuth
//    private lateinit var binding: com.example.hospitalbooking.databinding.ActivityUserLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
//        checkUser()
        showNavBar()
        googleLog()
    }
private companion object{

    private const val  RC_SIGN_IN=100
    private const val TAG="GOOGLE_SING_IN_TAG"
}

    private fun checkUserLog() {

        val id = findViewById<EditText>(R.id.id)
        val pass = findViewById<EditText>(R.id.pass)



        var valid=false

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {


                mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
                mFirebaseDatabaseInstance!!.collection("user").whereEqualTo("userId", id).get()
                    .addOnSuccessListener {


                        Toast.makeText(this, "the user presents  ", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {

                        Toast.makeText(this, "Failed to search user ", Toast.LENGTH_SHORT).show()
                    }


                mFirebaseDatabaseInstance!!.collection("user").whereEqualTo("userPass", pass).get()
                    .addOnSuccessListener {


                        Toast.makeText(this, "correct password  ", Toast.LENGTH_SHORT).show()
                        valid=true
                    }
                    .addOnFailureListener {

                        Toast.makeText(this, "Invalid password ", Toast.LENGTH_SHORT).show()
                    }


            if(valid) {
                val user = hashMapOf(
                    "userId" to id.text.toString(),


                    )
//        val  doc =doctor?.uid


                mFirebaseDatabaseInstance?.collection("ValidUser")?.document("userInfo")
                    ?.set(user)?.addOnSuccessListener {


                        Toast.makeText(
                            this,
                            "Successfully added to valid user  ",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                    ?.addOnFailureListener {

                        Toast.makeText(this, "Failed to add valid user ", Toast.LENGTH_SHORT).show()
                    }

            }
        }






    }


    private fun googleLog(){

        val googleSignInoptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("385146186710-cvq262o56jch8hh3d51u6skh5f4qehqt.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient=GoogleSignIn.getClient(this,googleSignInoptions)

        firebaseAuth= FirebaseAuth.getInstance()



        val btn=findViewById<SignInButton>(R.id.googleSignInBtn)
        btn.setOnClickListener {
            val intent=googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)

        }

        checkUser()

    }


    private fun checkUser()
    {
        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser!=null)
        {
//            startActivity(Intent(this,Profile::class.java))

            val intent = Intent(this, Profile::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
            startActivity(intent)
            finish()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "Activity Result II ", Toast.LENGTH_SHORT).show()
        if(requestCode== RC_SIGN_IN)
        {

            Toast.makeText(this, "Activity Result ", Toast.LENGTH_SHORT).show()
            val accountTask=GoogleSignIn.getSignedInAccountFromIntent(data)

//            try{


                val account=accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
//            }

//            catch(e:Exception)
//            {
//               sage}") Log.d(TAG,"ONActivityResult:${e.mes
//                Toast.makeText(this, "Activity Result ", Toast.LENGTH_SHORT).show()
//            }


        }



    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG,"BEGIN firebase with google account")
        val credential=GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            Toast.makeText(this, "LoggedIn ", Toast.LENGTH_SHORT).show()
            val firebaseUser=firebaseAuth.currentUser
            val uid=firebaseUser!!.uid
            val email= firebaseUser!!.email
            Toast.makeText(this, "id;$uid ", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "email:$email ", Toast.LENGTH_SHORT).show()

            if(it.additionalUserInfo!!.isNewUser)
            {

                Toast.makeText(this, "new acc is created$email ", Toast.LENGTH_SHORT).show()
            }
            else{

                Toast.makeText(this, "existing user$email ", Toast.LENGTH_SHORT).show()
            }


            val intent = Intent(this, Profile::class.java)
//            intent.putExtra("DoctorName", tempListViewClickedValue)
            startActivity(intent)

        }


        .addOnFailureListener {
            Toast.makeText(this, "LoggedIn Failed${it.message} ", Toast.LENGTH_SHORT).show()
        }



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
}