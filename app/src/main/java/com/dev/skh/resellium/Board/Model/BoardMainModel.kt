package com.dev.skh.resellium.Board.Model

import java.io.Serializable

/**
 * Created by Seogki on 2018. 10. 11..
 */


data class BoardMainModel(val id: String? = null
                          , val title: String? = null
                          , val review: String? = null
                          , val grade: String? = null
                          , val date: String? = null
                          , val platform: String? = null
                          , val comment: String? = null) : Serializable