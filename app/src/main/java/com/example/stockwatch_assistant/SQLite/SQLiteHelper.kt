package com.example.stockwatch_assistant.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta

class SQLiteHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + SYMBOL_COL + " TEXT PRIMARY KEY, "
                + NAME_COL + " TEXT, "
                + EXCHANGE_COL + " TEXT )")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addStock(symbol: String, name: String, exchange: String){
        val newStock = ContentValues()

        newStock.put(SYMBOL_COL,symbol)
        newStock.put(NAME_COL,name)
        newStock.put(EXCHANGE_COL,exchange)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, newStock)

        db.close()
    }

    fun addAllStocks(listStock: List<StockMeta>){
        val db = this.writableDatabase
        var newStock = ContentValues()

        for (i in listStock){
            newStock.put(SYMBOL_COL,i.symbol)
            newStock.put(NAME_COL,i.name)
            newStock.put(EXCHANGE_COL,i.exchange)
            db.insert(TABLE_NAME, null, newStock)
        }
        db.close()
    }

    fun getAllStocks(): Cursor {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $TABLE_NAME",null)
    }

    fun isTableExist(db: SQLiteHelper?, table: String?): Boolean {

//        val sqLiteDatabase = db?.writableDatabase
//
//        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen || table == null) {
//            return false
//        }
//        var count = 0
//        val args = arrayOf("table", table)
//        val cursor = sqLiteDatabase.rawQuery(
//            "SELECT COUNT(*) FROM sqlite_master WHERE type=? AND name=?",
//            args,
//            null
//        )
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0)
////            count++
//        }
//        cursor.close()
//        return count > 0

        val sqLiteDatabase = db?.writableDatabase

        var count = "SELECT count(*) FROM $table";
        val mcursor = sqLiteDatabase!!.rawQuery(count, null);
        mcursor.moveToFirst();
        var icount : Int = mcursor.getInt(0);
        return icount > 0

    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private const val DATABASE_NAME = "STOCKWATCH"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        //table name
        private const val TABLE_NAME = "ALLSTOCKS"

        //column name
        private const val SYMBOL_COL = "symbol"
        private const val NAME_COL = "name"
        private const val EXCHANGE_COL = "exchange"
        private const val TIME_COL = "time"
        private const val DATE_COL = "date"
        private const val PRICE_COL = "price"

    }
}