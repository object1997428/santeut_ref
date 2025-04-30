package com.santeut.ui.map

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor() : ViewModel() {

    val plantName = mutableStateOf("")
    val plantDescription = mutableStateOf("")
    val crawlPlantName = mutableStateOf("")
    val crawlPlantDescription = mutableStateOf("")
    val name = mutableStateOf("")
    val isLoading = mutableStateOf(true)

    fun identifyPlant(file: File) {

        viewModelScope.launch(Dispatchers.IO) {
            val filePath = file.absolutePath
            val base64Image = encodeFileToBase64Binary(filePath)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://plant.id/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val plantIdApi = retrofit.create(PlantIdApi::class.java)

            val request = PlantIdentificationRequest(images = base64Image, similar_images = true)
            val response = plantIdApi.identifyPlant(request)

            if (response.isSuccessful) {
                val jsonResponse = response.body()?.string()
                if (jsonResponse != null) {
                    val jsonObject = JSONObject(jsonResponse)
                    val resultObject = jsonObject.getJSONObject("result")
                    val classificationObject = resultObject.getJSONObject("classification")
                    val suggestionsArray = classificationObject.getJSONArray("suggestions")

                    if (suggestionsArray.length() > 0) {
                        val suggestionObject = suggestionsArray.getJSONObject(0)
                        plantName.value = suggestionObject.getString("name")
                        val detailsObject = suggestionObject.getJSONObject("details")
                        val descriptionObject =
                            detailsObject.getJSONObject("description")
                        plantDescription.value = descriptionObject.getString("value")

                        Log.d("Plant Name", "${plantName.value}")

                        val url =
                            "https://terms.naver.com/search.naver?query=${plantName.value}&searchType=text&dicType=&subject="
                        val docs = Jsoup.connect(url).get()

                        val h4Element = docs.selectFirst("h4")

                        if (h4Element != null) {
                            val keywordText = h4Element.selectFirst(".keyword a")?.text()
                            val descText = h4Element.selectFirst(".desc")?.text()

                            val titleText = "$keywordText $descText"

                            Log.d("crawlPlant Name", "${keywordText}")
                            crawlPlantName.value = keywordText.toString()

                            val description = docs.selectFirst("p.desc.__ellipsis")?.text()

                            if (description != null) {
                                Log.d("Plant Description", description)
                                crawlPlantDescription.value = description

                            } else {
                                Log.d("Description", "Description not found")
                            }

                            if (keywordText != null && description != null) isLoading.value = false
                        } else {
                            Log.d("Search Error", "정보를 찾을 수 없습니다.")
                        }
                    }
                }
            } else {
                Log.e("", "식물 정보 불러오기 실패")
            }
        }
    }

    private fun encodeFileToBase64Binary(filePath: String): String {
        val file = File(filePath)
        val fileInputStreamReader = FileInputStream(file)
        val bytes = fileInputStreamReader.readBytes()
        fileInputStreamReader.close()
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
    }

}

