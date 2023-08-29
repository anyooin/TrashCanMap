package com.example.application830

import android.provider.BaseColumns

object AddressDatas {
    object addressData : BaseColumns {  //  users 라는 DB 테이블의 데이터 컬럼 내용 정리
        const val TABLE_NAME = "address"
        const val COLUMN_NAME_ADDR = "addr"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_STATE = "state"
    }
}