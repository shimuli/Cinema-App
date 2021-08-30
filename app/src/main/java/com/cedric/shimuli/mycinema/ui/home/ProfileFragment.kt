package com.cedric.shimuli.mycinema.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.databinding.FragmentProfileBinding
import com.cedric.shimuli.mycinema.model.UserModel
import com.cedric.shimuli.mycinema.network.RestCall
import com.cedric.shimuli.mycinema.ui.auth.LoginActivity
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProfileFragment : Fragment() {
    private  var binding: FragmentProfileBinding? = null
    private val _binding get() = binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding!!.logout.setOnClickListener(View.OnClickListener {
            SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirm Logout")
                .setContentText("Are you sure you want to logout?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener {
                    val empty = ""
                    Paper.book().write("token", empty)
                    Paper.book().write("userPhone", empty)
                    Paper.book().write("userName", empty)
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()
                }
                .setCancelClickListener { sDialog -> sDialog.cancel() }
                .show()
        })

        //getProfile()
        getUser()
        return binding!!.root
    }

    private fun getUser() {
        val name = Paper.book().read<String>("userName")
        val phone = Paper.book().read<String>("userPhone")

        binding?.userName?.setText(name)
        binding?.phone?.setText(phone)

        binding!!.mMaterialLetterIcon.isInitials = true
        binding!!.mMaterialLetterIcon.initialsNumber = 2
        binding!!.mMaterialLetterIcon.letterSize = 25
        binding!!.mMaterialLetterIcon.shapeColor = resources.getColor(R.color.blue)
        binding!!.mMaterialLetterIcon.letter = name
    }

    private fun getProfile() {
        val userId = Paper.book().read<String>("userId")
        val token = Paper.book().read<String>("token")
        RestCall.client.getProfile(token,userId)
            ?.enqueue(object: Callback<UserModel?>{
                override fun onResponse(call: Call<UserModel?>, response: Response<UserModel?>) {
                    if(response.code()==200){
                        binding?.userName?.setText(response.body()?.name.toString())
                        binding?.phone?.setText(response.body()?.phone.toString())

                        binding!!.mMaterialLetterIcon.isInitials = true
                        binding!!.mMaterialLetterIcon.initialsNumber = 2
                        binding!!.mMaterialLetterIcon.letterSize = 25
                        binding!!.mMaterialLetterIcon.shapeColor = resources.getColor(R.color.blue)
                        binding!!.mMaterialLetterIcon.letter = response.body()?.name.toString()
                    }
                    else{
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserModel?>, t: Throwable) {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
                }
            })
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()

    }

}