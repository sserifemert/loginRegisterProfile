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


    @Delete()
    fun delete (user : User) : Completable

}