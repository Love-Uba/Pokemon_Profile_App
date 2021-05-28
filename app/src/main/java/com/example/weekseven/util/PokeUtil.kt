package com.example.weekseven.util

fun getPokeId(url: String): String {
    var id = ""

    for (i in 1..url.length) {
        if (i == url.lastIndex) {
            break
        }
        if (url[i - 1] != 'v' && url[i].isDigit()) {
            id += "${url[i]}"
        }
    }

    return id
}