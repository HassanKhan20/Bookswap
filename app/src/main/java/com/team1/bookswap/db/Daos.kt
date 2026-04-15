package com.team1.bookswap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): User?
}

@Dao
interface TextbookDao {
    @Insert suspend fun insert(textbook: Textbook): Long

    @Query("SELECT * FROM textbooks WHERE available = 1 ORDER BY id DESC")
    suspend fun getAll(): List<Textbook>

    @Query("""SELECT * FROM textbooks WHERE available = 1 AND (
              title LIKE '%' || :q || '%' OR
              author LIKE '%' || :q || '%' OR
              course LIKE '%' || :q || '%')
              ORDER BY id DESC""")
    suspend fun search(q: String): List<Textbook>

    @Query("SELECT * FROM textbooks WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): Textbook?

    @Query("SELECT COUNT(*) FROM textbooks")
    suspend fun count(): Int
}

@Dao
interface CartDao {
    @Insert suspend fun insert(item: CartItem): Long

    @Query("""SELECT c.id AS cartId, t.id AS textbookId, t.title, t.author,
              c.priceAtTime, c.quantity
              FROM cart_items c INNER JOIN textbooks t ON c.textbookId = t.id
              WHERE c.userId = :userId""")
    suspend fun rowsForUser(userId: Int): List<CartRow>

    @Query("DELETE FROM cart_items WHERE id = :cartId")
    suspend fun removeById(cartId: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearForUser(userId: Int)

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId AND textbookId = :textbookId")
    suspend fun exists(userId: Int, textbookId: Int): Int
}

@Dao
interface OrderDao {
    @Insert suspend fun insert(order: Order): Long

    @Query("SELECT * FROM orders WHERE buyerId = :userId ORDER BY createdAt DESC")
    suspend fun forUser(userId: Int): List<Order>
}
