package com.example.lifestyleapp.user

import androidx.room.*

@Entity(tableName = "user_data")
data class UserEntity(
    @PrimaryKey() val userName: String, // val because always want the same
    @ColumnInfo(name = "firstname") var firstName: String?,
    @ColumnInfo(name = "lastname") var lastName: String?,
    @ColumnInfo(name = "dob") var dob: String? = "",
    @ColumnInfo(name = "heightFt") var heightFt: String? = "",
    @ColumnInfo(name = "heightIn") var heightIn: String? = "",
    @ColumnInfo(name = "weight") var weight: String? = "",
    @ColumnInfo(name = "gender") var gender: String? = "",
    @ColumnInfo(name = "profPicPath") var profPicPath: String? = "",
    @ColumnInfo(name = "goal") var goal: String? = "",
    @ColumnInfo(name = "bmi") var bmi: String? = ""
) {

}

@Entity(tableName = "user_creds")
data class UserCreds(
    @PrimaryKey() val userName: String,
    @ColumnInfo(name = "password") var password: String
){

}

@Entity(tableName = "user_steps")
data class StepsEntity(
    @PrimaryKey() val userName: String,
    @ColumnInfo(name = "password") var steps: Int
) {
}

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey() val userName: String,
    @ColumnInfo(name = "weather") var weather: String
) {
}