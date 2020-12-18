package com.example.moshimultiplejsondemo

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.lang.IllegalArgumentException

class MyModelAdapter {
    @ToJson
    fun toJson(model: MyModel): String {
        // MyModel is data class so .toString() should convert it to correct Json format with
        // image property as list of image path strings
        return model.toString()
    }

    @FromJson
    fun fromJson(reader: JsonReader): MyModel = with(reader) {
        // We need to manually parse the json
        var id: Long? = null
        var singleImage: String? = null
        val imageList = mutableListOf<String>()

        beginObject()
        while (hasNext()) {
            // iterate through the JSON fields
            when (nextName()) {
                "Id" -> id = nextLong() // map the id field
                "image" -> { // map the image field
                    when (peek()) {
                        JsonReader.Token.BEGIN_ARRAY -> {
                            // the case where image field is an array
                            beginArray()
                            while(hasNext()) {
                                val imageFromList = nextString()
                                imageList.add(imageFromList)
                            }
                            endArray()
                        }
                        JsonReader.Token.STRING -> {
                            // the case where image field is single string
                            singleImage = nextString()
                        }
                        else -> skipValue()
                    }
                }
                else -> skipValue()
            }
        }
        endObject()
        id ?: throw IllegalArgumentException("Id should not be null")
        val images = if (singleImage != null) {
            listOf(singleImage)
        } else {
            imageList
        }
        MyModel(Id = id, images = images)
    }
}
