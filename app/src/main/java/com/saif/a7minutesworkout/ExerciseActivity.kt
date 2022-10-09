package com.saif.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saif.a7minutesworkout.databinding.ActivityExerciseBinding
import com.saif.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var exerciseActivityBinding : ActivityExerciseBinding? = null
    private var restTimer : CountDownTimer? = null
    private var exerciseTimer : CountDownTimer? = null
    private var restProgress = 0
    private var exerciseProgress = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var textToSpeech : TextToSpeech? = null
    private var mediaPlayer : MediaPlayer? = null
    private var adapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exerciseActivityBinding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(exerciseActivityBinding?.root)

        exerciseList = Constants.defaultExerciseList()

        setSupportActionBar(exerciseActivityBinding?.toolBarExercise)
        textToSpeech = TextToSpeech(this, this)

        // the back arrow button is visible if we write the two lines of code below
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // Handle the task of going back when the back button is pressed
        exerciseActivityBinding?.toolBarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        setRestView()
        setUpExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()

    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        //simple method to show the content of the dialog_custom_back_confirmation
        //dialog.setContentView(R.layout.dialog_custom_back_confirmation)

        // Databinding approach for setting the contentview for the custom dialog
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        dialogBinding.yesButton.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.noButton.setOnClickListener {
            customDialog.dismiss()
        }

//        val yesButton = dialog.findViewById(R.id.yesButton) as Button
//        val noButton = dialog.findViewById(R.id.noButton) as Button
//        yesButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        noButton.setOnClickListener {
//            dialog.dismiss()
//        }
        customDialog.setCancelable(false)
        customDialog.show()
    }


    private fun setUpExerciseStatusRecyclerView(){
        adapter = ExerciseStatusAdapter(exerciseList!!)
        exerciseActivityBinding?.rvExerciseStatus?.adapter = adapter
        exerciseActivityBinding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,
            RecyclerView.HORIZONTAL,false)
    }

    private fun setExerciseProgressBar(){
        exerciseActivityBinding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                exerciseActivityBinding?.progressBarExercise?.progress = 30 - exerciseProgress
                exerciseActivityBinding?.textViewTimerExercise?.text = (30 - exerciseProgress).toString()

            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseList!![currentExercisePosition].setIsSelected(false)
                adapter?.notifyDataSetChanged()
               if (currentExercisePosition < exerciseList!!.size - 1){
                   setRestView()
               }else{
                   val intent = Intent(applicationContext, FinishActivity::class.java)
                   startActivity(intent)
                   finish()
               }
            }

        }.start()
    }

    private fun setRestProgressBar(){
        exerciseActivityBinding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                exerciseActivityBinding?.progressBar?.progress = 10 - restProgress
                exerciseActivityBinding?.textViewTimer?.text = (10 - restProgress).toString()

            }

            override fun onFinish() {
                currentExercisePosition++
                setUpExerciseView()
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                adapter?.notifyDataSetChanged()
            }

        }.start()
    }

    private fun setRestView(){
        try {
            val soundURI = Uri.parse("android.resource://com.saif.a7minutesworkout/" +
            R.raw.press_start)
            mediaPlayer = MediaPlayer.create(this, soundURI)
            mediaPlayer?.isLooping = false
            mediaPlayer?.start()
        }catch (e: Exception){
            Toast.makeText(this, e.toString(),Toast.LENGTH_SHORT).show()
        }
        exerciseActivityBinding?.flRestView?.visibility = View.VISIBLE
        exerciseActivityBinding?.textViewTitle?.visibility = View.VISIBLE
        exerciseActivityBinding?.tvExerciseName?.visibility = View.INVISIBLE
        exerciseActivityBinding?.flExerciseView?.visibility = View.INVISIBLE
        exerciseActivityBinding?.ivImage?.visibility = View.INVISIBLE
        exerciseActivityBinding?.tvUpcomingLabel?.visibility = View.VISIBLE
        exerciseActivityBinding?.tvUpcomingExerciseName?.visibility  = View.VISIBLE
        exerciseActivityBinding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setUpExerciseView(){
        exerciseActivityBinding?.flRestView?.visibility = View.INVISIBLE
        exerciseActivityBinding?.textViewTitle?.visibility = View.INVISIBLE
        exerciseActivityBinding?.tvExerciseName?.visibility = View.VISIBLE
        exerciseActivityBinding?.flExerciseView?.visibility = View.VISIBLE
        exerciseActivityBinding?.ivImage?.visibility = View.VISIBLE
        exerciseActivityBinding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        exerciseActivityBinding?.tvUpcomingExerciseName?.visibility  = View.INVISIBLE
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        speckOut(exerciseList!![currentExercisePosition].getName())
        exerciseActivityBinding?.ivImage?.setImageResource(
            exerciseList!![currentExercisePosition].getImage())
        exerciseActivityBinding?.tvExerciseName?.text = exerciseList!![currentExercisePosition]?.getName()
        setExerciseProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (textToSpeech != null){
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
        exerciseActivityBinding = null
    }

    private fun speckOut(text : String){
        textToSpeech?.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = textToSpeech!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported")
            }
        }else{
            Log.e("TTS", "Initialization failed")
        }
    }
}