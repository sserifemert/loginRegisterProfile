package com.sevvalmert.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.sevvalmert.loginregister.databinding.ActivityMain2Binding
import com.sevvalmert.loginregister.model.User
import com.sevvalmert.loginregister.roomdb.UserDao
import com.sevvalmert.loginregister.roomdb.UserDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private lateinit var userDao: UserDao
    val compositeDisposable = CompositeDisposable()
    private var userNameOrEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = Room.databaseBuilder(applicationContext, UserDatabase::class.java, "User").build()
        userDao = db.userDao()
        userNameOrEmail = intent.getStringExtra("USERNAME")
        userNameOrEmail= intent.getStringExtra("EMAIL")
    }

    fun delete (view: View) {
        compositeDisposable.add(
            userDao.getUserByUsername(userNameOrEmail.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    userDao.delete(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Toast.makeText(this, "Hesap başarıyla silindi!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }, { error ->
                            Toast.makeText(this, "Hata oluştu: ${error.message}", Toast.LENGTH_SHORT).show()
                        })
                }, {
                    Toast.makeText(this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show()
                })
        )


    }

}