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
    override suspend fun loadSet(setLegoId: String, setId: Int): Flow<SetState> = flow {
        if (!isConnected()) {
            emit(SetState.Error(ErrorTypes.NoNetwork))
            return@flow
        }

        val constructorSet = try {
            setsRepository.loadSetByBaseId(setId)
        } catch (e: Exception) {
            emit(SetState.Error(ErrorTypes.Unknown))
            return@flow
        }

        val idForRequest = normalizeSetIdForRequest(setLegoId)

        val response = try {
            api.getStartData(idForRequest)
        } catch (e: Exception) {
            emit(SetState.Error(ErrorTypes.Unknown))
            return@flow
        }

        if (!response.isSuccessful) {
            emit(SetState.Error(ErrorTypes.HttpError(response.code())))
            return@flow
        }

        val body = try {
            response.body()?.string().orEmpty()
        } catch (e: Exception) {
            emit(SetState.Error(ErrorTypes.Unknown))
            return@flow
        }

        if (body.isBlank()) {
            emit(SetState.Error(ErrorTypes.NoData))
            return@flow
        }

        if (containsNoData(body)) {
            emit(SetState.Error(ErrorTypes.NoData))
            return@flow
        }

        val constructorSetToSave = try {
            handleBody(body, constructorSet)
        } catch (e: Exception) {
            emit(SetState.Error(ErrorTypes.Unknown))
            return@flow
        }

        if (constructorSetToSave.lines.isEmpty()) {
            emit(SetState.Error(ErrorTypes.NoData))
            return@flow
        }

        try {
            setsRepository.saveSetWithLinesAndParts(constructorSetToSave)
        } catch (e: Exception) {
            emit(SetState.Error(ErrorTypes.Unknown))
            return@flow
        }

        emit(SetState.Data(constructorSetToSave))
    }

    private fun normalizeSetIdForRequest(setLegoId: String): String {
        val trimmed = setLegoId.trim()
        return if (trimmed.contains("-")) trimmed else "$trimmed-1"
    }

    private fun containsNoData(body: String): Boolean {
        val normalizedBody = body.lowercase()
        return normalizedBody.contains("no item(s) were found".lowercase())
    }

    private fun handleBody(body: String, constructorSet: ConstructorSet): ConstructorSet {
        val startTable = body.indexOfIgnoreCase("Regular Items:")
        if (startTable == -1) {
            return constructorSet
        }

        val endMarkers = listOf("<B>Summary:</B>", "Extra Items:", "Counterparts:")
        val endTable = endMarkers
            .map { marker -> body.indexOfIgnoreCase(marker, startTable) }
            .filter { it > startTable }
            .minOrNull()
            ?: body.length

        if (endTable <= startTable) {
            return constructorSet
        }

        val bodyAnswer = body
            .substring(startTable, endTable)
            .replace("\n", "")
            .replace("<TR", "\n<TR")

        val listData = bodyAnswer.split("\n")

        for (line in listData) {
            if (!line.contains("/img.bricklink.com", ignoreCase = true)) {
                continue
            }
            loadDetailDescription(line, constructorSet)
        }

        return constructorSet
    }

    private fun loadDetailDescription(input: String, constructorSet: ConstructorSet) {
        val imageUrl = getContentBetweenSubstrings(input, "/img.bricklink.com", "'").result
        if (imageUrl.isBlank()) return

        val name = getContentBetweenSubstrings(input, "ALT=\"", "\"").result.trim()

        var searchResult = getContentBetweenSubstrings(input, "catalogitem.page?", "&")
        if (searchResult.result.contains("</A>")) {
            searchResult = getContentBetweenSubstrings(input, "catalogitem.page?", "\"")
        }

        val detailNumber = extractDetailNumber(searchResult.result) ?: return

        val colorCode = getContentBetweenSubstrings(
            input,
            "&idColor=",
            "\"",
            searchResult.startSymbol
        ).result.trim()
        if (colorCode.isBlank()) return

        val detailCount = getContentBetweenSubstrings(
            input,
            "\"RIGHT\">&nbsp;",
            "&nbsp;</TD>"
        ).result.trim().toIntOrNull() ?: return

        val detailId = "${detailNumber}_$colorCode"

        val resultLine = constructorSet.lines[detailId]
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

        if (resultLine.part.imgUrl.isEmpty()) {
            resultLine.part = resultLine.part.copy(
                imgUrl = imageUrl,
                colorCode = colorCode
            )
        }

        if (resultLine.part.name.isBlank() && name.isNotBlank()) {
            resultLine.part = resultLine.part.copy(name = name)
        }

        resultLine.count = detailCount
        constructorSet.lines[detailId] = resultLine
    }

    private fun extractDetailNumber(rawValue: String): String? {
        val cleanedValue = rawValue.trim()
        if (cleanedValue.isBlank()) return null

        val parts = cleanedValue.replace("=", "\n").split("\n")
        return parts.getOrNull(1)?.trim()?.takeIf { it.isNotBlank() }
    }

    private fun getContentBetweenSubstrings(
        input: String,
        startTag: String,
        endTag: String,
        startSearchSymbol: Int = 0,
        occurrence: Int = 1
    ): SearchResult {
        var startSymbol = input.indexOf(startTag, startSearchSymbol)
        if (startSymbol == -1) return SearchResult("", 0, 0)

        startSymbol += startTag.length
        val endSymbol = input.indexOf(endTag, startSymbol)
        if (endSymbol == -1) return SearchResult("", 0, 0)

        val result = input.substring(startSymbol, endSymbol)
        return SearchResult(result, startSymbol, endSymbol)
    }

    private fun String.indexOfIgnoreCase(value: String, startIndex: Int = 0): Int {
        return indexOf(value, startIndex = startIndex, ignoreCase = true)
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
