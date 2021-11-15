package com.example.lifestyleapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R


class statsFragment : Fragment() {
    private lateinit var bmi: TextView
    private lateinit var heightIn: TextView
    private lateinit var heightFt: TextView
    private lateinit var weight: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignValues()
        setValues()
    }

    private fun assignValues(){
        bmi = this.view?.findViewById(R.id.bmi_text) as TextView
        heightIn = this.view?.findViewById(R.id.height_in_text) as TextView
        heightFt = this.view?.findViewById(R.id.height_ft_text) as TextView
        weight = this.view?.findViewById(R.id.stats_weight_text) as TextView
    }

    private fun setValues(){
        if(MainActivity.mUserViewModel.currentUser.value?.bmi != ""){
            bmi.setText(MainActivity.mUserViewModel.currentUser.value?.bmi.toString())
        }
        if (MainActivity.mUserViewModel.currentUser.value?.heightFt != ""){
            heightFt.setText(MainActivity.mUserViewModel.currentUser.value?.heightFt.toString())
        }
        if(MainActivity.mUserViewModel.currentUser.value?.heightIn != ""){
            heightIn.setText(MainActivity.mUserViewModel.currentUser.value?.heightIn.toString())
        }
        if (MainActivity.mUserViewModel.currentUser.value?.weight != ""){
            weight.setText(MainActivity.mUserViewModel.currentUser.value?.weight.toString())
        }
    }
}