package com.example.lifestyleapp.fragments


import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R


import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.ClassCastException
import kotlin.math.pow

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    private lateinit var weatherButton: Button
    private lateinit var goalsButton: Button
    private lateinit var dietButton: Button
    private lateinit var hikeButton: Button
    private lateinit var userInfoButton: Button
    private lateinit var stepsButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var onDataPass: MainFragOnDataPass
    private lateinit var bmiTextView: TextView

    private lateinit var profilePicture: ImageView

    interface MainFragOnDataPass {
        fun navigate(button: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    // Bundle is saved data. Will be null on startup, but when user inputs
    // information it will contain info, should check for null and assign variables as necessary
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignValues()
        setOnClickListeners()

        if (savedInstanceState == null) {
            setTextViews()
        }

        // set observer
        MainActivity.mUserViewModel.currentUser.observe(viewLifecycleOwner) {
            setTextViews()
        }
    }

    private fun setTextViews() {
        setDobTextView()
        setMainNameDisplay()
        calculateBMI()
        setProfPic()
        setGoalTextView()
        setCurrentWeightTextView()
        setDobTextView()
        setGenderTextView()
    }

    private fun assignValues() {
        // assign the buttons
        weatherButton = this.view?.findViewById(R.id.button_weather) as Button
        goalsButton = this.view?.findViewById(R.id.button_goals) as Button
        dietButton = this.view?.findViewById(R.id.button_diet) as Button
        hikeButton = this.view?.findViewById(R.id.button_hikes) as Button
        userInfoButton = this.view?.findViewById(R.id.button_user_info) as Button
        stepsButton = this.view?.findViewById(R.id.button_steps) as Button
        bmiTextView = this.view?.findViewById(R.id.bmi_textview) as TextView
        profilePicture = this.view?.findViewById(R.id.profilePicture) as ImageView
    }

    private fun setOnClickListeners() {
        // assign onclick listeners
        weatherButton.setOnClickListener() {
            Toast.makeText(activity, "You have clicked the Weather", Toast.LENGTH_SHORT)
                .show()
            onDataPass.navigate("weather")

            //findNavController().navigate(R.id.action_MainFragment_to_WeatherFragment) // go to weather fragment
        }
        goalsButton.setOnClickListener() {
            Toast.makeText(activity, "You have clicked the GOALS", Toast.LENGTH_SHORT)
                .show()
            onDataPass.navigate("goals")
        }
        dietButton.setOnClickListener() {
            Toast.makeText(activity, "You have clicked the DIET", Toast.LENGTH_SHORT)
                .show()
            onDataPass.navigate("diet")
        }
        hikeButton.setOnClickListener() {
            Toast.makeText(activity, "You have clicked the HIKE", Toast.LENGTH_SHORT)
                .show()
            openGoogleMaps() //map stuff
        }
        userInfoButton.setOnClickListener() {
            onDataPass.navigate("userInfo")
//            findNavController().navigate(R.id.action_MainFragment_to_UserFragment) // navigate to user frag
        }
        stepsButton.setOnClickListener() {
            onDataPass.navigate("steps")
        }
    }


    private fun setDobTextView() {
        val dobTV = this.view?.findViewById(R.id.dob_textview) as TextView
        val formatted = "Birthday: ${MainActivity.mUserViewModel.currentUser.value?.dob}"
        dobTV.text = formatted
    }

    private fun setCurrentWeightTextView() {
        val weightTV = this.view?.findViewById(R.id.weight_textview) as TextView
        val formatted = "Current Weight: ${MainActivity.mUserViewModel.currentUser.value?.weight}"
        weightTV.text = formatted
    }

    private fun setGoalTextView() {
        val goalTV = this.view?.findViewById(R.id.goal_textview) as TextView
        val formatted = "Goal: ${MainActivity.mUserViewModel.currentUser.value?.goal}"
        goalTV.text = formatted
    }


    private fun setMainNameDisplay() {
        val userName = this.view?.findViewById(R.id.user_full_name) as TextView
        userName.text = MainActivity.mUserViewModel.currentUser.value?.firstName
    }


    private fun setGenderTextView() {
        val genderTV = this.view?.findViewById(R.id.gender_textview) as TextView
        val formatted = "Gender: ${MainActivity.mUserViewModel.currentUser.value?.gender}"
        genderTV.text = formatted
    }


    private fun setProfPic() {
        val imagePath = MainActivity.mUserViewModel.currentUser.value?.profPicPath
        if (imagePath != "") {
            try {
                var bitMapImg = BitmapFactory.decodeFile(imagePath)
                bitMapImg = getRoundedCornerBitmap(bitMapImg, 100)
                if (bitMapImg != null) {
                    println("Updating user image")
                    profilePicture.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            bitMapImg,
                            500,
                            500,
                            true
                        )
                    )
                } else {
                    println("Bitmap profile pic is null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Exception thrown while trying to set profile pic")
            }
        }
    }
    //https://stackoverflow.com/questions/18229358/bitmap-in-imageview-with-rounded-corners
    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap
                .height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
    // context is point to activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { // context is a pointer to activity
            onDataPass = context as MainFragOnDataPass // this is where magic happens
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }

    private fun openGoogleMaps() {
        val mapIntentUri = Uri.parse("geo:0,0?q=hiking")
        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)
        startActivity(mapIntent)
    }


    fun calculateBMI() {
        var feetStr = MainActivity.mUserViewModel.currentUser.value?.heightFt
        var inchesStr = MainActivity.mUserViewModel.currentUser.value?.heightIn
        var weightStr = MainActivity.mUserViewModel.currentUser.value?.weight

        // error checking
        if (feetStr == "") {
            feetStr = "0"
        }

        if (inchesStr == "") {
            inchesStr = "0"
        }

        if (weightStr == "") {
            weightStr = "0"
        }

        val feet = feetStr?.toInt()
        val inches = inchesStr?.toInt()
        val weight = weightStr?.toInt()
        var height = 0.0
        if (feet != null && inches != null) {
            height = convertToInches(feet, inches)
        }
        val bmi = weight?.let { calculateBMI(height, it) }
        var bmiStr = ""
        if (bmi != null) {
            if (bmi <= 0 || bmi.isNaN()) {
                bmiStr = "Undefined"
            } else {
                bmiStr = "%.2f".format(bmi)
            }
        }

        val formatted = "BMI: $bmiStr"
        bmiTextView.text = formatted
        MainActivity.mUserViewModel.currentUser.value?.bmi = bmiStr
    }


    private fun convertToInches(feet: Int, inches: Int): Double {
        return inches + (feet * 12).toDouble()
    }


    private fun calculateBMI(height: Double, weight: Int): Double {
        if (height <= 0 || weight <= 0) {
            return -1.0
        }
        return 703 * weight / height.pow(2)
    }

}