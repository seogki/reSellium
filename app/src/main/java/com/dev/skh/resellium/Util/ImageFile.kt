package com.dev.skh.resellium.Util

import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import java.util.*

/**
 * Created by Seogki on 2018. 6. 8..
 */
open class ImageFile{

    fun fetchAllImages(activity: AppCompatActivity): ArrayList<String> {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)


        val imageCursor = activity.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // 모든 개체 출력
                null, null,
                MediaStore.Images.Media._ID + " DESC")// DATA, _ID를 출력
        // 정렬 안 함

        val result = ArrayList<String>(imageCursor!!.count)
        val dataColumnIndex = imageCursor.getColumnIndex(projection[0])
//        val idColumnIndex = imageCursor.getColumnIndex(projection[1])

        if (imageCursor.moveToFirst()) {
            do {
                val filePath = imageCursor.getString(dataColumnIndex)
//                val imageId = imageCursor.getString(idColumnIndex)

                //                Uri thumbnailUri = uriToThumbnail(imageId);

                val imageUri = Uri.parse(filePath)
                // 원본 이미지와 썸네일 이미지의 uri를 모두 담을 수 있는 클래스를 선언합니다.
                result.add(imageUri.toString())
            } while (imageCursor.moveToNext())
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close()
        return result
    }
}