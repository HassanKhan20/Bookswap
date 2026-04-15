package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.databinding.ActivityLoginBinding
import com.team1.bookswap.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener { doLogin() }
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun doLogin() {
        val email = binding.emailInput.text.toString().trim()
        val pw = binding.passwordInput.text.toString()
        if (email.isEmpty() || pw.isEmpty()) {
            toast("Enter email and password"); return
        }
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                AppDatabase.get(this@LoginActivity).userDao().login(email, pw)
            }
            if (user == null) {
                toast("Invalid credentials")
            } else {
                Session.currentUser = user
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
