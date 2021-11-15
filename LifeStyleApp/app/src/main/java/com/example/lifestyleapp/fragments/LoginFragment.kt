package com.example.lifestyleapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import com.example.lifestyleapp.utils.Utils
import org.jetbrains.anko.doAsync


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var userNameET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var loginNavigate: loginNavigateInterface

    interface loginNavigateInterface {
        fun loginNavigate()
        fun signUpNavigatge()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignObjects() // assign all the objects
        setOnClickListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { // context is a pointer to activity
            loginNavigate = context as loginNavigateInterface // this is where magic happens
        } catch (e: ClassCastException) {
            throw ClassCastException("$context exception thrown in onAttach within Login")
        }
    }

    private fun assignObjects() {
        userNameET = this.view?.findViewById(R.id.loginUserName) as EditText
        passwordET = this.view?.findViewById(R.id.loginPassword) as EditText
        loginButton = this.view?.findViewById(R.id.loginButton) as Button
        registerButton = this.view?.findViewById(R.id.signupButton) as Button
    }


    private fun setOnClickListeners() {
        loginButton.setOnClickListener {
            doAsync {
                login()
            }
        }

        registerButton.setOnClickListener {
            loginNavigate.signUpNavigatge()
        }
    }

    private fun login() {
        val userName = userNameET.text.toString()
        val password = Utils.md5(passwordET.text.toString().trim()) // simple password hash

        val userCheck = MainActivity.mUserViewModel.checkUserCreds(userName, password)
        if (userCheck > 0) {

            val userObjFull = MainActivity.mUserViewModel.getFullUser(userName)

            MainActivity.mUserViewModel.currentUser.postValue(userObjFull)
            loginNavigate.loginNavigate()
            MainActivity.LOGGED_IN = true
        } else {
            Toast.makeText(activity, "Incorrect User Name or Password", Toast.LENGTH_SHORT).show()

        }
    }
}