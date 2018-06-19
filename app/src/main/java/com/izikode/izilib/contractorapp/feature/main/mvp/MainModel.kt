package com.izikode.izilib.contractorapp.feature.main.mvp

import android.content.Context
import com.izikode.izilib.contractorapp.mvp.AbstractMainModel

class MainModel(private val context: Context) : AbstractMainModel() {

    override fun isAuthenticated(): Boolean {
        return false
    }

}