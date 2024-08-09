package com.zeddikus.legohelper.data.db

import android.content.Context
import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.db.models.PartEntity
import com.zeddikus.legohelper.data.mappers.mapper
import com.zeddikus.legohelper.domain.SettingsRepository
import com.zeddikus.legohelper.domain.models.ConstructorPart
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.domain.models.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val roomDB: RoomDB,
    private val settingsRepository: SettingsRepository
) : SetsRepository {

    override suspend fun loadSets(): Flow<SetsState> = flow {
        try {
            val result = roomDB.databaseDao().getSetsWithLines().map {
                mapper.mapToDomain(it)
            }

            emit(SetsState.Data(result))
        } catch (e: Exception){
            emit(SetsState.Error)
        }

    }

    override suspend fun loadSet(setId: Int): Flow<SetState> = flow {
        val result = roomDB.databaseDao().getSetWithLines(setId)
        if (result == null) {
            emit(SetState.Empty)
        } else {
            emit(
                SetState.Data(
                    mapper.mapToDomain(result)
                )
            )
        }
    }

    override suspend fun loadSetByLegoId(setId: String): ConstructorSet {
        val result = roomDB.databaseDao().getSetWithLinesByLegoId(setId)
        return mapper.mapToDomain(result)
    }

    override suspend fun loadSetByBaseId(setId: Int): ConstructorSet {
        val result = roomDB.databaseDao().getSetWithLines(setId)
        return mapper.mapToDomain(result!!)
    }

    override suspend fun saveSet(constructorSet: ConstructorSet): Int {
        return roomDB.databaseDao().insertSet(
            mapper.mapToData(constructorSet)
        ).toInt()
    }

    override suspend fun saveSetWithLinesAndParts(constructorSet: ConstructorSet): Int {

        val listParts = mutableSetOf<ConstructorPart>()

        constructorSet.lines.forEach {
            listParts.add(it.value.part)
        }

        return roomDB.databaseDao().insertSetWithLines(
            mapper.mapToDataWithLines(constructorSet),
            mapper.mapToData(listParts.toList())
        )
    }

    override suspend fun saveLineSet(constructorSetLine: ConstructorSetLine): Flow<LineSetState> = flow {
        val lineId = roomDB.databaseDao().insertSetLine(
            mapper.mapToData(constructorSetLine),
        )

        emit(LineSetState.Data(constructorSetLine.copy(lineId = lineId.toInt())))
    }

    override suspend fun loadLine(lineId: Int): Flow<LineSetState> = flow {
        emit(
            LineSetState.Data(
                mapper.mapToDomain(
                    roomDB.databaseDao().getSetLineWithPart(lineId = lineId)
                )
            )
        )
    }

    override suspend fun loadLines(setId: Int, hideCollected: Boolean): Flow<LinesState> = flow {
        val sort = settingsRepository.loadSort() as Settings.Sort
        emit(
            LinesState.Data(
                mapper.mapToDomain(roomDB.databaseDao().getSetLinesWithPartSorted(setId,sort,hideCollected)),
                settingsRepository.loadColumnCount() as Settings.Columns,
                sort
            )
        )
    }

    override suspend fun deleteSet(setId: Int): Flow<SetState> = flow {
        roomDB.databaseDao().deleteSetAndLines(setId)
        emit(SetState.Deleted)
    }

}