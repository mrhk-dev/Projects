package com.rahmath888hussain.zomatoexpensetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DB_ZomatoExpenseTracker"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "tableZomatoExpenses"
        private const val COL_ID = "id"
        private const val COL_DATE = "date"
        private const val COL_CASH_COLLECTION = "cashCollection"
        private const val COL_TRIPS_COLLECTION = "tripsCollection"
        private const val COL_EXPENSES = "expenses"
        private const val COL_PETROL = "petrolExpense"
        private const val COL_DISTANCE_TRAVELED = "distanceTraveled"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME " +
                "($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_DATE DATE, " +
                "$COL_CASH_COLLECTION INTEGER, $COL_TRIPS_COLLECTION INTEGER, " +
                "$COL_EXPENSES INTEGER, $COL_PETROL INTEGER, $COL_DISTANCE_TRAVELED INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun checkIfDataExistsWithDate(date: String): Boolean {
        return getID(date)?.isNotEmpty() == true
    }

    fun getID(date: String): String? {
        val db = this.readableDatabase
        val query = """
        SELECT $COL_ID 
        FROM $TABLE_NAME 
        WHERE $COL_DATE = ? 
        ORDER BY $COL_ID DESC 
        LIMIT 1
    """.trimIndent()

        db.rawQuery(query, arrayOf(date)).use { cursor ->
            return if (cursor.moveToFirst()) {
                cursor.getString(0)
            } else {
                null
            }
        }
    }

    fun getAllData(): List<DatesModelClass> {
        val db = this.readableDatabase
        val dataList = mutableListOf<DatesModelClass>()

        val query = """
        SELECT $COL_ID, $COL_DATE, $COL_EXPENSES, $COL_PETROL, $COL_CASH_COLLECTION, $COL_TRIPS_COLLECTION 
        FROM $TABLE_NAME
        ORDER BY $COL_DATE DESC
    """.trimIndent()

        db.rawQuery(query, null).use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                val expenses = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXPENSES))
                val petrol = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PETROL))
                val cashCollection =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_CASH_COLLECTION))
                val tripsCollection =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRIPS_COLLECTION))

                // Create an instance of DatesModelClass and add it to the list
                val profitLoss = tripsCollection + cashCollection - petrol - expenses

                val dataEntry = DatesModelClass()
                dataEntry.expenses = expenses.toString()
                dataEntry.petrol = petrol.toString()
                dataEntry.cashCollected = cashCollection.toString()
                dataEntry.tripCollected = tripsCollection.toString()
                dataEntry.profitLoss = profitLoss.toString()
                dataEntry.date = date.toString()

                dataList.add(dataEntry)
            }
        }

        return dataList

    }

    fun getData(date: String): DatesModelClass? {
        val db = this.readableDatabase

        if (checkIfDataExistsWithDate(date)) {
            val query = """
        SELECT $COL_ID, $COL_DATE, $COL_EXPENSES, $COL_PETROL, $COL_CASH_COLLECTION, $COL_TRIPS_COLLECTION, $COL_DISTANCE_TRAVELED
        FROM $TABLE_NAME
        WHERE $COL_DATE = ?
        ORDER BY $COL_DATE DESC
    """.trimIndent()

            return db.rawQuery(query, arrayOf(date)).use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                    val expenses = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXPENSES))
                    val petrol = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PETROL))
                    val cashCollection =
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CASH_COLLECTION))
                    val tripsCollection =
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRIPS_COLLECTION))
                    val distanceTraveled =
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_DISTANCE_TRAVELED))

                    val profitLoss = tripsCollection + cashCollection - petrol - expenses

                    val data = DatesModelClass()
                    data.id = id.toString()
                    data.date = date.toString()
                    data.petrol = petrol.toString()
                    data.expenses = expenses.toString()
                    data.cashCollected = cashCollection.toString()
                    data.tripCollected = tripsCollection.toString()
                    data.distanceTraveled = distanceTraveled.toString()
                    data.profitLoss = profitLoss.toString()

                    return data
                } else {
                    null
                }
            }
        }else{
            val data = DatesModelClass()
            data.date = date
            return data
        }
    }

    fun addData(modelClass: DatesModelClass) {
        val db = this.writableDatabase
        if (checkIfDataExistsWithDate(modelClass.date)) {
            updateData(
                modelClass.date, modelClass.cashCollected.toInt(),
                modelClass.tripCollected.toInt(), modelClass.expenses.toInt(),
                modelClass.petrol.toInt(), modelClass.distanceTraveled.toInt()
            )
        } else {
            val values = ContentValues().apply {
                put(COL_DATE, modelClass.date)
                put(COL_CASH_COLLECTION, modelClass.cashCollected)
                put(COL_TRIPS_COLLECTION, modelClass.tripCollected)
                put(COL_EXPENSES, modelClass.expenses)
                put(COL_PETROL, modelClass.petrol)
                put(COL_DISTANCE_TRAVELED, modelClass.distanceTraveled)
            }
            db.insert(TABLE_NAME, null, values)
        }

        db.close()

    }

    private fun updateData(
        date: String,
        cashCollected: Int = 0,
        tripsCollected: Int = 0,
        expenses: Int = 0,
        petrol: Int = 0,
        distanceTraveled: Int = 0
    ) {
        val db = this.writableDatabase
        val id = getID(date)

        if (id != null) {
            val values = ContentValues().apply {
                put(COL_DATE, date)
                put(COL_CASH_COLLECTION, cashCollected)
                put(COL_TRIPS_COLLECTION, tripsCollected)
                put(COL_EXPENSES, expenses)
                put(COL_PETROL, petrol)
                put(COL_DISTANCE_TRAVELED, distanceTraveled)
            }

            val whereClause = "$COL_ID = ?"
            val whereArgs = arrayOf(id)

            val rowsAffected = db.update(TABLE_NAME, values, whereClause, whereArgs)

            if (rowsAffected > 0) {
                println("Record updated successfully.")
            } else {
                println("No record found with ID: $id")
            }
        } else {
            println("No record found for date: $date")
        }

    }
}