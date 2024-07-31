package com.example.moviesapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.loginBtn.setOnClickListener {
            val progressDialog = ProgressDialog(this@LoginActivity)
            progressDialog.setMessage("Please Wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            var email = binding.emailTxt.text.toString()
            var password = binding.passwordTxt.text.toString()
            if (email.isEmpty()) {
                progressDialog.dismiss()
                binding.emailTxt.error = "Enter your email"
            } else if (password.isEmpty()) {
                progressDialog.dismiss()
                binding.emailTxt.error = "Enter your password"
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog.dismiss()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed to login", Toast.LENGTH_LONG).show()
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.registerBtn.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}