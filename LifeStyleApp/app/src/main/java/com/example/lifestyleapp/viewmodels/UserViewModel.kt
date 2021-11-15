package com.example.lifestyleapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lifestyleapp.MainActivity
import com.example.lifestyleapp.user.StepsEntity
import com.example.lifestyleapp.user.UserCreds
import com.example.lifestyleapp.user.UserEntity
import com.example.lifestyleapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserRepository = UserRepository(application)
    var currentUser: MutableLiveData<UserEntity>

    // TODO: figure out if this is the way to go
    // currently we are creating an empty user, and just updating the fields as we go.
    init { // initially create a user with empty fields
        println("INITIALIZING VIEW MODEL")
        val userEntityInitial = UserEntity("", "", "", "")
        this.currentUser = MutableLiveData<UserEntity>().default(userEntityInitial)
    }

    fun addUserToDB(user: UserEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user != null) {
                UserRepository.addUser(user)
            }
        }
    }

    fun checkUserCreds(userName: String, password: String): Int {
        return UserRepository.checkCredentials(userName, password)
    }


    // set the value of the live data user.
    //https://stackoverflow.com/questions/51305150/mutablelivedata-with-initial-value
    private fun <T : Any?> MutableLiveData<T>.default(initialValue: T) =
        apply { setValue(initialValue) }

    fun getUser(userName: String, password: String): String {
        val user: UserCreds = UserRepository.getId(userName, password)
        return user.userName
    }

    fun getFullUser(userObjID: String): UserEntity {
        return UserRepository.getUserByID(userObjID)
    }

    fun checkForUser(userName: String): Int {
        return UserRepository.getUserByUserName(userName)
    }

    fun addCreds(creds: UserCreds) {
        UserRepository.addNewCreds(creds)
    }

    fun updateUserInfoInDB() {
        UserRepository.updateUserInfo(MainActivity.mUserViewModel.currentUser.value)

    }

    fun addSteps(stepsEntity: StepsEntity){
        UserRepository.addSteps(stepsEntity)
    }

    fun updateSteps(stepsEntity: StepsEntity){
        UserRepository.updateSteps(stepsEntity)
    }

    fun uploadDBFile(){
        val context = getApplication<Application>().applicationContext
        UserRepository.uploadDBFile(context)
    }
}