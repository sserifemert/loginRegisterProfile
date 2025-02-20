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
import com.sevvalmert.loginregister.databinding.ActivityMainBinding
import com.sevvalmert.loginregister.roomdb.UserDao
import com.sevvalmert.loginregister.roomdb.UserDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var userDao : UserDao
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Room.databaseBuilder(applicationContext, UserDatabase::class.java, "User").build()
        userDao = db.userDao()
    }

    fun giris(view: View){

        val userName = binding.emailText.text.toString()
        val emailU = binding.emailText.text.toString()
        val sifreU = binding.sifreText.text.toString()

        if(emailU.isEmpty() || sifreU.isEmpty()){
            Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
        }
        else{
            compositeDisposable.add(
                userDao.checkUserExistsAndTrue(userName,emailU, sifreU)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ count ->
                        if (count == 1) {
                            val intent = Intent(this,MainActivity2::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this,"Kullanıcı adınızı veya şifrenizi kontrol ediniz",Toast.LENGTH_LONG).show()
                        }
                    })
            )
        }
    }

    fun hesapOlustur(view:View)
    {
        val intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}