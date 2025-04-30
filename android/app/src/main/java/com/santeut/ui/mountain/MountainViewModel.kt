package com.santeut.ui.mountain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.domain.usecase.MountainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainUseCase: MountainUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _mountains = MutableLiveData<List<MountainResponse>>()
    val mountains: LiveData<List<MountainResponse>> = _mountains

    private val _mountain = MutableLiveData<MountainDetailResponse>()
    val mountain: LiveData<MountainDetailResponse> = _mountain

    private val _courseList = MutableLiveData<List<HikingCourseResponse>>()
    val courseList: LiveData<List<HikingCourseResponse>> = _courseList

    private val _pathList = MutableLiveData<List<CourseDetailResponse>>()
    val pathList: LiveData<List<CourseDetailResponse>> = _pathList

    fun popularMountain() {
        viewModelScope.launch {
            try {
                mountainUseCase.popularMountain().let {
                    _mountains.postValue(mountainUseCase.popularMountain())
                }
            } catch (e: Exception) {
                _error.postValue("인기 있는 산 조회 실패: ${e.message}")
            }
        }
    }

    fun searchMountain(name: String, region: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mountainUseCase.searchMountain(name, region).let {
                    _mountains.postValue(mountainUseCase.searchMountain(name, region))
                }
            } catch (e: Exception) {
                _error.postValue("산 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun mountainDetail(mountainId: Int) {
        viewModelScope.launch {
            try {
                mountainUseCase.mountainDetail(mountainId).let {
                    _mountain.postValue(mountainUseCase.mountainDetail(mountainId))
                }
            } catch (e: Exception) {
                _error.postValue("산 상세 정보 조회 실패: ${e.message}")
            }
        }
    }

    fun getHikingCourseList(mountainId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mountainUseCase.getHikingCourseList(mountainId).let {
                    _courseList.postValue(mountainUseCase.getHikingCourseList(mountainId))
                }
            } catch (e: Exception) {
                _error.postValue("코스 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun setPathList(mountainId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mountainUseCase.getAllCourses(mountainId).let {
                    _pathList.postValue(mountainUseCase.getAllCourses(mountainId))
                }
            } catch (e: Exception) {
                _error.postValue("코스 목록 조회 실패: ${e.message}")
            }
        }
    }
}
