package com.example.firebaseapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        //        pull and display data from db
        getUsers()
    }

    fun getUsers(){
        val usersarray: ArrayList<UserSchema> = ArrayList()
        val myAdapter = CustomAdapter(this, usersarray)

        val progress = showProgress()
//        fetch data from db
        progress.show()
        val fb_db_ref = FirebaseDatabase.getInstance().reference.child("Users")

        fb_db_ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
//                clear previously added data in the usersarray
                usersarray.clear()
                for(snap in p0.children){
                    val user = snap.getValue(UserSchema::class.java)
                    usersarray.add(user!!)
                }
                myAdapter.notifyDataSetChanged()
                progress.dismiss()
            }
            override fun onCancelled(p0: DatabaseError) {
                progress.dismiss()
                showMessage("Database locked", "Please wait or contact service provider")
            }
        })
        user_list.adapter = myAdapter
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
    fun showProgress(): ProgressDialog {
        val progress = ProgressDialog(this)
        progress.setTitle("Saving...")
        progress.setMessage("Please wait as data is being saved")
        return progress

    }




}
