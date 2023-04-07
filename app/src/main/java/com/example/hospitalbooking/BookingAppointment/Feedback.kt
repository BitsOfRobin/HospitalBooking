package com.example.hospitalbooking.BookingAppointment

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hospitalbooking.KotlinClass.UserInput
import com.example.hospitalbooking.R
import com.example.hospitalbooking.databinding.ActivityAppointmentFeedbackBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text


class Feedback : AppCompatActivity()  {

    // Use View Binding to access UI elements
    private lateinit var binding : ActivityAppointmentFeedbackBinding
    private val userInput = UserInput()

    // Create a Firestore instance
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    // Appointment Detail Variable
    private lateinit var docName :String
    private lateinit var userAppointment :String
    private lateinit var userName :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a layout XML file
        binding = ActivityAppointmentFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_appointment_feedback)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Comment Appointment")

        appointmentDetail()
        appointmentComment()

        binding.submitComment.setOnClickListener {
            if (userInput.answer1 == null || userInput.answer2 == null || userInput.rating == null) {
                Toast.makeText(this, "Please select answer for both question and provide a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                appointmentDetail()
                appointmentComment()
                appointmentUpdate()
                textComment()
            }
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

    private fun appointmentComment() {
        binding.questionOneRadio.setOnCheckedChangeListener { _, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            when (checkedId) {
                R.id.exceptional -> {
                    userInput.answer1 = radioButton.text.toString()
                    Toast.makeText(this, "${radioButton.text} is selected", Toast.LENGTH_SHORT).show()

                }
                R.id.satisfactory -> {
                    userInput.answer1 = radioButton.text.toString()
                    Toast.makeText(this, "${radioButton.text} is selected", Toast.LENGTH_SHORT).show()
                }
                R.id.adequate -> {
                    userInput.answer1 = radioButton.text.toString()
                    Toast.makeText(this, "${radioButton.text} is selected", Toast.LENGTH_SHORT).show()
                }
                R.id.unsatisfactory -> {
                    userInput.answer1 = radioButton.text.toString()
                    Toast.makeText(this, "${radioButton.text} is selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.questionTwoRadio.setOnCheckedChangeListener { _, checkedId ->
            val radioButton2: RadioButton = findViewById(checkedId)

            when (checkedId) {
                R.id.rude -> {
                    userInput.answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "${radioButton2.text} is selected", Toast.LENGTH_SHORT).show()
                }
                R.id.talkOwn -> {
                    userInput.answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "${radioButton2.text} is selected", Toast.LENGTH_SHORT).show()
                }
                R.id.talkLong -> {
                    userInput.answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "${radioButton2.text} is selected", Toast.LENGTH_SHORT).show()
                }
                R.id.extremeSkill -> {
                    userInput.answer2 = radioButton2.text.toString()
                    Toast.makeText(this, "${radioButton2.text} is selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ratingComment.setOnRatingBarChangeListener { _, rating, _ ->
            userInput.rating = rating
        }
    }
    private fun appointmentUpdate() {
        // Comment
        val comment = findViewById<TextView>(R.id.commentText)
        val commentText = comment.text.toString()
        Toast.makeText(this, "Your comment: ${comment.text}", Toast.LENGTH_SHORT).show()

        val feedback= "{docName=$docName, doctorAppoint=$userAppointment, user=$userName}"

        val feedbackFirebase = hashMapOf(
            "rateStar" to userInput.rating,
            "radioAns1" to userInput.answer1,
            "radioAns2" to userInput.answer2,
            "comment" to commentText
        )
        val docRef = db.collection("userAppointment").document(feedback)
        docRef.update(feedbackFirebase as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Thank you for your feedback.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "The feedback failed to submit please try again.", Toast.LENGTH_SHORT).show()
            }
    }
    private fun textComment() {
        val comment = findViewById<TextView>(R.id.commentText)
        Toast.makeText(this, "Your comment: ${comment.text}", Toast.LENGTH_SHORT).show()
    }
}