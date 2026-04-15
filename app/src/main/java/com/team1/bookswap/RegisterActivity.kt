package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.databinding.ActivityRegisterBinding
import com.team1.bookswap.db.AppDatabase
import com.team1.bookswap.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener { register() }
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun register() {
        val name = binding.nameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val pw = binding.passwordInput.text.toString()
        if (name.isEmpty()) return toast("Enter your name")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return toast("Invalid email")
        if (pw.length < 6) return toast("Password must be 6+ characters")

        lifecycleScope.launch {
            val db = AppDatabase.get(this@RegisterActivity)
            val existing = withContext(Dispatchers.IO) { db.userDao().findByEmail(email) }
            if (existing != null) return@launch toast("Email already registered")
            val newId = withContext(Dispatchers.IO) {
                db.userDao().insert(User(name = name, email = email, password = pw))
            }.toInt()
            Session.currentUser = User(id = newId, name = name, email = email, password = pw)
            toast("Welcome, $name!")
            val i = Intent(this@RegisterActivity, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
