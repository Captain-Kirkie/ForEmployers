package com.example.lifestyleapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import com.example.lifestyleapp.user.StepsEntity
import com.example.lifestyleapp.user.UserCreds
import com.example.lifestyleapp.user.UserEntity
import com.example.lifestyleapp.user.WeatherEntity
import com.example.lifestyleapp.utils.Utils
import org.jetbrains.anko.doAsync

/**
 * handles the registration of users, passing info to local storage and eventually on to DB
 */
class Register_Fragment : Fragment() {
    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var userNameET: EditText
    private lateinit var passwordET: EditText
    private lateinit var registerButton: Button

    private lateinit var loginNavigate: registerNavigateInterface

    interface registerNavigateInterface {
        fun loginNavigate()
        fun userInfoNavigate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("ON VIEW CREATED")
        assignObjects() // assign all the objects
        setOnClickListeners()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("on attach user info")
        try { // context is a pointer to activity
            loginNavigate = context as registerNavigateInterface // this is where magic happens
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }

    /**
     * set objects to proper elements
     */
    private fun assignObjects() {
        println("ASSIGN OBJECTS")
        firstNameET = this.view?.findViewById(R.id.registerFirstName) as EditText
        lastNameET = this.view?.findViewById(R.id.registerLastName) as EditText
        userNameET = this.view?.findViewById(R.id.registerUserName) as EditText
        passwordET = this.view?.findViewById(R.id.registerPassword) as EditText
        registerButton = this.view?.findViewById(R.id.registerButton) as Button
    }

    /**
     * sets listener for register button
     */
    private fun setOnClickListeners() {
        registerButton.setOnClickListener {
            doAsync { collectUserInfo() }
        }
    }

    /**
     * gathers user info from form and creates user (upon validation).
     * userCheck validates if there is an existing user with that user name in existence. prevents duplicates
     */
    private fun collectUserInfo() {
        val fName = firstNameET.text.toString().trim()
        val lName = lastNameET.text.toString().trim()
        val userName = userNameET.text.toString().trim()
        val password = Utils.md5(passwordET.text.toString().trim())  // hash the password
        val userCheck = MainActivity.mUserViewModel.checkForUser(userName)
        if (password.isEmpty()) {
            Toast.makeText(context, "password is required", Toast.LENGTH_SHORT).show()
        } else {
            if (userCheck > 0) {
                Toast.makeText(context, "Username already in use", Toast.LENGTH_SHORT).show()
            } else {
                //create user cred
                val creds = UserCreds(userName, password)

                MainActivity.mUserViewModel.addCreds(creds)

                //create user and add to user table
                val user = UserEntity(userName, fName, lName)

                MainActivity.mUserViewModel.addUserToDB(user)
                MainActivity.mUserViewModel.addSteps(StepsEntity(userName, 0))
                MainActivity.weatherViewModel.addWeather(WeatherEntity(userName, ""))

                //make view model user new user object
                MainActivity.mUserViewModel.currentUser.postValue(user)

                //navigate to userinfo frag
                loginNavigate.userInfoNavigate()
                MainActivity.LOGGED_IN = true
            }
        }
    }
}