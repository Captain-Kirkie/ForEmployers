package com.example.lifestyleapp.user

import androidx.lifecycle.LiveData
import androidx.room.*

import androidx.room.Dao


@Dao
interface UserDao {
    // insert a user
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addCreds(creds: UserCreds)

    @Insert()
    fun addWeather(weather: WeatherEntity)

    @Insert()
    fun addSteps(steps: StepsEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Update
    fun updateSteps(steps: StepsEntity)

    @Update
    fun updateWeather(weather: WeatherEntity)

    @Query("SELECT * FROM user_data")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_data WHERE userName = :userId")
    fun getUser(userId: String): UserEntity //todo this should be live data but won't let me return one...

    @Query("SELECT COUNT() FROM user_data WHERE userName = :id")
    fun count(id: Int): Int

    @Query("SELECT COUNT() FROM user_creds WHERE userName = :userName AND password = :password")
    fun userLogin(userName : String, password: String): Int

    @Query("SELECT * FROM user_creds WHERE userName = :userName AND password = :password ")
    fun getUserCreds(userName: String, password: String): UserCreds

    @Query("SELECT COUNT() FROM user_creds WHERE userName = :userName")
    fun getUserByUserName(userName: String): Int

}