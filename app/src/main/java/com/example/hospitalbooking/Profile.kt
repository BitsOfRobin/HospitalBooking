package com.example.hospitalbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class Profile : AppCompatActivity() {
    private lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()
        val btn=findViewById<Button>(R.id.logoutBtn)
        btn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()

        }

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
            val etxt=findViewById<TextView>(R.id.emailTv)
            etxt.text=email

        }


    }


}