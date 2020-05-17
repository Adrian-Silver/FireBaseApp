package com.example.firebaseapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Show all users in UsersActivity
        img_users.setOnClickListener {
            startActivity(Intent(this,UsersActivity::class.java))
        }

//        Show Password
        img_show.setOnClickListener {
            showPassword()
        }

        //    Add data into database
        btn_signup.setOnClickListener {
            addUser()
        }



    }



//    Grab data from edit text
    fun addUser(){
    val  firstname = et_firstname.text.trim().toString()
    val  lastname = et_lastname.text.trim().toString()
    val  email = et_email.text.trim().toString()
    val  password = et_password.text.trim().toString()
    val id = System.currentTimeMillis()  //Time in milli-seconds

//    Initalize show progress bar
    val appProgress = showProgress()

//    Validate input
    if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()|| password.isEmpty()){
        showMessage("Empty fields", "Please ensure all fields have the required data")
    }else{
//        Firebase database
//        1. Ssave data into the database
        val my_ref = FirebaseDatabase.getInstance().reference.child("Users/$id")
//      Data is to be displayed in the following format :  438473478 \\ John \\ Doe \\ email \\Password

        val user_object = UserSchema(firstname, lastname, email, password)

//        Show progress bar as data is being added
        appProgress.show()
        my_ref.setValue(user_object).addOnCompleteListener{task ->
            appProgress.dismiss()
            if (task.isSuccessful){
                showMessage("Saving was successful","$firstname's data has been saved successfuly")
                clearEditText()
            }else{
                showMessage("Saving unsuccessful"," Data has not been saved ")
            }
        }

    }
}

//    Show message
    fun showMessage (title:String, message:String){
        val dialogBox = AlertDialog.Builder(this)
        dialogBox.setTitle(title)
        dialogBox.setMessage(message)
    dialogBox.setPositiveButton("OK",{dialog, which -> dialog.dismiss() })
    dialogBox.create().show()

    }

//    ProgressBar
    fun showProgress(): ProgressDialog{
        val progress = ProgressDialog(this)
        progress.setTitle("Saving...")
        progress.setMessage("Please wait as data is being saved")
        return progress

    }

    fun clearEditText(){
        et_firstname.setText(null)
        et_lastname.setText(null)
        et_email.setText(null)
        et_password.setText(null)
    }

    fun showPassword(){
        if (img_show.tag.toString().equals("Show")){
            et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            img_show.tag =  "Hide"
        }else{
            et_password.transformationMethod = PasswordTransformationMethod.getInstance()
            et_password.tag = "Show"
        }
    }


}
