package com.izikode.izilib.contractorapp.feature.main.mvp

import com.izikode.izilib.contractorapp.mvp.AbstractMainPresenter

class MainPresenter : AbstractMainPresenter() {

    override fun init() {
        if (model().isAuthenticated()) {
            view().goToAccount()
        } else {
            view().goToLogin()
        }
    }

}