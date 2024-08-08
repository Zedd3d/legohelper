package com.zeddikus.legohelper.data.mappers

import com.zeddikus.legohelper.domain.db.models.PartEntity
import com.zeddikus.legohelper.domain.db.models.SetEntity
import com.zeddikus.legohelper.domain.db.models.SetLineEntity
import com.zeddikus.legohelper.domain.db.models.SetLineWithPart
import com.zeddikus.legohelper.domain.db.models.SetWithLines
import com.zeddikus.legohelper.domain.models.ConstructorPart
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine

object mapper {

    fun mapToDomain(setWithLines: SetWithLines): ConstructorSet {

        val mm = mutableMapOf<String, ConstructorSetLine>()

        setWithLines.lines.forEach {
            mm.set(it.partId,
                mapToDomain(it)
            )
        }

        return ConstructorSet(
            id = setWithLines.set.id,
            setIdExt = setWithLines.set.setIdExt,
            name = setWithLines.set.name,
            lines = mm
        )
    }

    fun mapToDomain(setLineEntity: SetLineEntity): ConstructorSetLine {
        return ConstructorSetLine(
            lineId = setLineEntity.id,
            setId = setLineEntity.setId,
            part = ConstructorPart(id = setLineEntity.partId, name = setLineEntity.partId),
            count = setLineEntity.count,
            countFound = setLineEntity.countFound,
        )
    }

    fun mapToDomain(setLineWithPart: SetLineWithPart): ConstructorSetLine {
        return ConstructorSetLine(
            lineId = setLineWithPart.setLine.id,
            setId = setLineWithPart.setLine.setId,
            part = ConstructorPart(
                id = setLineWithPart.part.id,
                name = setLineWithPart.part.name,
                imgUrl = setLineWithPart.part.imgUrl,
                colorCode = setLineWithPart.part.colorCode
            ),
            count = setLineWithPart.setLine.count,
            countFound = setLineWithPart.setLine.countFound,
        )
    }

    fun mapToDataWithLines(constructorSet: ConstructorSet): SetWithLines {
        val ml = mutableListOf<SetLineEntity>()

        constructorSet.lines.forEach { constSet ->
            ml.add(
                mapToData(constSet.value)
            )
        }

        return SetWithLines(
            set = SetEntity(id = constructorSet.id, setIdExt = constructorSet.setIdExt, name = constructorSet.name),
            lines = ml
        )
    }

    fun mapToData(constructorSet: ConstructorSet): SetEntity {
        return SetEntity(
            id = constructorSet.id,
            setIdExt = constructorSet.setIdExt,
            name = constructorSet.name
        )
    }

    fun mapToData(line: ConstructorSetLine): SetLineEntity {
        return SetLineEntity(
            id = line.lineId,
            setId = line.setId,
            partId = line.part.id,
            count = line.count,
            countFound = line.countFound
        )
    }

    fun mapToData(listParts: List<ConstructorPart>): List<PartEntity>{

        return listParts.map {
            PartEntity(
                id = it.id,
                name = it.name,
                imgUrl = it.imgUrl,
                colorCode = it.colorCode
            )
        }
    }

    fun mapToDomain(setLinesWithPart: List<SetLineWithPart>): List<ConstructorSetLine> {
        return setLinesWithPart.map {
            mapToDomain(it)
        }
    }
}