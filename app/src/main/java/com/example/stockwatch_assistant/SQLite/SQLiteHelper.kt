package com.example.stockwatch_assistant.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta

class SQLiteHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val re = Regex("[^A-Za-z0-9 ]")

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

    fun createNewTableStock(tableName: String){
        val db = this.writableDatabase
        val newTableName = re.replace(tableName,"")

//        db.execSQL("DROP TABLE IF EXISTS $newTableName")
        Log.d("tabForSym","before creating table")
//        val query = ("CREATE TABLE $newTableName (" +
//                "$DATE_COL TEXT, $TIME_COL TEXT, $PRICE_COL INT )")
        val query = ("CREATE TABLE IF NOT EXISTS $newTableName (" +
                "$DATE_COL TEXT, $TIME_COL TEXT, $PRICE_COL INT )")

        db.execSQL(query)

        var newStockPrice = ContentValues()
        for (i in 0..10){
            newStockPrice.put(DATE_COL,"11/16/2022")
            newStockPrice.put(TIME_COL,i.toString())
            newStockPrice.put(PRICE_COL,i*25+17)
            db.insert(newTableName,null,newStockPrice)
        }

        Log.d("tabForSym","after creating table")
        db.close()
    }

    fun getStockPriceAtMin(symbol: String, time: String): String{
        val newSymbol = re.replace(symbol,"")
        val db = this.readableDatabase
        var price =""
//        val cur = db.rawQuery("IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'TheSchema' AND TABLE_NAME = $newSymbol)" +
//                "BEGIN SELECT * FROM $newSymbol WHERE $TIME_COL = $time END",null)
        val cur = db.rawQuery("SELECT * FROM $newSymbol WHERE $TIME_COL = $time", null)
        while (cur.moveToNext()){
            price = cur.getString(cur.getColumnIndexOrThrow("price"))
        }
        return price
    }

    fun deleteTable(tableName: String){
        val db = this.writableDatabase
        val newTableName = re.replace(tableName,"")

        db.execSQL("DROP TABLE IF EXISTS $newTableName")

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

    fun getAllStocks(): Cursor{
        val db = this.readableDatabase
        var cur = db.rawQuery("SELECT * FROM $TABLE_NAME",null)
//        db.close() //do not push db.close() in readableDatabase
//                   //for some reason cur still need db
        return cur
    }

    fun isTableExist(db: SQLiteHelper?, table: String?): Boolean {

        val sqLiteDatabase = db?.writableDatabase

        var count = "SELECT count(*) FROM $table"
        val mcursor = sqLiteDatabase!!.rawQuery(count, null);
        mcursor.moveToFirst()
        var icount : Int = mcursor.getInt(0)

        db.close()

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

//IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'TheSchema' AND TABLE_NAME = $newSymbol)
//BEGIN
//SELECT * FROM A WHERE time == 2
//END