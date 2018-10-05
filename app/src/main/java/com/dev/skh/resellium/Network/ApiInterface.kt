package com.dev.skh.resellium.Network

import com.dev.skh.resellium.Game.Model.Ps4MainModel
import com.dev.skh.resellium.Game.Model.SwitchMainModel
import com.dev.skh.resellium.Game.Model.XboxMainModel
import com.dev.skh.resellium.Main.Model.HoriModel
import com.google.gson.JsonObject
import io.reactivex.Flowable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Seogki on 2018. 6. 26..
 */
interface ApiInterface {

    /***
     *  메인
     */
    @POST("main/getMainData")
    fun getMainData(@Query("Platform") platform: String): Flowable<MutableList<HoriModel>>

    /***
     * 게임
     */
    /***PS4
     *
     */

    @POST("game/getPs4Data")
    fun getPs4Data(): Flowable<MutableList<Ps4MainModel>>

    @POST("game/getScrollPs4Data")
    fun getScrollPs4Data(@Query("Id") id: String): Flowable<MutableList<Ps4MainModel>>

    @POST("game/getSpinnerPs4Data")
    fun getSpinnerPs4Data(@Query("First") first: String
                          , @Query("Second") second: String): Flowable<MutableList<Ps4MainModel>>

    @POST("game/getSpinnerScrollPs4Data")
    fun getSpinnerScrollPs4Data(@Query("First") first: String
                                , @Query("Second") second: String
                                , @Query("Id") id: String): Flowable<MutableList<Ps4MainModel>>

    @POST("game/getSearchPs4Data")
    fun getSearchPs4Data(@Query("Search") search: String): Flowable<MutableList<Ps4MainModel>>

    @POST("game/getSearchScrollPs4Data")
    fun getSearchScrollPs4Data(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<Ps4MainModel>>


    /***
     * XBOX
     */

    @POST("game/getxboxData")
    fun getxboxData(): Flowable<MutableList<XboxMainModel>>

    @POST("game/getScrollxboxData")
    fun getScrollxboxData(@Query("Id") id: String): Flowable<MutableList<XboxMainModel>>


    @POST("game/getSpinnerxboxData")
    fun getSpinnerxboxData(@Query("First") first: String
                          , @Query("Second") second: String): Flowable<MutableList<XboxMainModel>>

    @POST("game/getSpinnerScrollxboxData")
    fun getSpinnerScrollxboxData(@Query("First") first: String
                                , @Query("Second") second: String
                                , @Query("Id") id: String): Flowable<MutableList<XboxMainModel>>

    @POST("game/getSearchXboxData")
    fun getSearchXboxData(@Query("Search") search: String): Flowable<MutableList<XboxMainModel>>

    @POST("game/getSearchScrollXboxData")
    fun getSearchScrollXboxData(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<XboxMainModel>>


    /***
     * SWITCH
     */

    @POST("game/getswitchData")
    fun getSwitchData(): Flowable<MutableList<SwitchMainModel>>


    @POST("game/getScrollswitchData")
    fun getScrollswitchData(@Query("Id") id: String): Flowable<MutableList<SwitchMainModel>>


    @POST("game/getSpinnerswitchData")
    fun getSpinnerswitchData(@Query("First") first: String
                           , @Query("Second") second: String): Flowable<MutableList<SwitchMainModel>>

    @POST("game/getSpinnerScrollswitchData")
    fun getSpinnerScrollswitchData(@Query("First") first: String
                                 , @Query("Second") second: String
                                 , @Query("Id") id: String): Flowable<MutableList<SwitchMainModel>>


    @POST("game/getSearchSwitchData")
    fun getSearchSwitchData(@Query("Search") search: String): Flowable<MutableList<SwitchMainModel>>

    @POST("game/getSearchScrollSwitchData")
    fun getSearchScrollSwitchData(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<SwitchMainModel>>

    @POST("game/RegisterData")
    fun RegisterData(@Query("Platform") platform: String
                     , @Query("Title") title: String
                     , @Query("Place") place: String
                     , @Query("Money") money: String
                     , @Query("Which") which: String): Flowable<JsonObject>

}
