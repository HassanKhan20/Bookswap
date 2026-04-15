package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.adapters.TextbookAdapter
import com.team1.bookswap.databinding.ActivityHomeBinding
import com.team1.bookswap.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: TextbookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Session.currentUser
        if (user == null) { startLogin(); return }
        binding.greeting.text = "Hi, ${user.name.split(" ").first()}"

        adapter = TextbookAdapter { book ->
            startActivity(Intent(this, DetailsActivity::class.java)
                .putExtra("bookId", book.id))
        }
        binding.bookList.adapter = adapter

        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.logoutBtn.setOnClickListener {
            Session.logout(); startLogin()
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loadBooks(s?.toString().orEmpty().trim())
            }
        })

        loadBooks("")
    }

    override fun onResume() {
        super.onResume()
        loadBooks(binding.searchInput.text.toString().trim())
    }

    private fun loadBooks(query: String) {
        lifecycleScope.launch {
            val dao = AppDatabase.get(this@HomeActivity).textbookDao()
            val list = withContext(Dispatchers.IO) {
                if (query.isBlank()) dao.getAll() else dao.search(query)
            }
            adapter.submit(list)
            binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun startLogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i); finish()
    }
}
