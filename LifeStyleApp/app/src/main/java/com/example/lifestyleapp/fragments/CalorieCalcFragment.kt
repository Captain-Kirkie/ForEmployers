package com.example.lifestyleapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CalorieCalcFragment : Fragment() {

    private lateinit var height: EditText
    private lateinit var weight: EditText
    private lateinit var age: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var activityRadioGroup: RadioGroup
    private lateinit var activeRadioButton: RadioButton
    private lateinit var sedentaryRadioButton: RadioButton
    private lateinit var desiredWeightChange: EditText
    private lateinit var weightChangeTV: TextView
    private lateinit var calorieRecommendation: TextView
    private lateinit var calculateButton: Button

    private lateinit var goal: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calorie_calc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignValues()
        setInitialUserValues()
        setOnClickListeners()
        setGoalText()
    }

    private fun assignValues() {
        // assign the buttons
        height = this.view?.findViewById(R.id.height_et) as EditText
        weight = this.view?.findViewById(R.id.weight_et) as EditText
        age = this.view?.findViewById(R.id.age_et) as EditText
        genderRadioGroup = this.view?.findViewById(R.id.gender_radio_group) as RadioGroup
        maleRadioButton = this.view?.findViewById(R.id.male_radio_button) as RadioButton
        femaleRadioButton = this.view?.findViewById(R.id.female_radio_button) as RadioButton
        activityRadioGroup = this.view?.findViewById(R.id.activity_radio_group) as RadioGroup
        activeRadioButton = this.view?.findViewById(R.id.active_radio_button) as RadioButton
        sedentaryRadioButton = this.view?.findViewById(R.id.sedentary_radio_button) as RadioButton
        desiredWeightChange = this.view?.findViewById(R.id.desired_weight_change_et) as EditText
        weightChangeTV = this.view?.findViewById(R.id.weight_change_goal_tv) as TextView
        calorieRecommendation = this.view?.findViewById(R.id.calorie_rec_tv) as TextView
        calculateButton = this.view?.findViewById(R.id.calculate_button) as Button
    }

    override fun onResume() {
        super.onResume()
        setInitialUserValues()
    }

    @SuppressLint("SetTextI18n")
    private fun setInitialUserValues() {
        height.setText(heightInInches().toString())
        if (MainActivity.mUserViewModel.currentUser.value?.weight.toString() != "") {
            weight.setText(MainActivity.mUserViewModel.currentUser.value?.weight.toString())
        } else {
            weight.setText("0")
        }

        val currAge = getAge().toString()
        age.text.clear()
        age.setText(currAge)
        if (MainActivity.mUserViewModel.currentUser.value?.gender == "Male" || MainActivity.mUserViewModel.currentUser.value?.gender == "") {
            genderRadioGroup.check(R.id.male_radio_button)
        } else {
            genderRadioGroup.check(R.id.female_radio_button)
        }
        goal = MainActivity.mUserViewModel.currentUser.value?.goal.toString()
    }

    private fun setOnClickListeners() {
        calculateButton.setOnClickListener() {
            if (!activeRadioButton.isChecked && !sedentaryRadioButton.isChecked) {
                Toast.makeText(activity, "Please select an activity level", Toast.LENGTH_SHORT)
                    .show()
            } else {
                calculateCalories()
            }
        }
    }


    private fun setGoalText() {
        when (goal) {
            "lose" -> {
                weightChangeTV.visibility = View.VISIBLE
                calorieRecommendation.visibility = View.VISIBLE
                weightChangeTV.text = "How many pounds would you like to lose each week?"
            }
            "gain" -> {
                weightChangeTV.visibility = View.VISIBLE
                calorieRecommendation.visibility = View.VISIBLE
                weightChangeTV.text = "How many pounds would you like to gain each week?"
            }
            else -> {
                weightChangeTV.visibility = View.INVISIBLE
                desiredWeightChange.visibility = View.INVISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateCalories() {
        val age = getAge()
        var bmr = 0.0
        val weightInt = weight.text.toString().toInt()
        val heightTotalInches = height.text.toString().toInt()
        val isMale = maleRadioButton.isChecked
        bmr = if (isMale) {
            66.47 + (6.24 * weightInt) + (12.7 * heightTotalInches) - (6.755 * age)
        } else {
            655.1 + (4.35 * weightInt) + (4.7 * heightTotalInches) - (4.7 * age)
        }

        bmr *= if (activeRadioButton.isChecked) {
            1.55
        } else {
            1.2
        }

        if (desiredWeightChange.text.isBlank()) {
            desiredWeightChange.setText("0")
        }
        var enteredWeightChange = desiredWeightChange.text.toString().toInt()
        if (goal == "lose") {
            enteredWeightChange *= -1
        }

        if (kotlin.math.abs(enteredWeightChange) > 2) {
            calorieRecommendation.text =
                "Altering your weight by that much is dangerous, values should be 2 or below"
        } else {
            println("bmr pre changes $bmr")
            when {
                enteredWeightChange < 0 -> {
                    println("negative weight change $enteredWeightChange")
                    bmr += enteredWeightChange * 500
                    println("bmr post changes $bmr")
                    if (isMale && bmr < 1200) {
                        calorieRecommendation.text =
                            "Our formula is recommending too few calories for you. Eat at least 1200 calories daily!"
                    } else if (!isMale && bmr < 1000) {
                        calorieRecommendation.text =
                            "Our formula is recommending too few calories for you. Eat at least 1000 calories daily!"
                    } else {
                        println("bmr before SetText $bmr")
                        calorieRecommendation.text =
                            "Recommended calorie consumption is: " + bmr.toInt().toString()
                    }
                }
                enteredWeightChange > 0 -> {
                    println("positive weight change $enteredWeightChange")
                    println("bmr post changes, before settext() $bmr")
                    calorieRecommendation.text = "Recommended calorie consumption is: " +
                            ((bmr + (enteredWeightChange * 500)).toInt()).toString()
                }
                else -> {

                    println("no weight change $enteredWeightChange")
                    println("bmr no changes $bmr")
                    calorieRecommendation.text = "Recommended calorie consumption is: " +
                            (bmr.toInt()).toString()
                }
            }
        }
    }

    private fun getAge(): Int {
        val dobStr = MainActivity.mUserViewModel.currentUser.value?.dob?.replace("\\s".toRegex(), "")
        println("dob string $dobStr")
        return if (dobStr == "0/0/0" || dobStr == null || dobStr == "") {
            0
        } else {
            val formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            val dob = LocalDate.parse(dobStr, formatter)
            ChronoUnit.YEARS.between(dob, LocalDate.now()).toInt()
        }
    }


    private fun heightInInches(): Int {
        var ft = 0
        var inches = 0
        val userFt = MainActivity.mUserViewModel.currentUser.value?.heightFt
        if (userFt != "" && userFt != null) {
            ft = userFt.toInt()
        }
        val userInches = MainActivity.mUserViewModel.currentUser.value?.heightIn
        if (userInches != "" && userInches != null) {
            inches = userInches.toInt()
        }
        return ft * 12 + inches
    }


}