package com.zeddikus.legohelper.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.zeddikus.legohelper.data.models.SearchResult
import com.zeddikus.legohelper.di.ErrorTypes
import com.zeddikus.legohelper.domain.SetsRepository
import com.zeddikus.legohelper.domain.models.ConstructorPart
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val context: Context,
    private val api: LegoBrickApi,
    private val setsRepository: SetsRepository
) : NetworkRepository {
    override suspend fun loadSet(setId: String): Flow<SetState> = flow {
        if (!isConnected()) {
            emit(SetState.Error(ErrorTypes.NoNetwork))
            return@flow
        }

        val constructorSet = setsRepository.loadSetByLegoId(setId)

        val idForRequest = if (setId.contains("-")) {setId} else {setId + "-1"}

        val response = api.getStartData(idForRequest)
        val b = response.body()?.string()?:""

        if (b.contains("No Item(s) were found")) {
            emit(SetState.Error(ErrorTypes.NoData))
            return@flow
        }

        val constructorSetToSave = handleBody(b,constructorSet)

        setsRepository.saveSetWithLinesAndParts(constructorSetToSave)

        emit(SetState.Data(constructorSetToSave))
    }

    private fun handleBody(b: String, constructorSet:ConstructorSet): ConstructorSet {
        val startTable = b.lowercase().indexOf("Regular Items:".lowercase())
        val endTable = b.lowercase().indexOf("<B>Summary:</B>".lowercase(),startTable)

        val splitter = "<TR"

        val bodyAnswer = b.substring(startTable, endTable - startTable).replace("\n","").replace(splitter,"\n")
        val listData = bodyAnswer.split("\n")

        for (line in listData) {
            if (line.contains("Extra Items:")) {
                break
            }

            if (line.contains("Counterparts:")) {
                break
            }

            if (line.contains("Summary:")) {
                break
            }

            loadDetailDescription(line, constructorSet)
        }

        return constructorSet
    }

    private fun loadDetailDescription(input: String, constructorSet: ConstructorSet) {

        var searchResult = getContentBetweenSubstrings(input, "/img.bricklink.com", "'")
        val imageUrl = searchResult.result

        if (imageUrl.isEmpty()) return

        val name = getContentBetweenSubstrings(input, "ALT=\"", "\"").result

        searchResult = getContentBetweenSubstrings(input, "catalogitem.page?", "&")
        if (searchResult.result.contains("</A>")) {
            searchResult = getContentBetweenSubstrings(input, "catalogitem.page?", "\"")
        }

        var detailNumber = searchResult.result
        detailNumber = detailNumber.replace("=", "\n").split("\n")[1]

        val colorCode =
            getContentBetweenSubstrings(input, "&idColor=", "\"", searchResult.startSymbol).result

        val detailCount =
            getContentBetweenSubstrings(input, "\"RIGHT\">&nbsp;", "&nbsp;</TD>").result.toInt()

        val detailId = "" + detailNumber + "_" + colorCode

        val resultLine = constructorSet.lines.get(detailId)
            ?: ConstructorSetLine(
                lineId = 0,
                setId = constructorSet.id,
                part = ConstructorPart(
                    name = name,
                    id = detailId,
                    imgUrl = imageUrl,
                    colorCode = colorCode
                ),
                count = 0,
                countFound = 0,
            )

        if (resultLine.part.imgUrl.isEmpty()){
            resultLine.part = resultLine.part.copy(
                imgUrl = imageUrl,
                colorCode = colorCode
            )
        }

        resultLine.count = detailCount

        constructorSet.lines[detailId] = resultLine
    }


    private fun getContentBetweenSubstrings(input: String, startTag: String, endTag: String, startSearchSymbol: Int = 0, occurrence: Int = 1): SearchResult {
        var startSymbol = input.indexOf(startTag, startSearchSymbol)
        if (startSymbol == -1) return SearchResult("", 0, 0)

        startSymbol += startTag.length
        val endSymbol = input.indexOf(endTag, startSymbol)
        if (endSymbol == -1) return SearchResult("", 0, 0)

        val result = input.substring(startSymbol, endSymbol)
        return SearchResult(result, startSymbol, endSymbol)
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

