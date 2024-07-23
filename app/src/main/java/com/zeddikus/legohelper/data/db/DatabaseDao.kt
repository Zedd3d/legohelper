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
    suspend fun insertSet(set: SetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLines(lines: List<SetLineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLine(line: SetLineEntity): Long

    @Query("DELETE FROM setlines_table WHERE setId = :setId")
    suspend fun deleteSetLines(setId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParts(parts: List<PartEntity>)

    @Transaction
    suspend fun insertSetWithLines(setWithLines: SetWithLines, parts:List<PartEntity>): Int {
        val result = insertSet(setWithLines.set)
        //deleteSetLines(setWithLines.set.id)
        insertSetLines(setWithLines.lines)

        insertParts(parts)
        return result.toInt()
    }

    @Transaction
    @Query("SELECT * FROM sets_table WHERE id = :setId")
    suspend fun getSetWithLines(setId: Int): SetWithLines?

    @Transaction
    @Query("SELECT * FROM sets_table WHERE setIdExt = :setId")
    suspend fun getSetWithLinesByLegoId(setId: String): SetWithLines

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPart(part: PartEntity)

    @Transaction
    @Query("SELECT * FROM setlines_table WHERE id = :lineId")
    suspend fun getSetLineWithPart(lineId: Int): SetLineWithPart

    @Transaction
    @Query("SELECT * FROM setlines_table WHERE setId = :setId")
    suspend fun getSetLinesWithPart(setId: Int): List<SetLineWithPart>

    @Transaction
    @Query("SELECT * FROM sets_table")
    suspend fun getSetsWithLines(): List<SetWithLines>
}