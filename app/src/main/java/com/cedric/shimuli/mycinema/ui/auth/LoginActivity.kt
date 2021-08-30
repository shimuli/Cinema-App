package com.cedric.shimuli.mycinema.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.databinding.ActivityLoginBinding
import com.cedric.shimuli.mycinema.model.LoginResponse
import com.cedric.shimuli.mycinema.network.RestCall
import com.cedric.shimuli.mycinema.ui.home.DashboardActivity
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        binding.loginBtn.setOnClickListener(View.OnClickListener {
            validateData()
        })
        binding.signUpText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
       // val phone=intent.getStringExtra("phone")
    }

    private fun validateData() {
        when {
            binding.phone.text.isNullOrEmpty() -> {
                binding.phone.error = "Phone number is required please!"
            }
            binding.password.text.isNullOrEmpty() -> {
                binding.password.error = "LPassword is required please!"
            }
            else->{
                val phone:String = binding.phone.text.toString()
                val password:String = binding.password.text.toString()

                loginMethod(phone,password )
            }
        }
    }

    private fun loginMethod(phone: String, password: String) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Logging in"
        pDialog.setCancelable(false)
        pDialog.show()
        RestCall.client.loginIn(phone, password)
            ?.enqueue(object :Callback<LoginResponse?>{
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>) {
                    if (response.code() == 200){
                        pDialog.dismiss()
                        Toast.makeText(this@LoginActivity, "Login was successful", Toast.LENGTH_LONG).show()
                        Paper.book().write("userId", response.body()?.userId.toString())
                        Paper.book().write("token", "Bearer "+ response.body()?.accessToken.toString())

                        Paper.book().write("userName", response.body()?.userName.toString())
                        Paper.book().write("userPhone", response.body()?.userPhone.toString())

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else if(response.code() == 401){
                        pDialog.dismiss()
                        SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Invalid!")
                            .setContentText("Phone or Password is Invalid!")
                            .setConfirmText("Ok")
                            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                            .show()
                    }
                    else{
                        pDialog.dismiss()
                        Toast.makeText(this@LoginActivity, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    pDialog.dismiss()
                    Log.e("Login Error ", "Error is : " + t.message)
                    Toast.makeText(this@LoginActivity, "Failed, please try again", Toast.LENGTH_LONG).show()
                }
            })
    }
}