package com.example.lifestyleapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.lifestyleapp.Goal
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import org.jetbrains.anko.doAsync
import java.lang.ClassCastException


/**
 * A simple [Fragment] subclass.
 * Use the [GoalsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalsFragment : Fragment() {

    //declare goal fields
    private lateinit var goalRadioGroup: RadioGroup
    private lateinit var loseRadioButton: RadioButton
    private lateinit var gainRadioButton: RadioButton
    private lateinit var maintainRadioButton: RadioButton
    private lateinit var goalSubmit: Button

    lateinit var goal: Goal

    var mDataPasser: GoalsOnDataPass? = null

    interface GoalsOnDataPass {
        fun goalsOnDataPass(goal: Goal)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        goal = Goal.UNDEFINED
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignObjects()
        setOnClickListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { // context is a pointer to activity
            mDataPasser = context as GoalsOnDataPass // this is where magic happens
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }

    private fun assignObjects() {
        goalRadioGroup = this.view?.findViewById(R.id.goals_radioGroup) as RadioGroup
        loseRadioButton = this.view?.findViewById(R.id.goals_radio_lose) as RadioButton
        gainRadioButton = this.view?.findViewById(R.id.goals_radio_gain) as RadioButton
        maintainRadioButton = this.view?.findViewById(R.id.goals_radio_maintain) as RadioButton
        goalSubmit = this.view?.findViewById(R.id.goals_button_submit) as Button
    }

    private fun setOnClickListeners() {
        goalSubmit.setOnClickListener() {
            returnGoal()
            mDataPasser?.goalsOnDataPass(goal)
            doAsync {
                MainActivity.mUserViewModel.updateUserInfoInDB()
            }

        }

    }

    private fun returnGoal() {
        // one of the radio buttons is checked
        if (loseRadioButton.isChecked) {
            goal = Goal.LOSE
        } else if (gainRadioButton.isChecked) {
            goal = Goal.GAIN
        } else if (maintainRadioButton.isChecked) {
            goal = Goal.MAINTAIN
        }
    }

}