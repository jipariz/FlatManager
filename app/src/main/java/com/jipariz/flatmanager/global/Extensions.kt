package com.jipariz.flatmanager.global

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.IdRes
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}
