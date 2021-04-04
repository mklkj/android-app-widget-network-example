package io.github.mklkj.androidappwidgetexample.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    load(url)
}
