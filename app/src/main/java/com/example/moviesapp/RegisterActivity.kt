package com.example.moviesapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.Entities.User
import com.example.moviesapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val binding:ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val  emailPattern:String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        binding.loginBtn.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerBtn.setOnClickListener {
            val progressDialog = ProgressDialog(this@RegisterActivity)
            progressDialog.setMessage("Please Wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            var user_name=binding.userNameTxt.text.toString()
            var email=binding.emailTxt.text.toString()
            var password=binding.passwordTxt.text.toString()
            var password_confirm=binding.password2Txt.text.toString()
            if(user_name.isEmpty()||email.isEmpty()||password.isEmpty()||password_confirm.isEmpty()){
                progressDialog.dismiss()
                Toast.makeText(this,"\n" +
                        "Enter your information completely.",Toast.LENGTH_SHORT).show()
            }else{
                if(!isEmailValid(email)) {
                    progressDialog.dismiss()
                    binding.emailTxt.error = "Invalid Email"
                }else if(password.length<8){
                    progressDialog.dismiss()
                    binding.passwordTxt.error = "The length of the password must contain at least 8 characters"
                }
                else if(password!=password_confirm){
                    progressDialog.dismiss()
                    binding.passwordTxt.error = "Password does not Match"
                    binding.password2Txt.error = "Password does not Match"
                } else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                        if(task.isSuccessful) {
                            database.reference.child("Users").child(auth.currentUser!!.uid)
                                .setValue(User(user_name,email)).addOnCompleteListener { task->
                                    if(task.isSuccessful){
                                        progressDialog.dismiss()
                                        Toast.makeText(this,"User Successfully created.",Toast.LENGTH_LONG).show()
                                        val intent=Intent(this@RegisterActivity,MainActivity::class.java)
                                        startActivity(intent)
                                        finish()


                                    }else{
                                        Toast.makeText(this,"Could not create user",Toast.LENGTH_LONG).show()
                                    }
                                }
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener { exception->
                        Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            emailPattern
        ).matcher(email).matches()
    }
}