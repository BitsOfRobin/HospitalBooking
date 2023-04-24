package com.example.hospitalbooking.DoctorInformationManagement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalbooking.Adapter.FeedbackReviewAdapter
import com.example.hospitalbooking.KotlinClass.MyCache
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.example.hospitalbooking.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DoctorSummarizeReport : AppCompatActivity() {
    private var mFirebaseDatabaseInstance: FirebaseFirestore ?= null
    private lateinit var summarizeReportViewModel: SummarizeReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_summarize_report)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Doctor Summarize Report"


        val docName = findViewById<TextView>(R.id.dotName)
        val docJob = findViewById<TextView>(R.id.dtPro)
        val docLocation = findViewById<TextView>(R.id.dtHos)
        val numRate = findViewById<TextView>(R.id.numRate)

        val userGoogle = Firebase.auth.currentUser
        var dtname = ""
        var hospital = ""
        var doctorSpecialist = ""
        var numberFeedback = 0

        userGoogle.let {
            if (userGoogle != null) {
                dtname = "Dr " + userGoogle.displayName.toString()
            }

        }
        val cache = MyCache()
        val bitmap = cache.retrieveBitmapFromCache(dtname)
        val imgDoc = findViewById<ImageView>(R.id.ImgMed)
        imgDoc.setImageBitmap(bitmap)

        val documentRef = mFirebaseDatabaseInstance!!.collection("doctor")
        documentRef.whereEqualTo("name", dtname)
            .get().addOnSuccessListener {
                for(document in it){
                    doctorSpecialist = document.get("pro").toString()
                    hospital = document.get("hospital").toString()
                    numberFeedback = (document.get("numRatings") as Long).toInt()
                }
                docName.text = dtname
                docJob.text = doctorSpecialist
                docLocation.text = hospital
                numRate.text = numberFeedback.toString()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
        summarizeReportViewModel = ViewModelProvider(this)[SummarizeReportViewModel::class.java]

        getCalculateRating(dtname)
        getFeedbackCommentResult(dtname)
        getFeedbackReview(dtname)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getCalculateRating(docName: String){
        val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
        summarizeReportViewModel.calculateAverageRating(docName)

        summarizeReportViewModel.averageRating.observe(this, Observer { averageRating ->
            // Update the UI with the average rating
            ratingBar.rating = averageRating.toFloat()
        })
    }

    private fun getFeedbackCommentResult(docName: String){
        summarizeReportViewModel.questionOneResult(docName)
        summarizeReportViewModel.feedbackAnsOne.observe(this) { feedbackAnsOne ->
            findViewById<TextView>(R.id.quesOneOptionOne).text = feedbackAnsOne[0].toString()
            findViewById<TextView>(R.id.quesOneOptionTwo).text = feedbackAnsOne[1].toString()
            findViewById<TextView>(R.id.quesOneOptionThree).text = feedbackAnsOne[2].toString()
            findViewById<TextView>(R.id.quesOneOptionFour).text = feedbackAnsOne[3].toString()
        }

        summarizeReportViewModel.questionTwoResult(docName)
        summarizeReportViewModel.feedbackAnsTwo.observe(this) { feedbackAnsTwo ->
            findViewById<TextView>(R.id.quesTwoOptionOne).text = feedbackAnsTwo[0].toString()
            findViewById<TextView>(R.id.quesTwoOptionTwo).text = feedbackAnsTwo[1].toString()
            findViewById<TextView>(R.id.quesTwoOptionThree).text = feedbackAnsTwo[2].toString()
            findViewById<TextView>(R.id.quesTwoOptionFour).text = feedbackAnsTwo[3].toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getFeedbackReview(docName: String) {
        summarizeReportViewModel.feedbackReview(docName)

        val feedbackReviewView = findViewById<RecyclerView>(R.id.feedbackReviewRecycler)
        feedbackReviewView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        summarizeReportViewModel.feedbackReview.observe(this) { feedbackReview ->
            val arrFeedback = FeedbackReviewAdapter(this, feedbackReview as ArrayList<feedbackReview>)
            arrFeedback.notifyDataSetChanged()
            feedbackReviewView.adapter = arrFeedback
        }
    }
}