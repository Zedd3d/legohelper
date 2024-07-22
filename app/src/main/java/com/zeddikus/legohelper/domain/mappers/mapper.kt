package com.zeddikus.legohelper.domain.mappers

import com.zeddikus.legohelper.domain.db.models.SetEntity
import com.zeddikus.legohelper.domain.db.models.SetLineEntity
import com.zeddikus.legohelper.domain.db.models.SetWithLines
import com.zeddikus.legohelper.domain.models.ConstructorPart
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine

object mapper {

    fun mapToDomain(setWithLines: SetWithLines): ConstructorSet {
        return ConstructorSet(
            id = setWithLines.set.id,
            setIdExt = setWithLines.set.setIdExt,
            name = setWithLines.set.name,
            lines = setWithLines.lines.map {
                ConstructorSetLine(
                    part = ConstructorPart(id = it.partId, name = it.partId),
                    count = it.count,
                    countFound = it.countFound
                )
            }
        )
    }

    fun mapToData(constructorSet: ConstructorSet): SetWithLines {
        return SetWithLines(
            set = SetEntity(id = constructorSet.id, setIdExt = constructorSet.setIdExt, name = constructorSet.name),
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
    }
}