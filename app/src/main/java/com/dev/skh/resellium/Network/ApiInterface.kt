package com.dev.skh.resellium.Network

import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Model.SearchKeyModel
import com.dev.skh.resellium.Game.Model.GameCommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
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

    @POST("main/getNewMainData")
    fun getNewMainData(): Flowable<MutableList<BoardMainModel>>

    @POST("main/getBestMainData")
    fun getBestMainData(): Flowable<MutableList<BoardMainModel>>

    @POST("main/getWorstMainData")
    fun getWorstMainData(): Flowable<MutableList<BoardMainModel>>

    /***
     *  보드
     */
    @POST("board/getBoardData")
    fun getBoardData(): Flowable<MutableList<BoardMainModel>>

    @POST("board/getScrollBoardData")
    fun getScrollBoardData(@Query("Id") id: String): Flowable<MutableList<BoardMainModel>>

    @POST("board/getSpinnerBoardData")
    fun getSpinnerBoardData(@Query("Spinner") spinner: String): Flowable<MutableList<BoardMainModel>>

    @POST("board/getSpinnerScrollBoardData")
    fun getSpinnerScrollBoardData(@Query("Spinner") spinner: String
                                  , @Query("Id") id: String): Flowable<MutableList<BoardMainModel>>


    @POST("board/getSearchBoardData")
    fun getSearchBoardData(@Query("Search") search: String): Flowable<MutableList<BoardMainModel>>

    @POST("board/getSearchScrollBoardData")
    fun getSearchScrollBoardData(@Query("Search") search: String
                                 , @Query("Id") id: String): Flowable<MutableList<BoardMainModel>>

    @POST("board/registerBoardData")
    fun registerBoardData(@Query("Title") title: String
                          , @Query("Review") review: String
                          , @Query("Grade") grade: String
                          , @Query("Platform") platform: String): Flowable<JsonObject>


    @POST("board/getKeywordBoardData")
    fun getKeywordBoardData(): Flowable<MutableList<SearchKeyModel>>

    /***
     * 게임
     */


    @POST("game/getCommentData")
    fun getCommentData(@Query("Type") type: String
                       , @Query("Gameid") gameid: String): Flowable<MutableList<GameCommentModel>>


    @POST("game/registerCommentData")
    fun registerCommentData(@Query("Type") type: String
                            , @Query("Gameid") gameid: String
                            , @Query("Comment") comment: String): Flowable<JsonObject>


    @POST("normal/setReport")
    fun setReport(@Query("Platform") platform: String
                  , @Query("Curid") id: String
                  , @Query("Title") title: String
                  , @Query("Date") date: String): Flowable<JsonObject>

    /***PS4
     *
     */

    @POST("game/getPs4Data")
    fun getPs4Data(): Flowable<MutableList<GameMainModel>>

    @POST("game/getScrollPs4Data")
    fun getScrollPs4Data(@Query("Id") id: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerPs4Data")
    fun getSpinnerPs4Data(@Query("First") first: String
                          , @Query("Second") second: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerScrollPs4Data")
    fun getSpinnerScrollPs4Data(@Query("First") first: String
                                , @Query("Second") second: String
                                , @Query("Id") id: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchPs4Data")
    fun getSearchPs4Data(@Query("Search") search: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchScrollPs4Data")
    fun getSearchScrollPs4Data(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<GameMainModel>>


    /***
     * XBOX
     */

    @POST("game/getxboxData")
    fun getxboxData(): Flowable<MutableList<GameMainModel>>

    @POST("game/getScrollxboxData")
    fun getScrollxboxData(@Query("Id") id: String): Flowable<MutableList<GameMainModel>>


    @POST("game/getSpinnerxboxData")
    fun getSpinnerxboxData(@Query("First") first: String
                           , @Query("Second") second: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerScrollxboxData")
    fun getSpinnerScrollxboxData(@Query("First") first: String
                                 , @Query("Second") second: String
                                 , @Query("Id") id: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchXboxData")
    fun getSearchXboxData(@Query("Search") search: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchScrollXboxData")
    fun getSearchScrollXboxData(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<GameMainModel>>


    /***
     * SWITCH
     */

    @POST("game/getswitchData")
    fun getSwitchData(): Flowable<MutableList<GameMainModel>>


    @POST("game/getScrollswitchData")
    fun getScrollswitchData(@Query("Id") id: String): Flowable<MutableList<GameMainModel>>


    @POST("game/getSpinnerswitchData")
    fun getSpinnerswitchData(@Query("First") first: String
                             , @Query("Second") second: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerScrollswitchData")
    fun getSpinnerScrollswitchData(@Query("First") first: String
                                   , @Query("Second") second: String
                                   , @Query("Id") id: String): Flowable<MutableList<GameMainModel>>


    @POST("game/getSearchSwitchData")
    fun getSearchSwitchData(@Query("Search") search: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchScrollSwitchData")
    fun getSearchScrollSwitchData(@Query("Id") id: String, @Query("Search") search: String): Flowable<MutableList<GameMainModel>>

    @POST("game/RegisterData")
    fun RegisterData(@Query("Platform") platform: String
                     , @Query("Title") title: String
                     , @Query("Place") place: String
                     , @Query("Money") money: String
                     , @Query("Which") which: String): Flowable<JsonObject>

}
