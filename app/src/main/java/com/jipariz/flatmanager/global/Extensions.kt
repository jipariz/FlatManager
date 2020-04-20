package com.jipariz.flatmanager.global

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}


fun <A, R> LiveData<A>.map(block: (A) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(it)
    }
    return result
}
