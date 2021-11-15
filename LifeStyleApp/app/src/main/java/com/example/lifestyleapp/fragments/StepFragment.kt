package com.example.lifestyleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class StepFragment : Fragment() {

    private lateinit var stepsText: TextView
    private lateinit var progressCircle: CircularProgressBar
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("ON VIEW CREATED")
        assignObjects() // assign all the objects
        setResetStepsListener()
        MainActivity.currentSteps.observe(viewLifecycleOwner){
            setSteps()
        }
    }

    private fun assignObjects() {
        println("ASSIGN OBJECTS")
        stepsText = this.view?.findViewById(R.id.steps_taken) as TextView
        progressCircle = this.view?.findViewById(R.id.circularProgressBar) as CircularProgressBar
        resetButton = this.view?.findViewById(R.id.reset_steps_button) as Button
    }

    private fun setSteps(){
        stepsText.text = MainActivity.currentSteps.value.toString()
        progressCircle.apply {
            MainActivity.currentSteps.value?.let { setProgressWithAnimation(it.toFloat()) }
        }
    }

    private fun setResetStepsListener(){
        resetButton.setOnClickListener{
            Toast.makeText(this.context, "long press to reset steps", Toast.LENGTH_SHORT).show()
        }

        resetButton.setOnLongClickListener{
            MainActivity.previousTotalSteps = MainActivity.totalSteps
            stepsText.text = 0.toString()

            true
        }
    }

}