package com.dev.skh.resellium.Dagger;

import com.dev.skh.resellium.Board.BoardMainPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Seogki on 2018. 11. 8..
 */

@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface ApiComponent {
    void inject(BoardMainPresenter boardMainPresenter);

}
