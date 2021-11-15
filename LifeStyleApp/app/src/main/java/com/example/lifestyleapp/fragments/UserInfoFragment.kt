package com.example.lifestyleapp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.R
import com.example.lifestyleapp.user.UserEntity
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileOutputStream
import java.util.*


private const val REQUEST_CODE = 42

class userInfoFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    // all the stuff
    private lateinit var fNameET: EditText
    private lateinit var lNameET: EditText
    private lateinit var dobET: ImageButton // date of birth
    private lateinit var dobText: TextView
    private lateinit var heightFtET: EditText
    private lateinit var heightInchET: EditText
    private lateinit var weightET: EditText
    private lateinit var mfRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var profilePicButton: Button
    private lateinit var submitUserInfoButton: Button
    private lateinit var userProfilePic: ImageView


    private lateinit var takenImage: Bitmap

    // data passer interface
    var mDataPasser: UserInfoOnDataPass? = null

    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0


    interface UserInfoOnDataPass {
        fun userInfoOnDataPass(data: UserEntity?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignObjects() // assign all the objects
        setOnClickListeners()
        pickDate()
        MainActivity.mUserViewModel.currentUser.observe(viewLifecycleOwner) {
            setUserVals()
        }
    }

    private fun setUserVals() {

        fNameET.setText(MainActivity.mUserViewModel.currentUser.value?.firstName)
        lNameET.setText(MainActivity.mUserViewModel.currentUser.value?.lastName)
        dobText.text = MainActivity.mUserViewModel.currentUser.value?.dob
        heightFtET.setText(MainActivity.mUserViewModel.currentUser.value?.heightFt)
        heightInchET.setText(MainActivity.mUserViewModel.currentUser.value?.heightIn)
        weightET.setText(MainActivity.mUserViewModel.currentUser.value?.weight)

        if (MainActivity.mUserViewModel.currentUser.value?.gender == "Male") {

            mfRadioGroup.check(R.id.radioButtonMale)
        } else {
            mfRadioGroup.check(R.id.userInfoFemaleRadio)
        }
        if (MainActivity.mUserViewModel.currentUser.value?.profPicPath != "") {
            setProfPic()
        }
    }

    private fun assignObjects() {
        fNameET = this.view?.findViewById(R.id.userInfoFirstName) as EditText
        lNameET = this.view?.findViewById(R.id.userInfoLastName) as EditText
        dobET = this.view?.findViewById(R.id.userInfoDatePickerButton) as ImageButton
        dobText = this.view?.findViewById(R.id.dob) as TextView
        heightFtET = this.view?.findViewById(R.id.userInfoHeightFeet) as EditText
        heightInchET = this.view?.findViewById(R.id.userInfoHeightInches) as EditText
        weightET = this.view?.findViewById(R.id.userInfoWeightLbs) as EditText
        mfRadioGroup = this.view?.findViewById(R.id.gender_radio_group) as RadioGroup
        maleRadioButton = this.view?.findViewById(R.id.radioButtonMale) as RadioButton
        femaleRadioButton = this.view?.findViewById(R.id.userInfoFemaleRadio) as RadioButton
        profilePicButton = this.view?.findViewById(R.id.userInfoTakePicButton) as Button
        userProfilePic = this.view?.findViewById(R.id.userImage) as ImageView
        submitUserInfoButton = this.view?.findViewById(R.id.userInfoSubmitButton) as Button
    }

    // context is point to activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("on attach user info")
        try { // context is a pointer to activity
            mDataPasser = context as UserInfoOnDataPass // this is where magic happens
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }

    // onclick implementation
    private fun setOnClickListeners() {
        // submit info button
        submitUserInfoButton.setOnClickListener() {
            updateUserAndNavigate()

        }

        // profile pic button
        profilePicButton.setOnClickListener() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }
    }

    fun updateUserAndNavigate() {
        updateUser() // collect input
        if (MainActivity.mUserViewModel.currentUser.value?.firstName == "") {
            Toast.makeText(activity, "Please Enter First Name", Toast.LENGTH_SHORT)
                .show()
        }
        if (MainActivity.mUserViewModel.currentUser.value?.lastName == "") {
            Toast.makeText(activity, "Please Enter Last Name", Toast.LENGTH_SHORT)
                .show()
        } else { // navigate
            mDataPasser?.userInfoOnDataPass(MainActivity.mUserViewModel.currentUser.value) // do we even need to do this?
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            takenImage = data?.extras?.get("data") as Bitmap
            MainActivity.mUserViewModel.currentUser.value?.profPicPath =
                saveImage(takenImage) // save the image, store the file path
            userProfilePic.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateUser() {
        MainActivity.mUserViewModel.currentUser.value?.firstName = fNameET.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.lastName = lNameET.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.dob = dobText.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.heightFt = heightFtET.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.heightIn =
            heightInchET.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.weight = weightET.text.toString().trim()
        MainActivity.mUserViewModel.currentUser.value?.gender = returnGender().trim()
        MainActivity.mUserViewModel.currentUser.postValue(MainActivity.mUserViewModel.currentUser.value) // hacky

        // Update the User in the DB
        doAsync {
            MainActivity.mUserViewModel.updateUserInfoInDB()
        }
    }


    private fun returnGender(): String {
        var gender = ""
        if (mfRadioGroup.checkedRadioButtonId == -1) { // no radio buttons are checked
            gender += "No Gender Selected"
        } else { // TODO: Should add other option
            // one of the radio buttons is checked
            if (maleRadioButton.isChecked) {
                gender += "Male"
            } else if (femaleRadioButton.isChecked) {
                gender += "Female"
            }
        }
        return gender
    }


    private fun saveImage(finalBitmap: Bitmap): String {
        val root: String? = this.context?.getExternalFilesDir(null)?.getAbsolutePath()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "profile_pic_$timeStamp.jpg"

        val path = myDir.path + "/" + fname
        println("\n\n PATH $path")
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            println("Failed to save bitmap to file. Exception thrown")
            e.printStackTrace()
        }
        return path
    }

    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        dobET.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month + 1
        savedYear = year
        val formatted = "$savedMonth / $savedDay / $savedYear"
        dobText.text = formatted
    }


    private fun setProfPic() {
        val imagePath = MainActivity.mUserViewModel.currentUser.value?.profPicPath
        if (imagePath != null && imagePath != "") {
            try {
                val bitMapImg = BitmapFactory.decodeFile(imagePath)
                if (bitMapImg != null) {
                    println("Updating user image")
                    userProfilePic.setImageBitmap(
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

}

