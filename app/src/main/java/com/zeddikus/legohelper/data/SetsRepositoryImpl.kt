package com.zeddikus.legohelper.data

import com.zeddikus.legohelper.db.RoomDB
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.db.models.SetEntity
import com.zeddikus.legohelper.domain.db.models.SetLineEntity
import com.zeddikus.legohelper.domain.db.models.SetWithLines
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.HomeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetsRepositoryImpl @Inject constructor(
    private val roomDB: RoomDB
):SetsRepository {
    override suspend fun loadSets(): Flow<HomeState> = flow {
        val result = roomDB.databaseDao().getSetsWithLines().map {
            ConstructorSet(it.set.id,it.set.id)
        }

        emit(HomeState.Data(result))
    }

    override suspend fun saveSet(constructorSet: ConstructorSet) {
        roomDB.databaseDao().insertSetWithLines(
            SetWithLines(
               set = SetEntity(constructorSet.id, constructorSet.name),
               lines = constructorSet.lines.map { line ->
                   SetLineEntity(
                       0,
                       setId = constructorSet.id,
                       partId = line.part.id,
                       count = line.count,
                       countFound = line.countFound
                   )
               }
            )
        )
    }
}