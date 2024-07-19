package com.zeddikus.legohelper.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeddikus.legohelper.domain.db.models.PartEntity
import com.zeddikus.legohelper.domain.db.models.SetEntity
import com.zeddikus.legohelper.domain.db.models.SetLineEntity

@Database(
    entities = [
        SetEntity::class,
        SetLineEntity::class,
        PartEntity::class,
    ],
    version = 1
)
@TypeConverters(
//    Converters::class,
//    ListConverter::class
)
abstract class RoomDB : RoomDatabase() {

//    abstract fun newsDao(): NewsDao
//    abstract fun profileDao(): ProfileDao
//    abstract fun profileCompanyDao(): ProfileCompanyDao
}
