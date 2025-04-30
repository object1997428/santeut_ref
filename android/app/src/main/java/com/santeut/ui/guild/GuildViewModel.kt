package com.santeut.ui.guild

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.data.model.response.GuildApplyResponse
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingResponse
import com.santeut.domain.usecase.GuildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GuildViewModel @Inject constructor(
    private val guildUseCase: GuildUseCase
) : ViewModel() {

    private val _guilds = MutableLiveData<List<GuildResponse>>(emptyList())
    val guilds: LiveData<List<GuildResponse>> = _guilds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _guild = MutableLiveData<GuildResponse>()
    val guild: LiveData<GuildResponse> = _guild

    private val _postList = MutableLiveData<List<GuildPostResponse>>(emptyList())
    val postList: LiveData<List<GuildPostResponse>> = _postList

    private val _post = MutableLiveData<GuildPostDetailResponse>()
    val post: LiveData<GuildPostDetailResponse> = _post

    private val _memberList = MutableLiveData<List<GuildMemberResponse>>(emptyList())
    val memberList: LiveData<List<GuildMemberResponse>> = _memberList

    private val _applyList = MutableLiveData<List<GuildApplyResponse>>(emptyList())
    val applyList: LiveData<List<GuildApplyResponse>> = _applyList

    private val _rankingList = MutableLiveData<List<RankingResponse>>(emptyList())
    val rankingList: LiveData<List<RankingResponse>> = _rankingList

    fun getGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.getGuilds())
            } catch (e: Exception) {
                _error.postValue("동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun searchGuilds(regionName: String, gender: String) {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.searchGuilds(regionName, gender))
            } catch (e: Exception) {
                _error.postValue("검색 동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun searchGuildByName(name: String?) {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.searchGuildByName(name))
            } catch (e: Exception) {
                _error.postValue("동호회 이름으로 검색 실패: ${e.message}")
            }
        }
    }

    fun createGuild(
        guildProfile: MultipartBody.Part?,
        createGuildRequest: CreateGuildRequest
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                guildUseCase.createGuild(guildProfile, createGuildRequest).collect {
                    Log.d("GuildViewModel", "동호회 생성 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 생성 실패: ${e.message}")
            }
        }
    }

    fun updateGuild(
        guildId: Int,
        guildProfile: MultipartBody.Part?,
        updateGuildRequest: CreateGuildRequest
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                guildUseCase.updateGuild(guildId, guildProfile, updateGuildRequest).collect {
                    Log.d("GuildViewModel", "동호회 정보 수정 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 정보 수정 실패: ${e.message}")
            }
        }
    }


    fun myGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.myGuilds())
            } catch (e: Exception) {
                _error.postValue("내 동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                _guild.value = guildUseCase.getGuild(guildId)
            } catch (e: Exception) {
                _error.postValue("동호회 정보 조회 실패: ${e.message}")
            }
        }
    }

    fun applyGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.applyGuild(guildId).collect {
                    Log.d("GuildViewModel", "가입 요청 전송")
                }
            } catch (e: Exception) {
                _error.postValue("가입 요청 전송 실패: ${e.message}")
            }
        }
    }

    fun getGuildPostList(guildId: Int, categoryId: Int) {
        viewModelScope.launch {
            try {
                _postList.postValue(guildUseCase.getGuildPostList(guildId, categoryId))
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun createGuildPost(
        images: List<MultipartBody.Part>?,
        createGuildPostRequest: CreateGuildPostRequest
    ) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "접근 성공")
                guildUseCase.createGuildPost(images, createGuildPostRequest).collect {
                    Log.d("GuildViewModel", "동호회 게시글 작성 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 작성 실패: ${e.message}")
            }
        }
    }

    fun getGuildPost(guildPostId: Int) {
        viewModelScope.launch {
            try {
                _post.postValue(guildUseCase.getGuildPost(guildPostId))
            } catch (e: Exception) {
                _error.postValue("게시글 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuildMemberList(guildId: Int) {
        viewModelScope.launch {
            try {
                _memberList.postValue(guildUseCase.getGuildMemberList(guildId))
            } catch (e: Exception) {
                _error.postValue("동호회 회원 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuildApplyList(guildId: Int) {
        viewModelScope.launch {
            try {
                _applyList.postValue(guildUseCase.getGuildApplyList(guildId))
            } catch (e: Exception) {
                _error.postValue("동호회 가입 신청 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun approveMember(guildId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.approveMember(guildId, userId).collect {
                    Log.d("GuildViewModel", "가입 승인 성공")
                }
            } catch (e: Exception) {
                _error.postValue("가입 승인 실패: ${e.message}")
            }
        }
    }

    fun denyMember(guildId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.denyMember(guildId, userId).collect {
                    Log.d("GuildViewModel", "가입 거절 성공")
                }
            } catch (e: Exception) {
                _error.postValue("가입 거절 실패: ${e.message}")
            }
        }
    }

    fun exileMember(guildId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.exileMember(guildId, userId).collect {
                    Log.d("GuildViewModel", "회원 추방 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("회원 추방 요청 실패: ${e.message}")
            }
        }
    }

    fun changeLeader(guildId: Int, newLeaderId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.changeLeader(guildId, newLeaderId).collect {
                    Log.d("GuildViewModel", "회장 위임 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("회장 위임 요청 실패: ${e.message}")
            }
        }
    }

    fun quitGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.quitGuild(guildId).collect {
                    Log.d("GuildViewModel", "동호회 탈퇴 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 탈퇴 요청 실패: ${e.message}")
            }
        }
    }

    fun getRanking(guildId: Int, type: Char) {
        viewModelScope.launch {
            try {
                _rankingList.postValue(guildUseCase.getRanking(guildId, type))
            } catch (e: Exception) {
                _error.postValue("랭킹 조회 실패: ${e.message}")
            }
        }
    }
}