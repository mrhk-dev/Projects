package com.rahmath888hussain.tapcounter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyAppDatabase(context: Context,
                    dataBaseName: String = "MyDatabase",
                    version: Int = 1 ) :
    SQLiteOpenHelper(context, dataBaseName, null, version ) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS Counters (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                counterName TEXT NOT NULL,
                counterValue INTEGER NOT NULL,
                defaultCounter INTEGER
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Counters")
        onCreate(db)
    }

    // Insert operation
    fun insertCounter(countModel: CountersListActivity.CountModelClass): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("counterName",countModel.counterName )
            put("counterValue",countModel.counterValue)
            put("defaultCounter",if(countModel.setDefault) 1 else 0)
        }
        return db.insert("Counters", null, values)
    }

    // Update operation
    fun updateCounter(countModel: CountersListActivity.CountModelClass): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("counterName",countModel.counterName)
            put("counterValue",countModel.counterValue)
            put("defaultCounter",countModel.setDefault)
        }
        return db.update("Counters", values, "id = ?", arrayOf(countModel.counterId.toString()))
    }

    // Delete operation
    fun deleteCounter(id: Int): Int {
        val db = writableDatabase
        return db.delete("Counters", "id = ?", arrayOf(id.toString()))
    }

    // Get all users
    fun getAllCounters(): List<CountersListActivity.CountModelClass> {
        val counters = mutableListOf<CountersListActivity.CountModelClass>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Counters", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(with(cursor) { getColumnIndex("id") })
                val name = cursor.getString(with(cursor) { getColumnIndex("counterName") })
                val value = cursor.getInt(with(cursor) { getColumnIndex("counterValue") })
                val selected = cursor.getInt(with(cursor) { getColumnIndex("defaultCounter") })
                counters.add(CountersListActivity.CountModelClass(id, name, value, if (selected == 0) false else true))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return counters
    }

}