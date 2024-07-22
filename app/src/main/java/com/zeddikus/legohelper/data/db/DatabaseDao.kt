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
interface DatabaseDao {
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
    suspend fun getSetWithLines(setId: Int): SetWithLines?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(part: PartEntity)

    @Transaction
    @Query("SELECT * FROM setlines_table WHERE id = :lineId")
    suspend fun getSetLineWithPart(lineId: Int): SetLineWithPart?

    @Transaction
    @Query("SELECT * FROM sets_table")
    suspend fun getSetsWithLines(): List<SetWithLines>
}