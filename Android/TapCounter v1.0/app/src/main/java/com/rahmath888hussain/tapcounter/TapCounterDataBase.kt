package com.rahmath888hussain.tapcounter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TapCounterDataBase(
    context: Context,
    dataBaseName: String = "MyDatabase",
    version: Int = 1
) : SQLiteOpenHelper(
    context, dataBaseName, null, version
) {

    //        table name
    private val tableName: String = "TblCounter"

    //    table column name
    private val counterId: String = "CounterID"
    private val counterName: String = "CounterName"
    private val counterValue: String = "CounterValue"
    private val showCounterDecrementBtn: String = "ShowCounterDecrementBtn"
    private val isDefaultCounter: String = "IsDefaultCounter"


    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $tableName (
                $counterId INTEGER PRIMARY KEY AUTOINCREMENT,
                $counterName TEXT NOT NULL,
                $counterValue INTEGER NOT NULL,
                $showCounterDecrementBtn INTEGER DEFAULT 0,
                $isDefaultCounter INTEGER DEFAULT 0
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    private fun getAvailableID(): Int {
        val db = writableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT MAX($counterId) AS $counterId FROM $tableName;", null)
        var id: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(with(cursor) { getColumnIndex(counterId) })
            } while (cursor.moveToNext())
            cursor.close()
            return id
        } else {
            cursor.close()
            return 0
        }
    }


    fun insertCounter(count: ModelClassCount): Int {
        val db = writableDatabase
        var insertValue: Long = -1
        try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put(counterName, count.name)
                put(counterValue, count.value)
                put(isDefaultCounter, if (count.default) 1 else 0)
                put(showCounterDecrementBtn, if (count.showDecreaseBtn) 1 else 0)
            }

            insertValue = db.insert(tableName, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseUpdate", "Error updating counter: ${e.message}")
        } finally {
            db.endTransaction()
        }

        return if (insertValue != -1L) {
            getAvailableID()
        } else {
            -1
        }
    }

    fun getACountersCount(): Int {
        val db = readableDatabase
        var getTapCountersCount: Int = 0
        try {
            db.beginTransaction()

            val cursor: Cursor =
                db.rawQuery("SELECT MAX($counterId) AS $counterId FROM $tableName ORDER BY $counterId DESC;", null)

            if (cursor.moveToFirst()) {
                do {
                    getTapCountersCount = cursor.getInt(with(cursor) { getColumnIndex(counterId) })
                } while (cursor.moveToNext())
                cursor.close()

            } else {
                cursor.close()
                return 0
            }
        } catch (e: Exception) {
            Log.e("DatabaseUpdate", "Error updating counter: ${e.message}")
        } finally {
            db.endTransaction()
        }
        return getTapCountersCount
    }

    fun getDefaultModelClass(id:Int):ModelClassCount{
        val db = readableDatabase
        var countModelClass = ModelClassCount()

        try {
            db.beginTransaction()

            val cursor: Cursor =
                db.rawQuery("SELECT * FROM $tableName WHERE $counterId = ?",
                    arrayOf(id.toString()))

            if (cursor.moveToFirst()) {
                do {
                    countModelClass.id = cursor.getInt(with(cursor) { getColumnIndex(counterId) })
                    countModelClass.name = cursor.getString(with(cursor) { getColumnIndex(counterName) })
                    countModelClass.value = cursor.getInt(with(cursor) { getColumnIndex(counterValue) })
                    countModelClass.default = cursor.getInt(with(cursor) {
                        getColumnIndex(isDefaultCounter) }) != 0
                    countModelClass.showDecreaseBtn = cursor.getInt(with(cursor) {
                        getColumnIndex(showCounterDecrementBtn) }) != 0
                } while (cursor.moveToNext())
            }
            cursor.close()

        } catch (e: Exception) {
            Log.e("DatabaseUpdate", "Error updating counter: ${e.message}")
        } finally {
            db.endTransaction()
        }
            return countModelClass
    }

    fun updateCounter(id:Int, value:Int) {
        val db = writableDatabase
        try {
            db.beginTransaction()

            val values = ContentValues().apply {
                put(counterValue, value)
            }

            val rowsAffected = db.update(tableName, values, "$counterId = ?", arrayOf(id.toString()))

            if (rowsAffected > 0) {
                Log.d("DatabaseUpdate", "Successfully updated counter value to $value.")
            } else {
                Log.w("DatabaseUpdate", "No rows updated. Check if the default counter exists.")
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseUpdate", "Error updating counter: ${e.message}")
        } finally {
            db.endTransaction()
        }
    }

}