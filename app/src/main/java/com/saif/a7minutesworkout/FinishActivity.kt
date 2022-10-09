package com.saif.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.saif.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null
    private val finishButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarFinish)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarFinish?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.finishButton?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        addDateToDatabase(historyDao)

    }

    private fun addDateToDatabase(historyDao: HistoryDao){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time// return date objecct
        Log.e("TAG", "calender time: ${dateTime} ", )
        val simpleDateFormat = SimpleDateFormat("dd MMM yyy HH:mm:ss",Locale.getDefault())
        Log.e("TAG", "formated date" + simpleDateFormat, )
        val date = simpleDateFormat.format(dateTime) // return string date
        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
        }

    }
}