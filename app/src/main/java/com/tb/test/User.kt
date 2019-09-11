package com.tb.test

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class User : BaseObservable() {


    @Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyChange()
        }

    var age: String? = null


}