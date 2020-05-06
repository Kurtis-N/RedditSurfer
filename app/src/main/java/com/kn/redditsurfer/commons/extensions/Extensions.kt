@file:JvmName("ExtensionsUtils")
// This would allow us to sse it in this way in Java class:
//ExtensionsUtils.inflate(container, R.layout.news_fragment);

package com.kn.redditsurfer.commons.commons.old

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kn.redditsurfer.R
import com.squareup.picasso.Picasso

//This actually creates a new function of the ViewGroup class - hence why we can access "this" keyword
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher)
    } else {
        Picasso.with(context).load(imageUrl).into(this)
    }
}