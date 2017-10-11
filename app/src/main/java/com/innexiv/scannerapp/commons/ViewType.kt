package com.innexiv.scannerapp.commons

interface ViewType {
    companion object {
        val TYPE_EQUIPMENT = 0
        val TYPE_NODE = 1
    }
    fun getViewType(): Int
}