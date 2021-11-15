package com.example.lifestyleapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity

import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.lifestyleapp.user.StepsEntity
import com.example.lifestyleapp.user.UserEntity
import com.example.lifestyleapp.viewmodels.UserViewModel
import com.example.lifestyleapp.fragments.*
import com.example.lifestyleapp.viewmodels.WeatherViewModel
import org.jetbrains.anko.doAsync


enum class Goal {
    GAIN, LOSE, MAINTAIN, UNDEFINED
}

class MainActivity : AppCompatActivity(), userInfoFragment.UserInfoOnDataPass,
    MainFragment.MainFragOnDataPass, GoalsFragment.GoalsOnDataPass, LoginFragment.loginNavigateInterface,
    Register_Fragment.registerNavigateInterface, SensorEventListener,
    GestureOverlayView.OnGesturePerformedListener {

    // fragments
    private var userInfoFragment: userInfoFragment = userInfoFragment()
    private var mainFragment: MainFragment = MainFragment()
    private var weatherFragment: WeatherFragment = WeatherFragment()
    private var goalsFragment: GoalsFragment = GoalsFragment()
    private var calorieCalcFragment: CalorieCalcFragment = CalorieCalcFragment()
    private var statsFragment: statsFragment = statsFragment()
    private var loginFragment: LoginFragment = LoginFragment()
    private var registerFragment: Register_Fragment = Register_Fragment()
    private var stepsFragment: StepFragment = StepFragment()
    private lateinit var gestureLibrary: GestureLibrary
    private lateinit var mediaPlayer: MediaPlayer

    var MAIN_TAG = "Main_Fragment"
    var USER_INFO_TAG = "UserInfo_Fragment"
    var DIET_TAG = "Diet_Fragment"
    var GOALS_TAG = "Goals_Fragment"
    var WEATHER_TAG = "Weather_Fragment"
    val STATS_TAG = "Stats_Fragment"
    val LOGIN_TAG = "Login_Fragment"
    val REGISTER_TAG = "Register_Fragment"
    val STEPS_TAG = "Steps_Fragment"

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var running = false

    companion object {
        lateinit var mUserViewModel: UserViewModel
        lateinit var weatherViewModel: WeatherViewModel
        lateinit var fTrans: FragmentTransaction
        var LOGGED_IN: Boolean = false
        var currentSteps: MutableLiveData<Int> = MutableLiveData(0)
        var totalSteps = 0f
        var previousTotalSteps = 0f
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.tool_bar))

        // calling the action bar
        // if its not null, show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // initialize the view model
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        if (savedInstanceState == null) { // opened for the first time
            replaceFragment(loginFragment, LOGIN_TAG, R.id.main_frag_placeholder)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1
            )
        }
        if(!isTablet()){
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            createGesture()
        }else{
            replaceFragment(mainFragment, MAIN_TAG, R.id.left_frag_placeholder) // replace with main frag
        }

        // amplify s3 stuff
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)
            Log.i("OnCreateWithinMainActivity", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("OnCreateWithinMainActivity", "Could not initialize Amplify", error)
        }
    }

    private fun createGesture() {
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)
        if (!gestureLibrary.load()) {
            finish()
        }
        val gestureOverlay = findViewById<GestureOverlayView>(R.id.gestureOverlay)
        gestureOverlay.addOnGesturePerformedListener(this)
    }

    override fun onGesturePerformed(overlay: GestureOverlayView?, gesture: Gesture?) {
        val predictions = gestureLibrary.recognize(gesture)
        predictions?.let {
            if (it.size > 0 && it[0].score > 1.0) {
                val gestureName = it[0].name
                if(gestureName == "star_step_counter"){
                    mediaPlayer = MediaPlayer.create(this, R.raw.start)
                    running = true
                    mediaPlayer.start()
                }
                if(gestureName == "stop_step_counter"){
                    mediaPlayer = MediaPlayer.create(this, R.raw.stop)
                    running = false
                    mediaPlayer.start()
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String, resID: Int) {
        doAsync{weatherViewModel.repository.updateWeather()}
        doAsync{mUserViewModel.updateSteps(StepsEntity(mUserViewModel.currentUser.value?.userName!!,
            currentSteps.value!!
        ))}
        fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(resID, fragment, tag)
        fTrans.commit()
    }


    // overriding the back buttons function
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            android.R.id.home -> { // home button == back button
                println("Clicked home button ${item.itemId}")
                if (!isTablet() && LOGGED_IN) {
                    replaceFragment(mainFragment, MAIN_TAG, R.id.main_frag_placeholder)
                } else if (!LOGGED_IN) {
                    replaceFragment(loginFragment, LOGIN_TAG, R.id.main_frag_placeholder)
                } else {
                    //replaceFragment(mainFragment, MAIN_TAG, R.id.main_frag_placeholder)
                }
                return true
            }
            R.id.menu_logout -> {
                loginFragment = LoginFragment()
                replaceFragment(loginFragment, "login", R.id.main_frag_placeholder)
                LOGGED_IN = false
                mUserViewModel.currentUser.value = UserEntity("", "", "")
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun userInfoOnDataPass(data: UserEntity?) {
        if (isTablet()) {
            replaceFragment(statsFragment, STATS_TAG, R.id.main_frag_placeholder)
        } else {
            replaceFragment(mainFragment, MAIN_TAG, R.id.main_frag_placeholder)
        }
    }

    override fun goalsOnDataPass(goal: Goal) {
        var userGoal = ""
        if (goal == Goal.GAIN) userGoal = "gain"
        if (goal == Goal.LOSE) userGoal = "lose"
        if (goal == Goal.MAINTAIN) userGoal = "maintain"
        if (goal == Goal.UNDEFINED) userGoal = "undefined"

        mUserViewModel.currentUser.value?.goal = userGoal
        mUserViewModel.currentUser.postValue(mUserViewModel.currentUser.value) // hacky
        if (isTablet()) {
            replaceFragment(statsFragment, STATS_TAG, R.id.main_frag_placeholder)
        } else {
            replaceFragment(mainFragment, MAIN_TAG, R.id.main_frag_placeholder)
        }
    }


    override fun navigate(button: String) {
        when (button) {
            "weather" -> {
                replaceFragment(weatherFragment, WEATHER_TAG, R.id.main_frag_placeholder)
            }
            "userInfo" -> {
                replaceFragment(userInfoFragment, USER_INFO_TAG, R.id.main_frag_placeholder)
            }
            "diet" -> {
                replaceFragment(calorieCalcFragment, DIET_TAG, R.id.main_frag_placeholder)
            }
            "goals" -> {
                replaceFragment(goalsFragment, GOALS_TAG, R.id.main_frag_placeholder)
            }
            "steps" -> {
                replaceFragment(stepsFragment, STEPS_TAG, R.id.main_frag_placeholder)
                if(!running){
                    Toast.makeText(this, "Sensor turned off", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    override fun loginNavigate() {
        if(!isTablet()){
            replaceFragment(mainFragment, MAIN_TAG, R.id.main_frag_placeholder)
        }else{
            replaceFragment(statsFragment, STATS_TAG, R.id.main_frag_placeholder)
        }
    }

    override fun userInfoNavigate() {
        replaceFragment(userInfoFragment, USER_INFO_TAG, R.id.main_frag_placeholder)
    }

    override fun signUpNavigatge() {
        replaceFragment(registerFragment, REGISTER_TAG, R.id.main_frag_placeholder)
    }

    override fun onResume() {
        super.onResume()
        running = true
        if (stepSensor == null) {
            Toast.makeText(this, "No Step Sensor Detected", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]

            currentSteps.value = totalSteps.toInt() - previousTotalSteps.toInt()
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onPause() {
        super.onPause() // could do this asyc???
        mUserViewModel.uploadDBFile() // on pause upload all data to the cloud
    }
}






