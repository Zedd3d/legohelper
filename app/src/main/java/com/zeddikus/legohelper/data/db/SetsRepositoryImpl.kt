package com.zeddikus.legohelper.data.db

import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.db.models.PartEntity
import com.zeddikus.legohelper.data.mappers.mapper
import com.zeddikus.legohelper.domain.models.ConstructorPart
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetsRepositoryImpl @Inject constructor(
    private val roomDB: RoomDB
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

    override suspend fun saveSet(constructorSet: ConstructorSet): Int {

        val listParts = mutableSetOf<ConstructorPart>()

        constructorSet.lines.forEach {
            listParts.add(it.value.part)
        }

        return roomDB.databaseDao().insertSetWithLines(
            mapper.mapToData(constructorSet),
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

    override suspend fun loadLines(setId: Int): Flow<LinesState> = flow {
        emit(
            LinesState.Data(
                mapper.mapToDomain(roomDB.databaseDao().getSetLinesWithPart(setId))
            )
        )
    }
}