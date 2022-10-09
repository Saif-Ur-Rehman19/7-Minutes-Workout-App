package com.saif.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.saif.a7minutesworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    private var binding : ActivityBmiactivityBinding? = null
    private var metricIsAtive = true
    private var usIsActive = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.bmiLabel?.visibility = View.INVISIBLE
        binding?.usMetric?.visibility = View.INVISIBLE
        binding?.unitRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            if (binding?.radioButtonMetricUnit?.isChecked == true){
                metricIsAtive = true
                usIsActive = false
                binding?.heightLayout?.visibility = View.VISIBLE
                binding?.usMetric?.visibility = View.INVISIBLE
                binding?.textViewMessageContent?.visibility = View.INVISIBLE
                binding?.textViewMessageTitle?.visibility = View.INVISIBLE
                binding?.bmiLabel?.visibility = View.INVISIBLE
                binding?.bmiResultTextView?.visibility = View.INVISIBLE
                binding?.editTextWeight?.visibility = View.VISIBLE
                binding?.weightInPoundLayout?.visibility = View.INVISIBLE
                binding?.weightLayout?.visibility = View.VISIBLE
                binding?.editTextHeight?.text?.clear()
                binding?.editTextWeight?.text?.clear()
            }
            if (binding?.radioButtonUsUnits?.isChecked == true){
                binding?.editTextWeight?.visibility = View.INVISIBLE
                binding?.weightLayout?.visibility = View.INVISIBLE
                binding?.weightInPoundLayout?.visibility = View.VISIBLE
                usIsActive = true
                metricIsAtive = false
                binding?.heightLayout?.visibility = View.INVISIBLE
                binding?.usMetric?.visibility = View.VISIBLE
                binding?.textViewMessageContent?.visibility = View.INVISIBLE
                binding?.textViewMessageTitle?.visibility = View.INVISIBLE
                binding?.bmiLabel?.visibility = View.INVISIBLE
                binding?.bmiResultTextView?.visibility = View.INVISIBLE
                binding?.editTextHeight?.text?.clear()
                binding?.editTextWeight?.text?.clear()
                binding?.usMetricHeightInch?.text?.clear()
                binding?.editTextWeightInPound?.text?.clear()
                binding?.usMetricUnitHeightFeet?.text?.clear()
            }
        }

        binding?.bmiCalculateButton?.setOnClickListener {
            if (validateMetricUnits() && metricIsAtive){
                val height : Float = binding?.editTextHeight?.text.toString().toFloat() / 100
                val weight : Float = binding?.editTextWeight?.text.toString().toFloat()
                val bmi = weight / (height * height)
                displayBMIResult(bmi)
            }
            if (validateUsUnits() && usIsActive){
                calculateUnits()
            }
        }
    }

    private fun displayBMIResult(bmi : Float){
        val bmiLabel : String
        val bmiDescription : String
        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding?.textViewMessageContent?.visibility = View.VISIBLE
        binding?.textViewMessageTitle?.visibility = View.VISIBLE
        binding?.bmiLabel?.visibility = View.VISIBLE
        binding?.bmiResultTextView?.visibility = View.VISIBLE
        binding?.textViewMessageTitle?.text = bmiLabel
        binding?.textViewMessageContent?.text = bmiDescription
        val bmiValue = BigDecimal(bmi.toDouble()).
        setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.bmiResultTextView?.text = bmiValue
    }

    private fun validateMetricUnits() : Boolean{
        var isValid = true
        if (binding?.editTextWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.editTextHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if (validateUsUnits()){
            val usUnitHeightFeet : String = binding?.usMetricUnitHeightFeet?.text.toString()
            val usUnitHeightInch : String = binding?.usMetricHeightInch?.text.toString()
            val usUnitWeightValue : Float = binding?.editTextWeightInPound?.text.toString().toFloat()
            val height = usUnitHeightInch.toFloat() + usUnitHeightFeet.toFloat() * 12
            val bmi = 703 * (usUnitWeightValue / (height * height))
            displayBMIResult(bmi)
        }else{
            Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()

        }
    }

    private fun validateUsUnits() : Boolean{
        var isValid = true
        when{
            binding?.editTextWeightInPound?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.usMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.usMetricHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}