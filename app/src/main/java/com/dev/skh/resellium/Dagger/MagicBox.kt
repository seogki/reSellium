package com.dev.skh.resellium.Dagger

import com.dev.skh.resellium.Base.BasePresenter
import dagger.Component

/**
 * Created by Seogki on 2018. 11. 8..
 */
@Component
interface MagicBox{
    fun poke(presenter: BasePresenter)
}