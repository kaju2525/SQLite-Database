package com.demo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log







class MyDB(context : Context):SQLiteOpenHelper(context,DATABASE_NAME,null,1){

    companion object {

        private val DATABASE_NAME="/sdcard/MySchool/school.db"
        private val TABLE_NAME="Student"
        private val COL_ID="ID"
        private val COL_ROLL_NO="ROLL_NO"
        private val COL_NAME="NAME"
        private val COL_AGE="AGE"
        private val COL_MOB="MOBILE"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_ROLL_NO INTEGER, $COL_NAME TEXT, $COL_AGE INTEGER,$COL_MOB TEXT )")
        Log.d("TAGS ","onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

        Log.d("TAGS ","onUpgrade")
    }





    fun AddData(contact:Contact):String{

        val db=this.writableDatabase
        val vals=ContentValues()

        vals.put(COL_ROLL_NO,contact.ROLL_NO)
        vals.put(COL_NAME,contact.NAME)
        vals.put(COL_AGE,contact.AGE)
        vals.put(COL_MOB,contact.MOB)

       val i= db.insert(TABLE_NAME,null,vals)
        db.close()
        if(i>0){
            Log.d("TAGS","SAVE DATA")
            return "SAVE DATA"
        }else
            Log.d("TAGS","Not SAVE DATA")
        return "Not SAVE DATA"
    }



    fun getAllContacts(): List<Contact> {
        val contactList = ArrayList<Contact>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT  * FROM  $TABLE_NAME", null)


        if (cursor.moveToFirst()) {
            do {
                val contact = Contact()
                contact.ROLL_NO=cursor.getString(1).toInt()
                contact.NAME=cursor.getString(2)
                contact.AGE=cursor.getString(3).toInt()
                contact.MOB=cursor.getString(4)
                contactList.add(contact)
            } while (cursor.moveToNext())
        }

        return contactList
    }

    fun deleteContact(contact: Contact) :String {
        val db = this.writableDatabase
        val i = db.delete(TABLE_NAME, COL_ROLL_NO + " = ?", arrayOf(contact.ROLL_NO.toString()))
        db.close()
        if (i > 0) {
            return "Deleted DATA"
        } else
            return "Not Deleted DATA"
    }

    fun getContactsCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT  * FROM $TABLE_NAME", null)
        return cursor.count
    }

    fun updateContact(contact: Contact): String {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COL_NAME, contact.NAME)
        values.put(COL_AGE, contact.AGE)

        // updating row
        val i= db.update(TABLE_NAME, values, COL_ROLL_NO + " = ?", arrayOf(contact.ROLL_NO.toString()))
        db.close()
        if(i>0){
            return "Update DATA"
        }else
        return "Not Update DATA"
    }



    fun getContact(id: Contact): Contact {
        val db = this.readableDatabase

        val cursor = db.query(TABLE_NAME, arrayOf(COL_NAME, COL_AGE, COL_MOB), COL_ROLL_NO + "=?",
                arrayOf(id.ROLL_NO.toString()), null, null, null, null)
        cursor?.moveToFirst()
        return Contact(0,cursor!!.getString(2), cursor.getString(3).toInt(), cursor.getString(4))
    }


}
