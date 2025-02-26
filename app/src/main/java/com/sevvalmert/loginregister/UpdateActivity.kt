package com.sevvalmert.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.sevvalmert.loginregister.databinding.ActivityUpdateBinding
import com.sevvalmert.loginregister.roomdb.UserDao
import com.sevvalmert.loginregister.roomdb.UserDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var userDao: UserDao
    val compositeDisposable = CompositeDisposable()
    private var userNameOrEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(applicationContext, UserDatabase::class.java, "User").build()
        userDao = db.userDao()
        userNameOrEmail = intent.getStringExtra("USERNAME") ?: intent.getStringExtra("EMAIL")

        if (userNameOrEmail.isNullOrEmpty()) {
            Toast.makeText(this, "Kullanıcı bilgisi bulunamadı!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }

    fun updateB(view: View) {
        val firstNameU = binding.isimUp.text.toString().trim()
        val lastNameU = binding.soyisimUp.text.toString().trim()
        val userNameU = binding.kullaniciAdiUp.text.toString().trim()
        val emailAdressU = binding.emailUp.text.toString().trim()
        val passwordU = binding.sifreUp.text.toString().trim()
        val phoneNumberU = binding.telNoUp.text.toString().trim()

        userNameOrEmail?.let { input ->
            compositeDisposable.add(
                userDao.getUserByUsername(input)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ user ->
                        val updatedFirstName = if (firstNameU.isNotEmpty()) firstNameU else user.firstName
                        val updatedLastName = if (lastNameU.isNotEmpty()) lastNameU else user.lastName
                        val updatedUserName = if (userNameU.isNotEmpty()) userNameU else user.userName
                        val updatedEmail = if (emailAdressU.isNotEmpty()) emailAdressU else user.emailAdress
                        val updatedPassword = if (passwordU.isNotEmpty()) passwordU else user.password
                        val updatedPhone = if (phoneNumberU.isNotEmpty()) phoneNumberU else user.phoneNumber

                        compositeDisposable.add(
                            userDao.updateUser(
                                updatedFirstName.toString(),
                                updatedLastName.toString(),
                                updatedUserName.toString(),
                                updatedEmail.toString(),
                                updatedPassword.toString(),
                                updatedPhone.toString(),
                                user.userName.toString(),
                                user.emailAdress.toString()
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    userNameOrEmail = updatedUserName

                                    val intent = Intent(this, MainActivity2::class.java)
                                    startActivity(intent)
                                    finish()
                                })
                        )
                    })
            )
        }
    }
}
