package com.tb.test.activity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class TestInfo : BaseObservable() {

    @Bindable
    var currentpage: Int = 0
        set(value) {
            field = value
            notifyChange()
        }
    var list: List<X> = listOf()
    var pagesize: String = ""
    var totalcount: Int = 0
    var totalpage: Int = 0
}

data class X(
    var Address: String = "",
    var City: String = "",
    var District: String = "",
    var PostNumber: String = "",
    var Province: String = ""
)