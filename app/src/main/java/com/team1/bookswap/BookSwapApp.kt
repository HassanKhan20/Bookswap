package com.team1.bookswap

import android.app.Application
import com.team1.bookswap.db.AppDatabase
import com.team1.bookswap.db.Textbook
import com.team1.bookswap.db.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BookSwapApp : Application() {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        scope.launch { seedIfEmpty() }
    }

    private suspend fun seedIfEmpty() {
        val db = AppDatabase.get(this)
        if (db.textbookDao().count() > 0) return

        val demo = User(
            name = "Demo Seller",
            email = "seller@bookswap.app",
            password = "demo1234",
            role = "seller"
        )
        val sellerId = db.userDao().insert(demo).toInt().coerceAtLeast(1)

        listOf(
            Textbook(sellerId = sellerId, title = "Calculus: Early Transcendentals",
                author = "James Stewart", course = "MATH 2413", price = 45.00,
                condition = "Good",
                description = "8th edition. Some highlighting in first few chapters. No torn pages."),
            Textbook(sellerId = sellerId, title = "Introduction to Algorithms",
                author = "Cormen, Leiserson, Rivest, Stein", course = "CSE 3318",
                price = 60.00, condition = "Like New",
                description = "3rd edition. Barely used, no markings."),
            Textbook(sellerId = sellerId, title = "Operating System Concepts",
                author = "Silberschatz, Galvin, Gagne", course = "CSE 3320",
                price = 38.50, condition = "Good",
                description = "10th edition. Cover shows light wear."),
            Textbook(sellerId = sellerId, title = "Software Engineering",
                author = "Ian Sommerville", course = "CSE 3310", price = 30.00,
                condition = "Fair", description = "10th edition. Spine creased, text intact."),
            Textbook(sellerId = sellerId, title = "Database System Concepts",
                author = "Silberschatz, Korth, Sudarshan", course = "CSE 4331",
                price = 42.00, condition = "Good", description = "7th edition."),
            Textbook(sellerId = sellerId, title = "Computer Networking: A Top-Down Approach",
                author = "Kurose, Ross", course = "CSE 4344", price = 35.00,
                condition = "Like New", description = "8th edition."),
            Textbook(sellerId = sellerId, title = "Discrete Mathematics and Its Applications",
                author = "Kenneth H. Rosen", course = "CSE 2315", price = 28.00,
                condition = "Good", description = "8th edition. Solutions manual included."),
            Textbook(sellerId = sellerId, title = "Artificial Intelligence: A Modern Approach",
                author = "Russell, Norvig", course = "CSE 4309", price = 55.00,
                condition = "Like New", description = "4th edition."),
        ).forEach { db.textbookDao().insert(it) }
    }
}
