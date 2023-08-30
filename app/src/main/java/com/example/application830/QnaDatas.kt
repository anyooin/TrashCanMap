package com.example.application830

import android.provider.BaseColumns

object QnaDatas {
    object quesData : BaseColumns {  //  users 라는 DB 테이블의 데이터 컬럼 내용 정리
        const val TABLE_NAME = "qData"
        const val COLUMN_NAME_QUESTION = "question"
        const val COLUMN_NAME_QUESID = "QuesId"
    }

    object answData : BaseColumns {  //  users 라는 DB 테이블의 데이터 컬럼 내용 정리
        const val TABLE_NAME = "aData"
        const val COLUMN_NAME_QUESNUM = "questionNum"
        const val COLUMN_NAME_ANSWID = "AnswId"
        const val COLUMN_NAME_ANSWER = "answer"
    }
}