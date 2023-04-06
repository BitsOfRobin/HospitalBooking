package com.example.hospitalbooking.BookingAppointment

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitalbooking.R
import com.example.hospitalbooking.databinding.ActivityAppointmentFeedbackBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class Feedback : AppCompatActivity()  {
    private lateinit var binding : ActivityAppointmentFeedbackBinding
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var docName :String
    private lateinit var userAppointment :String
    private lateinit var userName :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppointmentFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_appointment_feedback)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Comment Appointment")

        appointmentDetail()
//        appointmentRating()
//        appointmentComment()
//        textComment()

        //val submit = findViewById<Button>(R.id.submitComment)
        binding.submitComment.setOnClickListener {
            appointmentDetail()
            appointmentRating()
            appointmentComment()
            textComment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun appointmentDetail() {
        docName = intent.getStringExtra("DoctorName").toString()
        userAppointment = intent.getStringExtra("Appointment").toString()
        userName = intent.getStringExtra("userName").toString()

        val doctor = findViewById<TextView>(R.id.doctorName)
        val appointment = findViewById<TextView>(R.id.appointment)
        val patient = findViewById<TextView>(R.id.patientName)

        doctor.text = docName
        appointment.text = userAppointment
        patient.text = userName
    }

    private fun appointmentRating() {
        val rating = findViewById<RatingBar>(R.id.ratingComment)

        val ratingStar = rating.rating
    }

    private fun appointmentComment() {
        // Rating Star
        val rating = findViewById<RatingBar>(R.id.ratingComment)
        val ratingStar = rating.rating

        // Radio Question
        val radioGroup = findViewById<RadioGroup>(R.id.questionOneRadio)
        val radioGroup2 = findViewById<RadioGroup>(R.id.questionTwoRadio)

        var answer1 = buttonGroupSelection(radioGroup)
        var answer2 = buttonGroupSelection2(radioGroup2)

        // Comment
        val comment = findViewById<TextView>(R.id.commentText)
        var commentText = comment.text.toString()
        Toast.makeText(this, "Your comment: ${comment.text.toString()}", Toast.LENGTH_SHORT).show()

        val feedback = hashMapOf(
            "docName" to docName,
            "doctorAppoint" to userAppointment,
            "user" to userName
        )

        val feedbackFirebase = hashMapOf(
            "docName" to docName,
            "doctorAppoint" to userAppointment,
            "user" to userName,
            "rateStar" to ratingStar,
            "radioAns1" to answer1,
            "radioAns2" to answer2,
            "comment" to commentText
        )

        db.collection("userAppointment").document(feedback.toString()).collection("comment").document(feedbackFirebase.toString())
            .set(feedbackFirebase)
            .addOnSuccessListener {
                Toast.makeText(this, "Thank you for your feedback.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "The feedback failed to submit please try again.", Toast.LENGTH_SHORT).show()
            }



//        var exception = ""
//        var satisfactory = ""
//        var adequate = ""
//        var unsatisfactory = ""

//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            val radioButton = findViewById<RadioButton>(checkedId)
//            //val selectedOption = radioButton.text.toString()
//
//            when(checkedId){
//                R.id.exceptional -> {
//                    exception = radioButton.text.toString()
//                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
//                }
//
//                R.id.satisfactory -> {
//                    satisfactory = radioButton.text.toString()
//                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
//                }
//
//                R.id.adequate -> {
//                    adequate = radioButton.text.toString()
//                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
//                }
//
//                R.id.unsatisfactory -> {
//                    unsatisfactory = radioButton.text.toString()
//                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }
    }
    private fun buttonGroupSelection(radioGroup: RadioGroup): String {
        var answer = ""

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            //val selectedOption = radioButton.text.toString()

            when(checkedId){
                R.id.exceptional -> {
                    answer = radioButton.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.satisfactory -> {
                    answer = radioButton.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.adequate -> {
                    answer = radioButton.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.unsatisfactory -> {
                    answer = radioButton.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton.text.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return answer
    }
    private fun buttonGroupSelection2(radioGroup: RadioGroup): String {
        var answer2 = ""

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton2 = findViewById<RadioButton>(checkedId)
            //val selectedOption = radioButton.text.toString()

            when(checkedId){
                R.id.rude -> {
                    answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton2.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.talkOwn -> {
                    answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton2.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.talkLong -> {
                    answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton2.text.toString()}", Toast.LENGTH_SHORT).show()
                }

                R.id.extremeSkill -> {
                    answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "You selected: ${radioButton2.text.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return answer2
    }

    private fun textComment() {
        val comment = findViewById<TextView>(R.id.commentText)
        Toast.makeText(this, "Your comment: ${comment.text.toString()}", Toast.LENGTH_SHORT).show()
    }
}