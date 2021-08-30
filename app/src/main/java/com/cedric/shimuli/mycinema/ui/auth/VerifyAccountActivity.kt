package com.cedric.shimuli.mycinema.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.databinding.ActivityVerifyAccountBinding
import com.cedric.shimuli.mycinema.model.RegisterResponse
import com.cedric.shimuli.mycinema.network.RestCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyAccountActivity : AppCompatActivity() {
    private lateinit var binding:ActivityVerifyAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val userId = Paper.book().read<String>("userId")
//        val code = Paper.book().read<String>("code")
//        val phone = Paper.book().read<String>("phone")

        val userId=intent.getStringExtra("userId")
        val code=intent.getStringExtra("code")
        val phone=intent.getStringExtra("phone")

        val test = userId+code+phone
        binding.info.setText("Enter the code sent to $phone to verify your account")
        binding.verifyBtn.setOnClickListener(View.OnClickListener {
            verifyInput(userId, phone)
        })
    }

    private fun verifyInput(userId: String?, phone: String?) {
        when {
            binding.code.text.isNullOrEmpty() -> {
                binding.code.error = "Code cannot be empty!"
            }
            else->{
                val code:String = binding.code.text.toString()
                verifyNumber(code, userId, phone)
            }
        }
    }

    private fun verifyNumber(code: String, userId: String?, phone: String?) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()

        if (userId != null) {
            if (phone != null) {
                RestCall.client.verifyNumber(userId,phone,code)?.enqueue(
                    object:Callback<RegisterResponse?>{
                        override fun onResponse(
                            call: Call<RegisterResponse?>,
                            response: Response<RegisterResponse?>) {
                            if (response.code()==200){
                                if (response.body()?.message.toString().contains("successfully")){
                                    pDialog.dismiss()
                                    SweetAlertDialog(this@VerifyAccountActivity, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Number was verified successfully!")
                                        .setConfirmText("Log in")
                                        .setConfirmClickListener {
                                            val intent = Intent(this@VerifyAccountActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .show()
                                }
                                else{
                                    pDialog.dismiss()
                                    SweetAlertDialog(this@VerifyAccountActivity, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Verified!")
                                        .setContentText("This number was verified before")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                        .show()
                                }
                            }
                            else{
                                pDialog.dismiss()
                                SweetAlertDialog(this@VerifyAccountActivity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Failed!")
                                    .setContentText("Something went wrong")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                            pDialog.dismiss()
                            Toast.makeText(this@VerifyAccountActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                            Log.d("RETROFIT", "ERROR: " + t.message)
                            SweetAlertDialog(this@VerifyAccountActivity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Something went wrong")
                                .setConfirmText("Ok")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                            TODO("Not yet implemented")
                        }

                    }
                )
            }
        }

    }
}