package com.cedric.shimuli.mycinema.ui.auth

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cedric.shimuli.mycinema.databinding.ActivityRegisterBinding
import com.cedric.shimuli.mycinema.model.RegisterResponse
import com.cedric.shimuli.mycinema.network.RestCall
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.paperdb.Paper
import org.apache.commons.lang3.RandomStringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var selectedImageBitmap: Bitmap
    val partImage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this);

        binding.addImage.setOnClickListener(View.OnClickListener {
            // create a dialog
            val imageDialog = AlertDialog.Builder(this)
            imageDialog.setTitle("Select Option")
            val dialogOptions = arrayOf("Select picture from gallery",
                "Capture photo from camera")

            imageDialog.setItems(dialogOptions){
                    _, which->
                when(which){
                    // array index 0 = open gallery
                    0-> openGallery()
                    1-> openCamera()
                }
            }
            imageDialog.show()
        })
        binding.RegisterBtn.setOnClickListener(View.OnClickListener {
            validateData()
        })
    }

    private fun validateData() {
        when {
            binding.firstName.text.isNullOrEmpty() -> {
                binding.firstName.error = "First Name is Required Please!"
            }
            binding.secondName.text.isNullOrEmpty() -> {
                binding.secondName.error = "Last Name is Required Please!"
            }

            binding.phone.text.isNullOrEmpty() -> {
                binding.phone.error = "Phone Number  is Required Please!"
            }

            binding.password.text.isNullOrEmpty() -> {
                binding.password.error = "Password is too short!"
            }

//            !binding.password.toString().equals(binding.confirmPassword.toString())-> {
//                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show()
//            }
            else->{

                val firstname:String = binding.firstName.text.toString()
                val lastname:String = binding.secondName.text.toString()

                val phone:String = binding.phone.text.toString()
                val password:String = binding.password.text.toString()

                val name = "$firstname $lastname"

                registerUser(name,phone, password)


            }
        }

    }

    private fun registerUser(name: String, phone: String, password: String) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()

        val email: String = randomeString().toString().lowercase() + "@gmail.com"
        //val email:String = "customer@gmail.com"
        RestCall.client.register(name, email, phone, password)
            ?.enqueue(object: Callback<RegisterResponse?>{
                override fun onResponse(
                    call: Call<RegisterResponse?>,
                    response: Response<RegisterResponse?>) {
                    var responseCode =""
                    var responseMessage = ""
                    if (response.code() == 200){
                        pDialog.dismiss()
                        Toast.makeText(this@RegisterActivity, "Account was created successfully", Toast.LENGTH_LONG).show()
                        val userId = response.body()?.id.toString()
                        val code = response.body()?.verifyPhoneCode

//                        Paper.book().write<String>("userId", userId)
//                        Paper.book().write("code", code)
//                        Paper.book().write("phone", phone)

                        val intent = Intent(this@RegisterActivity, VerifyAccountActivity::class.java)
                        intent.putExtra("userId",userId)
                        intent.putExtra("code",code)
                        intent.putExtra("phone",phone)
                        startActivity(intent)
                        finish()
                }
                else if(response.code() == 400){
                    pDialog.dismiss()
                        SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Account Exists!")
                            .setContentText("Phone Number is already in the system!")
                            .setConfirmText("Ok")
                            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                            .show()

                        Toast.makeText(this@RegisterActivity, "Phone Number is already in the system", Toast.LENGTH_LONG).show()
                }
                    else if (response.code() == 500){
                        pDialog.dismiss()
                        SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Sorry!")
                            .setContentText("Something went wrong,try again later!")
                            .setConfirmText("Ok")
                            .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                            .show()
                        Toast.makeText(this@RegisterActivity, "Something went wrong,try again later", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                    pDialog.dismiss()
                    Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                Log.d("RETROFIT", "ERROR: " + t.message)
                }

            })

    }
    private fun openCamera() {
        // ask for permissions
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ). withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?)

            // check permission
            {
                if (report!!.areAllPermissionsGranted()){
                    //if yes open gallery
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                showPermissionDialog()
            }
        }).onSameThread().check()
    }

    // open galley function
    private fun openGallery() {
        // ask for permissions
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ). withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?)

            // check permission
            {
                if (report!!.areAllPermissionsGranted()){
                    //if yes open gallery
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                p1: PermissionToken?
            ) {
                showPermissionDialog()
            }
        }).onSameThread().check()
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this).setMessage("You need to give permissions for this feature, enable in application settings")
            .setPositiveButton("Go To SETTINGS"){

                // open settings in phone
                    _,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                    dialog, _->
                dialog.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode== GALLERY){
                if(data != null){
                    val contentUrl = data.data
                    val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = contentUrl?.let { contentResolver.query(it, imageProjection, null, null, null) }
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val indexImage: Int = cursor.getColumnIndex(imageProjection[0])
                        val partImage = cursor.getString(indexImage)

//                        imgPath.setText(partImage) // Get the image file absolute path
                        Toast.makeText(this@RegisterActivity, partImage, Toast.LENGTH_LONG).show()
                        var bitmap: Bitmap? = null
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentUrl)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        binding.profile.setImageBitmap(bitmap) // Set the ImageView with the bitmap of the image
                    }

//                    try{
//                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentUrl)
//                        binding.profile.setImageBitmap(selectedImageBitmap)
//
//                    }
//                    catch(e: IOException){
//                        e.printStackTrace()
//                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
//                    }
                }
            }
            else if(requestCode == CAMERA){
                selectedImageBitmap = data!!.extras!!.get("data") as Bitmap
                binding.profile.setImageBitmap(selectedImageBitmap)
            }
        }
    }

    //permission codes
    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
    }

    fun randomeString(): String? {
        return RandomStringUtils.randomAlphabetic(8)
    }
}

