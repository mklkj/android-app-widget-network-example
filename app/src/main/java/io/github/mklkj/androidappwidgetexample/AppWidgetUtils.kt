package io.github.mklkj.androidappwidgetexample

import android.os.Bundle

fun Bundle.toPrintableString(): String {
    return keySet().map {
        it to get(it)
    }.map { (key, value) ->
        key to when (value) {
            is IntArray -> value.toList()
            else -> value
        }
    }.joinToString(", ") { (key, value) ->
        "$key: $value"
    }
}
