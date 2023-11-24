package com.example.cuurentworld.db

import androidx.room.TypeConverter
import com.example.cuurentworld.models.Source


class Converters {

    @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}

// source is not supported by the database that's why we convert it