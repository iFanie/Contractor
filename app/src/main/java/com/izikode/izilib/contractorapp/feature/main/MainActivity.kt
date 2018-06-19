package com.izikode.izilib.contractorapp.feature.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.izikode.izilib.contractorapp.R
import com.izikode.izilib.contractorapp.feature.main.mvp.MainModel
import com.izikode.izilib.contractorapp.feature.main.mvp.MainPresenter
import com.izikode.izilib.contractorapp.feature.main.mvp.MainView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model = MainModel(this)
        val view = MainView(this)
        val presenter = MainPresenter()

        presenter.setup(model).setup(view)
        presenter.init()
    }
}
