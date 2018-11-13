package com.dev.skh.resellium.Dagger

import com.dev.skh.resellium.Base.BasePresenter
import com.dev.skh.resellium.Base.BaseRepository
import dagger.Component

/**
 * Created by Seogki on 2018. 11. 8..
 */
@Component
interface MagicBox {
    fun poke(presenter: BasePresenter)
    fun viewModel(repo: BaseRepository)
}