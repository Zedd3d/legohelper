package com.zeddikus.legohelper.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.zeddikus.legohelper.domain.db.models.PartEntity
import com.zeddikus.legohelper.domain.db.models.SetEntity
import com.zeddikus.legohelper.domain.db.models.SetLineEntity
import com.zeddikus.legohelper.domain.db.models.SetLineWithPart
import com.zeddikus.legohelper.domain.db.models.SetWithLines

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: SetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLines(lines: List<SetLineEntity>)

    @Transaction
    suspend fun insertSetWithLines(setWithLines: SetWithLines) {
        insertSet(setWithLines.set)
        insertSetLines(setWithLines.lines)
    }

    @Transaction
    @Query("SELECT * FROM sets_table WHERE id = :setId")
    suspend fun getSetWithLines(setId: String): SetWithLines
}

@Dao
interface PartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(part: PartEntity)

    @Transaction
    @Query("SELECT * FROM setlines_table WHERE id = :lineId")
    suspend fun getSetLineWithPart(lineId: Int): SetLineWithPart
}