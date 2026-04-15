package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.bookswap.adapters.CartAdapter
import com.team1.bookswap.databinding.ActivityCartBinding
import com.team1.bookswap.db.AppDatabase
import com.team1.bookswap.db.CartRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CartAdapter { row -> remove(row) }
        binding.cartList.layoutManager = LinearLayoutManager(this)
        binding.cartList.adapter = adapter

        binding.backBtn.setOnClickListener { finish() }
        binding.checkoutBtn.setOnClickListener { checkout() }
        load()
    }

    private fun load() {
        val uid = Session.userId
        if (uid < 0) return
        lifecycleScope.launch {
            val rows = withContext(Dispatchers.IO) {
                AppDatabase.get(this@CartActivity).cartDao().rowsForUser(uid)
            }
            adapter.submit(rows)
            render(rows)
        }
    }

    private fun render(rows: List<CartRow>) {
        val total = rows.sumOf { it.priceAtTime * it.quantity }
        binding.total.text = "$" + String.format("%.2f", total)
        binding.emptyCart.visibility = if (rows.isEmpty()) View.VISIBLE else View.GONE
        binding.checkoutBtn.isEnabled = rows.isNotEmpty()
    }

    private fun remove(row: CartRow) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.get(this@CartActivity).cartDao().removeById(row.cartId)
            }
            load()
        }
    }

    private fun checkout() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
