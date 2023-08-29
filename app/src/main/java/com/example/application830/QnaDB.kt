package com.example.application830

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class QnaDB (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version){

    override fun onCreate(db: SQLiteDatabase?) { //?뺏음
        // DB 생성시 실행
        if (db != null) {
            createDatabase(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // DB 버전 변경시 실행됨
        val sqlUser : String = "DROP TABLE if exists ${LocalDatas.userData.TABLE_NAME}"
        //val sqlAddr : String = "DROP TABLE if exists ${LocalDatas.addressData.TABLE_NAME}"

        if (db != null) {
            db.execSQL(sqlUser)
            //db.execSQL(sqlAddr)
            onCreate(db)
        } //  버전이 변경되면 기존 Table을 삭제후 재생성함.
    }

    fun createDatabase(db: SQLiteDatabase) {
        // 테이블이 존재하지 않는경우 생성
        var sqlUser: String = "CREATE TABLE if not exists ${LocalDatas.userData.TABLE_NAME} (" +
                "${BaseColumns._ID} integer primary key autoincrement," +
                "${LocalDatas.userData.COLUMN_NAME_ID} varchar(15)," +
                "${LocalDatas.userData.COLUMN_NAME_PASSWORD} varchar(20)"+
                ");"

//        var sqlAddr: String = "CREATE TABLE if not exists ${LocalDatas.addressData.TABLE_NAME} (" +
//                "${BaseColumns._ID} integer primary key autoincrement," +
//                "${LocalDatas.addressData.COLUMN_NAME_ADDR} varchar(35) not null," +
//                "${LocalDatas.addressData.COLUMN_NAME_IMAGE} blob,"+
//                "${LocalDatas.addressData.COLUMN_NAME_STATE} integer"+
//                ");"

        db.execSQL(sqlUser)
        //db.execSQL(sqlAddr)
    }
}