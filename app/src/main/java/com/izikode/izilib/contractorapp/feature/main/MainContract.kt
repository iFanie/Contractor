package com.izikode.izilib.contractorapp.feature.main

import com.izikode.izilib.contractorannotations.Contract
import com.izikode.izilib.contractorapp.base.BaseModel
import com.izikode.izilib.contractorapp.base.BasePresenter

@Contract(target = "com.izikode.izilib.contractorapp.mvp")
interface MainContract {

    @Contract.Model(base = BaseModel::class)
    interface Model {

        fun isAuthenticated(): Boolean

    }

    @Contract.View
    interface View {

        fun goToAccount()

        fun goToLogin()

    }

    @Contract.Presenter(base = BasePresenter::class)
    interface Presenter {

        /* fun init() from BasePresenter */

    }

}