package com.zeddikus.legohelper.data.db

import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.mappers.mapper
import com.zeddikus.legohelper.domain.models.ConstructorSet
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

    override suspend fun saveSet(constructorSet: ConstructorSet) {
        roomDB.databaseDao().insertSetWithLines(
            mapper.mapToData(constructorSet)
        )
    }
}