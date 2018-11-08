package com.dev.skh.resellium.Dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Seogki on 2018. 11. 8..
 */

@Module
class ContextModule {
    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }
}
