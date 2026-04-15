package com.team1.bookswap

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.databinding.ActivityDetailsBinding
import com.team1.bookswap.db.AppDatabase
import com.team1.bookswap.db.CartItem
import com.team1.bookswap.db.Textbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var book: Textbook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookId = intent.getIntExtra("bookId", -1)
        binding.backBtn.setOnClickListener { finish() }
        binding.addToCartBtn.setOnClickListener { addToCart() }

        lifecycleScope.launch {
            val db = AppDatabase.get(this@DetailsActivity)
            val t = withContext(Dispatchers.IO) { db.textbookDao().findById(bookId) }
            if (t == null) { toast("Book not found"); finish(); return@launch }
            book = t
            binding.title.text = t.title
            binding.author.text = "by ${t.author}"
            binding.price.text = "$" + String.format("%.2f", t.price)
            binding.course.text = t.course
            binding.condition.text = t.condition
            binding.description.text = t.description.ifBlank { "No description provided." }
            val seller = withContext(Dispatchers.IO) { db.userDao().findById(t.sellerId) }
            binding.seller.text = seller?.name ?: "Unknown"
        }
    }

    private fun addToCart() {
        val t = book ?: return
        val uid = Session.userId
        if (uid < 0) { toast("Log in first"); return }
        lifecycleScope.launch {
            val dao = AppDatabase.get(this@DetailsActivity).cartDao()
            val exists = withContext(Dispatchers.IO) { dao.exists(uid, t.id) }
            if (exists > 0) { toast("Already in cart"); return@launch }
            withContext(Dispatchers.IO) {
                dao.insert(CartItem(userId = uid, textbookId = t.id, priceAtTime = t.price))
            }
            toast("Added to cart")
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
