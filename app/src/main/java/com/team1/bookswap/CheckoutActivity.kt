package com.team1.bookswap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.team1.bookswap.databinding.ActivityCheckoutBinding
import com.team1.bookswap.db.AppDatabase
import com.team1.bookswap.db.CartRow
import com.team1.bookswap.db.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private var rows: List<CartRow> = emptyList()
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelBtn.setOnClickListener { finish() }
        binding.payBtn.setOnClickListener { pay() }
        binding.doneBtn.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i); finish()
        }
        loadSummary()
    }

    private fun loadSummary() {
        val uid = Session.userId
        if (uid < 0) { finish(); return }
        lifecycleScope.launch {
            rows = withContext(Dispatchers.IO) {
                AppDatabase.get(this@CheckoutActivity).cartDao().rowsForUser(uid)
            }
            if (rows.isEmpty()) {
                toast("Your cart is empty")
                finish(); return@launch
            }
            total = rows.sumOf { it.priceAtTime * it.quantity }
            binding.summary.text = rows.joinToString("\n") {
                "\u2022 ${it.title}  \u2014  $" + String.format("%.2f", it.priceAtTime)
            }
            binding.total.text = "$" + String.format("%.2f", total)
        }
    }

    private fun pay() {
        val name = binding.cardName.text.toString().trim()
        val num = binding.cardNum.text.toString().filter { it.isDigit() }
        val exp = binding.expiry.text.toString().trim()
        val cvv = binding.cvv.text.toString().trim()
        if (name.isEmpty()) return toast("Enter name on card")
        if (num.length < 12) return toast("Enter a valid card number")
        if (!exp.matches(Regex("""\d{2}/\d{2}"""))) return toast("Expiry must be MM/YY")
        if (cvv.length < 3) return toast("Enter CVV")

        val uid = Session.userId
        lifecycleScope.launch {
            val db = AppDatabase.get(this@CheckoutActivity)
            val order = Order(buyerId = uid, total = total, itemCount = rows.size)
            withContext(Dispatchers.IO) {
                db.orderDao().insert(order)
                db.cartDao().clearForUser(uid)
            }
            binding.payView.visibility = View.GONE
            binding.confirmView.visibility = View.VISIBLE
            binding.confirmDetails.text =
                "${rows.size} item(s) purchased\nTotal charged: $" + String.format("%.2f", total)
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
