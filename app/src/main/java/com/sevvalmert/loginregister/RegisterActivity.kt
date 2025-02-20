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
import com.sevvalmert.loginregister.databinding.ActivityRegisterBinding
import com.sevvalmert.loginregister.model.User
import com.sevvalmert.loginregister.roomdb.UserDao
import com.sevvalmert.loginregister.roomdb.UserDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private lateinit var userDao : UserDao
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(applicationContext,UserDatabase::class.java,"User").build()
        userDao = db.userDao()

    }

    fun olustur(view: View)
    {

        val nameU = binding.isimR.text.toString()
        val lastU = binding.soyisimR.text.toString()
        val userName = binding.kullaniciAdiR.text.toString()
        val emailU = binding.emailR.text.toString()
        val sifreU = binding.sifreR.text.toString()
        val telefonU = binding.telNoR.text.toString()

        if (nameU.isEmpty() || lastU.isEmpty() || userName.isEmpty() || emailU.isEmpty() || sifreU.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
        }
        else if(telefonU.length != 10 || telefonU.isEmpty() || !telefonU.matches("\\d+".toRegex())) {
            Toast.makeText(this,"Telefon numarası boş bırakılmamalı ve 10 haneli olmalıdır", Toast.LENGTH_LONG).show()
        }
        else{
            val user = User(
                firstName = nameU,
                lastName = lastU,
                userName = userName,
                emailAdress = emailU,
                password = sifreU,
                phoneNumber = telefonU.toIntOrNull() ?: 0
            )

            compositeDisposable.add(
                userDao.checkUserExists(userName, emailU)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ count ->
                        if (count > 0) {
                            Toast.makeText(this, "Bu kullanıcı adı veya e-posta zaten kayıtlı!", Toast.LENGTH_SHORT).show()
                        } else {
                            addUserToDatabase(user)
                        }
                    })
            )

        }

    }


    private fun addUserToDatabase(user:User) {
        compositeDisposable.add(
            userDao.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({val intent = Intent(this,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)})
        )
    }

    override fun  onDestroy(){
        super.onDestroy()

        compositeDisposable.clear()
    }

}