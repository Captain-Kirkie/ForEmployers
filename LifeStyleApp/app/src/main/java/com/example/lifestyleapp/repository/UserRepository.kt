package com.example.lifestyleapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amplifyframework.core.Amplify
import com.example.lifestyleapp.user.StepsEntity
import com.example.lifestyleapp.user.UserCreds
import com.example.lifestyleapp.user.UserDao
import com.example.lifestyleapp.user.UserEntity
import com.example.lifestyleapp.database.UserDatabase
import java.io.File

// https://stackoverflow.com/questions/55371861/android-repository-pattern-what-is-the-best-solution-to-create-an-object
// this is a singleton class
object UserRepository {

    private lateinit var userDao: UserDao

    operator fun invoke(context: Context): UserRepository { // this is like get instance
        userDao = UserDatabase.getDatabase(context).userDao()
        return this
    }

    suspend fun addUser(user: UserEntity) {
        userDao.addUser(user)
    }

    fun checkCredentials(userName: String, password: String): Int {
        return userDao.userLogin(userName, password)
    }

    fun getId(userName: String, password: String): UserCreds {
        return userDao.getUserCreds(userName, password)
    }

//    suspend fun getUser(user: UserEntity){
//        userDao.getUser()
//    }

    fun getUserByID(userName: String): UserEntity {
        return userDao.getUser(userName)
    }

    fun getUserByUserName(userName: String): Int {
        userName.trim()
        return userDao.getUserByUserName(userName)
    }

    fun addNewCreds(creds: UserCreds) {
        userDao.addCreds(creds)
    }

    fun updateUserInfo(user: UserEntity?) {
        if (user != null) {
            userDao.updateUser(user)
        }
    }

    fun addSteps(stepsEntity: StepsEntity) {
        userDao.addSteps(stepsEntity)
    }

    fun updateSteps(stepsEntity: StepsEntity) {
        userDao.updateSteps(stepsEntity)
    }

    // a way to upload the file
    @SuppressLint("SdCardPath")
    fun uploadDBFile(appContext: Context) {
        val path = appContext.getDatabasePath("database_1").toString()

        val uploadedFile = File(path)

        // upload db file
        if (uploadedFile.exists()) {
            Amplify.Storage.uploadFile("Full_Data_Base", uploadedFile,
                { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
                { Log.e("MyAmplifyApp", "Upload failed", it) }
            )
        }

        // upload awss3transfertable.db-journal file
        val path2 = path.replace("database_1", "user_database-shm")
        val uploadFile2 = File(path2)
        if (uploadFile2.exists()) {
            Amplify.Storage.uploadFile("user_database-shm", uploadFile2,
                { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
                { Log.e("MyAmplifyApp", "Upload failed", it) }
            )
        }

        // upload user_database-shm
        val path3 = path.replace("user_database-shm", "awss3transfertable.db-journal")
        val uploadFile3 = File(path3)
        if (uploadFile3.exists()) {
            Amplify.Storage.uploadFile("awss3transfertable.db-journal", uploadFile3,
                { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
                { Log.e("MyAmplifyApp", "Upload failed", it) }
            )
        }
    }

//    suspend fun updateUser(user: UserEntity) {
//        userDao.updateUser(user)
//    }
}