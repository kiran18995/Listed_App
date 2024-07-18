package com.kiran.listedapp.ui.links

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listedtask.api.RetrofitInstance
import com.example.listedtask.models.Dashboard
import kotlinx.coroutines.launch

class LinksViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)

    var isLoaded = false
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _dashBoardData: MutableLiveData<Dashboard> = MutableLiveData<Dashboard>()

    val dashboard: LiveData<Dashboard>
        get() = _dashBoardData


    fun getData() {
        viewModelScope.launch {
            if (!isLoaded) {
                _isLoading.value = true
            }
            try {
                _dashBoardData.value = RetrofitInstance.api.getData()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
                isLoaded = true
            }
        }
    }
}