package com.sunrise.app.core

import com.sunrise.app.utils.domain.Status

open class BaseViewState(val baseStatus: Status, val baseError: String?) {

    fun isLoading() = baseStatus == Status.LOADING

    fun getErrorMessage() = baseError

    fun shouldShowErrorMessage() = baseError != null

}
