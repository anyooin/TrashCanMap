package com.example.application830

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.wifi.rtt.CivicLocationKeys.STATE
import android.os.FileObserver.DELETE
import android.provider.BaseColumns
import com.example.application830.AddressDatas.addressData.COLUMN_NAME_ADDR
import com.example.application830.AddressDatas.addressData.COLUMN_NAME_IMAGE
import java.io.ByteArrayOutputStream

data class AddressImage(var num: Int, var address: String?, var image: ByteArray?)

class AddressDB(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version){
    override fun onCreate(db: SQLiteDatabase?) {
        // DB 생성시 실행
        if (db != null) {
        createDatabase(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // DB 버전 변경시 실행됨
        val sqlAddr : String = "DROP TABLE if exists ${AddressDatas.addressData.TABLE_NAME}"

        if (db != null) {
            db.execSQL(sqlAddr)
            onCreate(db)
        } //  버전이 변경되면 기존 Table을 삭제후 재생성함.
    }

    fun createDatabase(db: SQLiteDatabase) {
        // 테이블이 존재하지 않는경우 생성
        var sqlAddr: String = "CREATE TABLE if not exists ${AddressDatas.addressData.TABLE_NAME} (" +
                "${BaseColumns._ID} integer primary key autoincrement," +
                "${AddressDatas.addressData.COLUMN_NAME_ADDR} varchar(35) not null," +
                "${AddressDatas.addressData.COLUMN_NAME_IMAGE} blob,"+
                "${AddressDatas.addressData.COLUMN_NAME_STATE} integer"+
                ");"

        db.execSQL(sqlAddr)
    }

    //Address 함수들
    fun registerAddressImage(addr: String, image: ByteArray?){
        val db =this.writableDatabase
        var p : SQLiteStatement = db.compileStatement(
            "INSERT INTO ${AddressDatas.addressData.TABLE_NAME} values(null,?, ?,?);")
        p.bindString(1, addr)
        p.bindBlob(2, image)
        p.bindLong(3, 0)
        p.execute()

//        val values = ContentValues().apply {// insert될 데이터값
//            put(AddressDatas.addressData.COLUMN_NAME_ADDR, addr)
//            put(AddressDatas.addressData.COLUMN_NAME_IMAGE, image)
//            put(AddressDatas.addressData.COLUMN_NAME_STATE, 0)
//        }
//        val newRowId = db?.insert(AddressDatas.addressData.TABLE_NAME, null, values)
        // 인서트후 인서트된 primary key column의 값(_id) 반환.
    }

    fun deleteItem(num : Int) {
        val db = this.writableDatabase
        var p : SQLiteStatement = db.compileStatement(
            "DELETE FROM ${AddressDatas.addressData.TABLE_NAME} " +
                    "WHERE ${BaseColumns._ID} = " + num + ";")

        p.execute()
    }

    fun registerMapItem(num : Int) {
        val db = this.writableDatabase
        var p : SQLiteStatement = db.compileStatement(
            "UPDATE ${AddressDatas.addressData.TABLE_NAME} " +
                    "SET ${AddressDatas.addressData.COLUMN_NAME_STATE} = 1 " +
                    "WHERE ${BaseColumns._ID} = " + num + ";")

        p.execute()

        //UPDATE AddressDB SET state=1 WHERE ID=1
    }

    fun cancelItem(num : Int) {
        val db = this.writableDatabase
        var p : SQLiteStatement = db.compileStatement(
            "UPDATE ${AddressDatas.addressData.TABLE_NAME} " +
                    "SET ${AddressDatas.addressData.COLUMN_NAME_STATE} = 0 " +
                    "WHERE ${BaseColumns._ID} = " + num + ";")

        p.execute()

        //UPDATE AddressDB SET state=1 WHERE ID=1
    }

    fun getList(state: String): MutableList<AddressImage> {
        val list = mutableListOf<AddressImage>()
        val db = this.readableDatabase

        // 리턴받고자 하는 컬럼 값의 array
        val projection = arrayOf(BaseColumns._ID,
            AddressDatas.addressData.COLUMN_NAME_ADDR,
            AddressDatas.addressData.COLUMN_NAME_IMAGE)

        //  WHERE "id" = id 구문 적용하는 부분
        val selection = "${AddressDatas.addressData.COLUMN_NAME_STATE} = ?"
        val selectionArgs = arrayOf(state)

        val cursor = db.query(
            AddressDatas.addressData.TABLE_NAME, projection,
            selection, selectionArgs, null, null, null)

        while(cursor.moveToNext()) {
            //val address = cursor.getString(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_ADDR))
            //val image: ByteArray? = cursor.getBlob(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_IMAGE)) ?: null
            val num = cursor.getInt(0)
            val address = cursor.getString(1)
            val image: ByteArray? = cursor.getBlob(2) ?: null
            list.add(AddressImage(num, address, image))
        }
        cursor.close()
        //db.close()

        return list
    }

    fun getListAll(): MutableList<AddressImage> {
        val list = mutableListOf<AddressImage>()
        val db = this.readableDatabase

        // 리턴받고자 하는 컬럼 값의 array
        val projection = arrayOf(BaseColumns._ID,
            AddressDatas.addressData.COLUMN_NAME_ADDR,
            AddressDatas.addressData.COLUMN_NAME_IMAGE)

        //  WHERE "id" = id 구문 적용하는 부분
        val cursor = db.query(
            AddressDatas.addressData.TABLE_NAME, projection,
            null, null, null, null, null)

        while(cursor.moveToNext()) {
            //val address = cursor.getString(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_ADDR))
            //val image: ByteArray? = cursor.getBlob(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_IMAGE)) ?: null
            val num = cursor.getInt(0)
            val address = cursor.getString(1)
            val image: ByteArray? = cursor.getBlob(2) ?: null
            list.add(AddressImage(num, address, image))
        }
        cursor.close()

        return list
    }
}