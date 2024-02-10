package com.waiyanphyo.betternews.data.local

import androidx.room.TypeConverter
import com.waiyanphyo.betternews.data.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source) = source.name

    @TypeConverter
    fun toSource(name: String) = Source(name, name)
}