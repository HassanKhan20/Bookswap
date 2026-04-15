package com.team1.bookswap.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team1.bookswap.databinding.ItemCartBinding
import com.team1.bookswap.db.CartRow

class CartAdapter(
    private val onRemove: (CartRow) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    private val items = mutableListOf<CartRow>()

    fun submit(list: List<CartRow>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }

    inner class VH(val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = items[pos]
        h.b.title.text = r.title
        h.b.author.text = r.author
        h.b.price.text = "$" + String.format("%.2f", r.priceAtTime)
        h.b.removeBtn.setOnClickListener { onRemove(r) }
    }

    override fun getItemCount(): Int = items.size
}
