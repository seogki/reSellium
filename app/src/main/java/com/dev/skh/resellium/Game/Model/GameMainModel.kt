package com.dev.skh.resellium.Game.Model

import java.io.Serializable

/**
 * Created by Seogki on 2018. 9. 28..
 */
data class GameMainModel(val id: String? = null
                         , val platform: String? = null
                         , val title: String? = null
                         , val place: String? = null
                         , val money: String? = null
                         , val date: String? = null
                         , val which: String? = null
                         , val comment: String? = null) : Serializable

