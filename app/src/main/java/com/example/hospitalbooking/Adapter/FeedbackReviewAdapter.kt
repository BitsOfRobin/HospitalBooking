package com.example.hospitalbooking.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalbooking.KotlinClass.feedbackReview
import com.example.hospitalbooking.R
import java.util.ArrayList

class FeedbackReviewAdapter(var context: Context, private var feedbackReview: ArrayList<feedbackReview>): RecyclerView.Adapter<FeedbackReviewAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "FeedbackReviewAdapter"
    }

    class ViewHolder(commentItem: View) : RecyclerView.ViewHolder(commentItem){
        private val feedbackUser: TextView = commentItem.findViewById(R.id.feedbackUserName)
        private val feedbackRating: RatingBar = commentItem.findViewById(R.id.feedbackRating)
        private val feedbackComment: TextView = commentItem.findViewById(R.id.feedbackComment)

        fun bind(review: feedbackReview){
            feedbackUser.text = review.userName
            feedbackRating.rating = review.ratingStar.toFloat()
            feedbackComment.text = review.comment
        }
    }

    override fun getItemCount() = feedbackReview.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = feedbackReview[position]

        holder.bind(commentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val feedback = LayoutInflater.from(parent.context)

        val commentItem = feedback.inflate(R.layout.comment_item, parent, false)

        // Return the view holder
        return ViewHolder(commentItem)
    }
}