package com.santeut.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.santeut.MainApplication
import com.santeut.data.di.RemoteModule
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: PartyUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _chatrooms = MutableLiveData<List<ChatRoomInfo>>()
    val chatrooms: LiveData<List<ChatRoomInfo>> = _chatrooms

    private val _chatmessages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val chatmessages: LiveData<MutableList<ChatMessage>> = _chatmessages

    fun getChatRoomList() {
        viewModelScope.launch {
            try {
                chatUseCase.getChatRoomList().let {
                    _chatrooms.postValue(chatUseCase.getChatRoomList())
                }
            } catch (e: Exception) {
                _error.value = "Failed to load chat rooms: ${e.message}"
            }
        }
    }

    fun getChatMessageList(partyId: Int) {
        viewModelScope.launch {
            try {
                chatUseCase.getChatMessageList(partyId).let {
                    _chatmessages.postValue(it)
                }
            } catch (e: Exception) {
                _error.value = "Failed to load chat messages(party: ${partyId}): ${e.message}}"
            }
        }
    }

    private fun <E> MutableLiveData<MutableList<E>>.setList(element: E?) {
        val tempList: MutableList<E> = mutableListOf()
        this.value.let {
            if (it != null) {
                tempList.addAll(it)
            }
        }
        if (element != null) {
            tempList.add(element)
        }
        this.postValue(tempList)
    }

    // web socket
    private val okHttpClient = RemoteModule.provideWebSocketClient()
    private var webSocket: WebSocket? = null

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("웹소켓 리스너", "onOpen")
            _isConnected.value = true
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("웹소켓 리스너", "onMessage ${text}")
            var json: JSONObject? = null
            json = JSONObject(text)

            Log.d(
                "SENDER: ",
                "${json.get("userId")}번 유저: ${json.get("userNickname")}, ${json.get("userProfile")}, ${
                    json.get("content")
                }"
            )
            var msg = ChatMessage(
                json.get("createdAt").toString(),
                json.get("userNickname").toString(),
                json.get("userProfile").toString(),
                json.get("content").toString()
            )
            _chatmessages.setList(msg);
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("웹소켓 리스너", "onClosing")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("웹소켓 리스너", "onClosed")
            _isConnected.value = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("웹소켓 리스너", "onFailure")
            Log.d("onFailure", "${webSocket}/ $t/ $response")
        }
    }

    fun connect(partyId: Int) {
        val webSocketUrl =
            "wss://k10e201.p.ssafy.io/api/party/ws/chat/room/${partyId}"

        val request: Request = Request.Builder()
            .header(
                "Authorization",
                "Bearer ${MainApplication.sharedPreferencesUtil.getAccessToken()}"
            )
            .url(webSocketUrl)
            .build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnected by client")
    }

    fun shutdown() {
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: Message) {
        Log.d("sendMessage", "${_isConnected.value}")
        if (_isConnected.value) {
            var gson = Gson()
            var text = gson.toJson(message)
            webSocket?.send(text)
            if (webSocket != null) {
                Log.d("sendMessage", text);
            }
        }
    }
}
