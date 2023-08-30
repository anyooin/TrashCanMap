package com.example.application830

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.provider.BaseColumns

data class IDQuesAnsw(var num: Int, var id: String?, var quesAnsw: String?)

class QnaDB (
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
        val sqlQna1 : String = "DROP TABLE if exists ${QnaDatas.quesData.TABLE_NAME}"
        val sqlQna2 : String = "DROP TABLE if exists ${QnaDatas.answData.TABLE_NAME}"

        if (db != null) {
            db.execSQL(sqlQna1)
            db.execSQL(sqlQna2)
            onCreate(db)
        } //  버전이 변경되면 기존 Table을 삭제후 재생성함.
    }

    fun createDatabase(db: SQLiteDatabase) {
        // 테이블이 존재하지 않는경우 생성
        var sqlQna1: String = "CREATE TABLE if not exists ${QnaDatas.quesData.TABLE_NAME} (" +
                "${BaseColumns._ID} integer primary key autoincrement," +
                "${QnaDatas.quesData.COLUMN_NAME_QUESID} varchar(15)," +
                "${QnaDatas.quesData.COLUMN_NAME_QUESTION} varchar(200)" +
                ");"

        var sqlQna2: String = "CREATE TABLE if not exists ${QnaDatas.answData.TABLE_NAME} (" +
                "${BaseColumns._ID} integer primary key autoincrement," +
                "${QnaDatas.answData.COLUMN_NAME_QUESNUM} integer," +
                "${QnaDatas.answData.COLUMN_NAME_ANSWID} varchar(15)," +
                "${QnaDatas.answData.COLUMN_NAME_ANSWER} varchar(200)" +
                ");"

        db.execSQL(sqlQna1)
        db.execSQL(sqlQna2)
    }

    fun registerQues(id: String, question: String){
        val db =this.writableDatabase
        val values = ContentValues().apply {// insert될 데이터값
            put(QnaDatas.quesData.COLUMN_NAME_QUESID, id)
            put(QnaDatas.quesData.COLUMN_NAME_QUESTION, question)
        }
        val newRowId = db?.insert(QnaDatas.quesData.TABLE_NAME, null, values)
        // 인서트후 인서트된 primary key column의 값(_id) 반환.
    }

    fun registerAnsw(num: Int, id: String, answer: String){
        val db =this.writableDatabase
        val values = ContentValues().apply {// insert될 데이터값
            put(QnaDatas.answData.COLUMN_NAME_QUESNUM, num)
            put(QnaDatas.answData.COLUMN_NAME_ANSWID, id)
            put(QnaDatas.answData.COLUMN_NAME_ANSWER, answer)
        }
        val newRowId = db?.insert(QnaDatas.answData.TABLE_NAME, null, values)
        // 인서트후 인서트된 primary key column의 값(_id) 반환.
    }

    fun getListQues() : MutableList<IDQuesAnsw>{
        val list = mutableListOf<IDQuesAnsw>()
        val db =this.readableDatabase

        // 리턴받고자 하는 컬럼 값의 array
        val projection = arrayOf(BaseColumns._ID,
            QnaDatas.quesData.COLUMN_NAME_QUESID,
            QnaDatas.quesData.COLUMN_NAME_QUESTION)

        //  WHERE "id" = id 구문 적용하는 부분
        val cursor = db.query(
            QnaDatas.quesData.TABLE_NAME, projection,
            null, null, null, null, null)


        while(cursor.moveToNext()) {
            val num = cursor.getInt(0)
            val id = cursor.getString(1)
            val quesAnsw = cursor.getString(2)
            list.add(IDQuesAnsw(num, id, quesAnsw))
        }
        cursor.close()

        return list
    }

    fun getListAnsw(num: String) : MutableList<IDQuesAnsw>{
        val list = mutableListOf<IDQuesAnsw>()
        val db =this.readableDatabase

        // 리턴받고자 하는 컬럼 값의 array
        val projection = arrayOf(BaseColumns._ID,
            QnaDatas.answData.COLUMN_NAME_ANSWID,
            QnaDatas.answData.COLUMN_NAME_ANSWER)

        //  WHERE "id" = id 구문 적용하는 부분
        val selection = "${QnaDatas.answData.COLUMN_NAME_QUESNUM} = ?"
        val selectionArgs = arrayOf(num)

        val cursor = db.query(
            QnaDatas.quesData.TABLE_NAME, projection,
            selection, selectionArgs, null, null, null)


        while(cursor.moveToNext()) {
            val num = cursor.getInt(0)
            val id = cursor.getString(1)
            val quesAnsw = cursor.getString(2)
            list.add(IDQuesAnsw(num, id, quesAnsw))
        }
        cursor.close()

        return list
    }

    fun deleteQuesItem(num : Int) {
        val db =this.writableDatabase
        var p : SQLiteStatement = db.compileStatement(
            "DELETE FROM ${QnaDatas.quesData.TABLE_NAME} " +
                    "WHERE ${BaseColumns._ID} = " + num + ";")
        p.execute()

        p = db.compileStatement(
            "DELETE FROM ${QnaDatas.answData.TABLE_NAME} " +
                    "WHERE ${QnaDatas.answData.COLUMN_NAME_QUESNUM} = " + num + ";")
        p.execute()
    }

    fun deleteAnswItem(num : Int) {
        val db =this.writableDatabase
        var p = db.compileStatement(
            "DELETE FROM ${QnaDatas.answData.TABLE_NAME} " +
                    "WHERE ${BaseColumns._ID} = " + num + ";")
        p.execute()
    }


}