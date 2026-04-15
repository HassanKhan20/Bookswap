package com.team1.bookswap

object BookCovers {
    fun forTitle(title: String): Int {
        val t = title.lowercase()
        return when {
            "calculus" in t -> R.drawable.cover_calculus
            "algorithm" in t -> R.drawable.cover_algorithms
            "operating system" in t -> R.drawable.cover_os
            "software engineering" in t -> R.drawable.cover_software
            "database" in t -> R.drawable.cover_database
            "network" in t -> R.drawable.cover_networking
            "discrete" in t -> R.drawable.cover_discrete
            "artificial intelligence" in t -> R.drawable.cover_ai
            else -> R.drawable.bg_book_cover
        }
    }
}
