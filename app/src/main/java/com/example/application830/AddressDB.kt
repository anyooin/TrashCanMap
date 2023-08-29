package com.example.application830

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.BaseColumns
import java.io.ByteArrayOutputStream

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
        val values = ContentValues().apply {// insert될 데이터값
            put(AddressDatas.addressData.COLUMN_NAME_ADDR, addr)
            put(AddressDatas.addressData.COLUMN_NAME_IMAGE, image)
            put(AddressDatas.addressData.COLUMN_NAME_IMAGE, 0)
        }
        val newRowId = db?.insert(AddressDatas.addressData.TABLE_NAME, null, values)
        // 인서트후 인서트된 primary key column의 값(_id) 반환.
    }

    fun registerAddress(addr: String){
        val db =this.writableDatabase
        val values = ContentValues().apply {// insert될 데이터값
            put(AddressDatas.addressData.COLUMN_NAME_ADDR, addr)
            put(AddressDatas.addressData.COLUMN_NAME_IMAGE, 0)
            put(AddressDatas.addressData.COLUMN_NAME_IMAGE, 0)
        }
        val newRowId = db?.insert(AddressDatas.addressData.TABLE_NAME, null, values)
        // 인서트후 인서트된 primary key column의 값(_id) 반환.
    }

//    fun drawableToByteArray(drawable: Drawable?) : ByteArray? {
//        val bitmapDrawable = drawable as BitmapDrawable?
//        val bitmap = bitmapDrawable?.bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//
//        return byteArray
//    }

    fun printList(id: String): MutableList<AddressImage> {
        val list = mutableListOf<AddressImage>()
        val db = this.readableDatabase

        // 리턴받고자 하는 컬럼 값의 array
        val projection = arrayOf(BaseColumns._ID)

        //  WHERE "id" = id 구문 적용하는 부분
        val selection = "${AddressDatas.addressData.COLUMN_NAME_STATE} = ?"
        val selectionArgs = arrayOf(id)

        val cursor = db.query(
            AddressDatas.addressData.TABLE_NAME, projection,
            selection, selectionArgs, null, null, null)

        while(cursor.moveToNext()) {
            val address = cursor.getString(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_ADDR))
            val image: ByteArray? = cursor.getBlob(cursor.getColumnIndexOrThrow(AddressDatas.addressData.COLUMN_NAME_IMAGE)) ?: null
            list.add(AddressImage(address, image))
        }
        //cursor.close()
        //db.close()

        return list
    }
}