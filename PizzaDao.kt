package com.example.colors

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class PizzaDao(context: Context) {
    private val dbHelper = AppDatabase(context)

    fun insert(pizzaName: String, pizzaPrice: Double) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_NAME, pizzaName)
            put(AppDatabase.COLUMN_PRICE, pizzaPrice)
        }
        db.insert(AppDatabase.TABLE_PIZZAS, null, values)
        db.close()
    }

    fun getAllPizzas(): List<Pair<String, Double>> {
        val pizzas = mutableListOf<Pair<String, Double>>()
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AppDatabase.TABLE_PIZZAS,
            arrayOf(AppDatabase.COLUMN_NAME, AppDatabase.COLUMN_PRICE),
            null,
            null,
            null,
            null,
            null
        )

        try {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_NAME)
                val priceIndex = cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_PRICE)
                pizzas.add(cursor.getString(nameIndex) to cursor.getDouble(priceIndex))
            }
        } finally {
            cursor.close()
            db.close()
        }

        return pizzas
    }
}
