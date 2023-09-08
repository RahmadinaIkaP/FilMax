package com.rahmadina.ika.movieapp_kotlin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id : String = "",
    var email : String = "",
    var name : String = ""
) : Parcelable
