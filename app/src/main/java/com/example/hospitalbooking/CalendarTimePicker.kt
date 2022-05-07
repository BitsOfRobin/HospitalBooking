package com.example.hospitalbooking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class CalendarTimePicker : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0
    var realDate=" "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_time_picker)
        pickDate()

    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)


    }

    private fun pickDate() {

        val btnTime = findViewById<Button>(R.id.btn_timePicker)
        btnTime.setOnClickListener {

            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()

        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        savedDay = p3
        savedMonth = p2+1
        savedYear = p1

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2
        val tvTime = findViewById<TextView>(R.id.tv_textTime)
        tvTime.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute:$savedMinute"





        var dateString = "$savedMonth/$savedDay/$savedYear"
        var timeString="$savedHour:$savedMinute"
        var totalDate="$savedMonth/$savedDay/$savedYear $savedHour:$savedMinute"







        when {
            savedHour<10 && savedMinute<10-> {
                totalDate="$savedMonth/$savedDay/$savedYear 0$savedHour:0$savedMinute"
            }
            savedHour<10 -> {
                totalDate="$savedMonth/$savedDay/$savedYear 0$savedHour:$savedMinute"
            }
            savedMinute<10 -> {
                totalDate="$savedMonth/$savedDay/$savedYear $savedHour:0$savedMinute"
            }




        }

        val dateTime = LocalDateTime.parse(totalDate, DateTimeFormatter.ofPattern("M/d/y HH:mm"))
//        val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/y"))
//        val time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:m"))
         realDate=dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))

//        val timestamp = java.sql.Timestamp.valueOf(realDate)




        Toast.makeText(this,"time$realDate",Toast.LENGTH_SHORT).show()

        updateDoc()


    }


    private fun updateDoc()
    {
        val doctorName = intent.getStringExtra("DoctorName")

        val mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()

        val doctor= hashMapOf(
            "Time" to realDate




        )
//        val  doc =doctor?.uid

//
        mFirebaseDatabaseInstance?.collection("doctor")?.document("$doctorName").update("Time",realDate) .addOnSuccessListener {


            Toast.makeText(this,"Successfully update doctor ",Toast.LENGTH_SHORT).show()

        }
            ?.addOnFailureListener {

                Toast.makeText(this,"Failed to update doctor",Toast.LENGTH_SHORT).show()
            }


    }
}