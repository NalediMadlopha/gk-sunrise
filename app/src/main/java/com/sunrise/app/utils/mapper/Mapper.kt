package com.sunrise.app.utils.mapper

interface Mapper<R, D> {
    fun mapFrom(type: R): D
}