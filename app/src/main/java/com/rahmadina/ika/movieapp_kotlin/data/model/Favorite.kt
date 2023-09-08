package com.rahmadina.ika.movieapp_kotlin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
    var idUser : String = "",
    var id : String = "",
    var name : String = "",
    var img : String = "",
    var type : String = ""
) : Parcelable
