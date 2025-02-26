package com.sevvalmert.loginregister.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sevvalmert.loginregister.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {

    @Insert()
    fun insert (user : User) : Completable

    @Query("SELECT COUNT(*) FROM User WHERE user_name = :userName OR email_adress = :email")
    fun checkUserExists(userName: String, email: String): Single<Int>

    @Query("SELECT COUNT(*) FROM User WHERE (user_name = :userName OR email_adress = :email) AND password = :password_")
    fun checkUserExistsAndTrue(userName : String , email : String , password_ : String): Single<Int>

    @Query("SELECT * FROM User WHERE user_name = :input OR email_adress = :input LIMIT 1")
    fun getUserByUsername(input: String): Single<User>

    @Query("UPDATE User SET first_name = :firstName, last_name = :lastName, user_name = :userName, email_adress = :emailAdress, password = :Password, phone_number = :phoneNumber WHERE user_name = :oldUserName OR email_adress = :oldEmail")
    fun updateUser(
        firstName: String,
        lastName: String,
        userName: String,
        emailAdress: String,
        Password: String,
        phoneNumber: String,
        oldUserName: String,
        oldEmail: String
    ): Completable

    @Delete()
    fun delete (user : User) : Completable

}