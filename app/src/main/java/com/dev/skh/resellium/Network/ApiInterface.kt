package com.dev.skh.resellium.Network

import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Model.SearchKeyModel
import com.dev.skh.resellium.Game.Model.CommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
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
    fun getMainData(@Query("Platform") platform: String): Flowable<MutableList<GameMainModel>>

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
                       , @Query("Gameid") gameid: String): Flowable<MutableList<CommentModel>>


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
    @POST("game/getGameData")
    fun getGameData(@Query("Type") type: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getScrollGameData")
    fun getScrollGameData(@Query("Type") type: String, @Query("Id") id: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerGameData")
    fun getSpinnerGameData(@Query("Type") type: String
                           , @Query("First") first: String
                           , @Query("Second") second: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSpinnerScrollGameData")
    fun getSpinnerScrollGameData(@Query("Type") type: String
                                 , @Query("First") first: String
                                 , @Query("Second") second: String
                                 , @Query("Id") id: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchGameData")
    fun getSearchGameData(@Query("Type") type: String
                          , @Query("Search") search: String): Flowable<MutableList<GameMainModel>>

    @POST("game/getSearchScrollGameData")
    fun getSearchScrollGameData(@Query("Type") type: String
                                , @Query("Id") id: String
                                , @Query("Search") search: String): Flowable<MutableList<GameMainModel>>



    @POST("game/RegisterData")
    fun RegisterData(@Query("Platform") platform: String
                     , @Query("Title") title: String
                     , @Query("Place") place: String
                     , @Query("Money") money: String
                     , @Query("Which") which: String): Flowable<JsonObject>

}
