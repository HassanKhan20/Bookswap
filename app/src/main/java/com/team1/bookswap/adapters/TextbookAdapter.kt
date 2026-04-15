package com.team1.bookswap.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team1.bookswap.databinding.ItemTextbookBinding
import com.team1.bookswap.db.Textbook

class TextbookAdapter(
    private val onClick: (Textbook) -> Unit
) : RecyclerView.Adapter<TextbookAdapter.VH>() {

    private val items = mutableListOf<Textbook>()

    fun submit(list: List<Textbook>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemTextbookBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemTextbookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val t = items[pos]
        h.b.title.text = t.title
        h.b.author.text = t.author
        h.b.price.text = "$" + String.format("%.2f", t.price)
        h.b.course.text = t.course
        h.b.root.setOnClickListener { onClick(t) }
    }

    override fun getItemCount(): Int = items.size
}
