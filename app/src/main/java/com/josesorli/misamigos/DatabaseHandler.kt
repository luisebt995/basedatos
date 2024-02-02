package com.josesorli.misamigos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "MyDatabase"
        private const val TABLE_NAME = "Contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PROVINCE = "province"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY,$KEY_NAME TEXT,$KEY_EMAIL TEXT, $KEY_PROVINCE TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(name: String, email: String, province:String): Long {
        val db = this.writableDatabase
        //Las siguientes lineas crean una lista Key,Valor con name, email
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_EMAIL, email)
        values.put(KEY_PROVINCE, province)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }

    fun getAllContact(): List<Contact>{
        val contactList = mutableListOf<Contact>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        cursor.use {
            //Check if there is a first row in the table
            if(it.moveToFirst()){
                //First to last row iteration
                do {
                    //Get values from row
                    val id = it.getInt(it.getColumnIndex(KEY_ID))
                    val name = it.getString(it.getColumnIndex(KEY_NAME))
                    val email = it.getString(it.getColumnIndex(KEY_EMAIL))
                    val province = it.getString(it.getColumnIndex(KEY_PROVINCE))

                    //Add new element in List
                    contactList.add(Contact(id, name, email, province))
                }while(it.moveToNext())
            }
        }
        db.close()
        return contactList
    }
}
