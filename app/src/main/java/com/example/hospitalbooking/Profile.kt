package com.example.hospitalbooking

import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.InputStream


class Profile : AppCompatActivity() {
    private lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Profile")
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        val btn=findViewById<Button>(R.id.logoutBtn)
        btn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun checkUser() {
        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser==null)
        {
            startActivity(Intent(this,UserLogin::class.java))
            finish()
        }
        else{

            val email=firebaseUser.email
            val data=firebaseUser.displayName
            val image=firebaseUser.photoUrl
            val phone=firebaseUser.phoneNumber

            val etxt=findViewById<TextView>(R.id.emailTv)
            val name=findViewById<TextView>(R.id.DisplayName)
            val userImg=findViewById<ImageView>(R.id.userImg)

            etxt.text="Email:\n$email"
            name.text="Name:$data\n Phone Number:$phone"
            Picasso.get().load(image).into(userImg);


        }


    }


}