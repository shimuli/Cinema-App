package com.cedric.shimuli.mycinema

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cedric.shimuli.mycinema.databinding.ActivityMainBinding
import com.cedric.shimuli.mycinema.ui.auth.LoginActivity
import com.cedric.shimuli.mycinema.ui.home.DashboardActivity
import com.cedric.shimuli.mycinema.utils.Constants
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var preferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getSharedPreferences(Constants.REF_VALUE, MODE_PRIVATE)
        Paper.init(this)
    }

    override fun onResume() {
        super.onResume()
        val SPLASH_TIME = 2000
        if (preferences!!.getBoolean("first", true)) {
            Handler().postDelayed({
                val empty = ""
                Paper.book().write("token", empty)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }, SPLASH_TIME.toLong())
            preferences!!.edit().putBoolean("first", false).apply()
        } else {
            Handler().postDelayed({ checkSession() },SPLASH_TIME.toLong())
        }
    }

    private fun checkSession() {
        val myToken = Paper.book().read<Any>("token").toString()

        if (myToken.isEmpty()) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
            finish()
        }
        finish()
    }
}