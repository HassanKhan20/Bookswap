package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.databinding.ActivityProfileBinding
import com.team1.bookswap.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Session.currentUser
        if (user == null) { startLogin(); return }

        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
        binding.profileRole.text = user.role.uppercase()
        binding.avatar.text = user.name.trim().firstOrNull()?.uppercase() ?: "U"

        bindThemeRadios()
        loadStats(user.id)

        binding.backBtn.setOnClickListener { finish() }
        binding.profileLogoutBtn.setOnClickListener {
            Session.logout()
            startLogin()
        }
    }

    private fun bindThemeRadios() {
        when (ThemePrefs.load(this)) {
            ThemePrefs.MODE_LIGHT -> binding.themeLight.isChecked = true
            ThemePrefs.MODE_DARK -> binding.themeDark.isChecked = true
            else -> binding.themeSystem.isChecked = true
        }
        binding.themeGroup.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                binding.themeLight.id -> ThemePrefs.MODE_LIGHT
                binding.themeDark.id -> ThemePrefs.MODE_DARK
                else -> ThemePrefs.MODE_SYSTEM
            }
            ThemePrefs.save(this, mode)
        }
    }

    private fun loadStats(userId: Int) {
        lifecycleScope.launch {
            val db = AppDatabase.get(this@ProfileActivity)
            val (orders, listings) = withContext(Dispatchers.IO) {
                val o = db.orderDao().forUser(userId)
                val l = db.textbookDao().countBySeller(userId)
                o to l
            }
            binding.orderCount.text = orders.size.toString()
            binding.listingCount.text = listings.toString()
            val total = orders.sumOf { it.total }
            binding.totalSpent.text = String.format("$%.2f", total)
        }
    }

    private fun startLogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i); finish()
    }
}
