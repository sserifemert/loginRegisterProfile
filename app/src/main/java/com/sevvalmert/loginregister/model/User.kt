package com.sevvalmert.loginregister.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (

    @ColumnInfo(name ="first_name")
    val firstName : String?,

    @ColumnInfo(name = "last_name")
    val lastName : String?,

    @ColumnInfo(name = "user_name")
    val userName : String?,

    @ColumnInfo(name = "email_adress")
    val emailAdress : String?,

    @ColumnInfo(name = "password")
    val password : String?,

    @ColumnInfo(name = "phone_number")
    val phoneNumber : Int?){

    @PrimaryKey(autoGenerate = true)
    var id =0
}
