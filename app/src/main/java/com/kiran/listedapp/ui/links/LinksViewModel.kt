package com.kiran.listedapp.ui.links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiran.listedapp.data.repository.DashboardRepository
import com.kiran.listedapp.models.Dashboard
import com.kiran.listedapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class LinksViewModel @Inject constructor(private val repository: DashboardRepository) :
    ViewModel() {

    private val _dashboardData = MutableStateFlow<Resource<Dashboard>>(Resource.Loading())
    val dashboardData: StateFlow<Resource<Dashboard>> = _dashboardData

    private val _greetings = MutableStateFlow("")
    val greetings: StateFlow<String> = _greetings

    init {
        initGreetings()
        fetchDashboardData()
    }

    private fun initGreetings() {
        _greetings.value = when (LocalTime.now().hour) {
            in 6..11 -> GOOD_MORNING
            in 12..16 -> GOOD_AFTERNOON
            in 17..20 -> GOOD_EVENING
            else -> GOOD_NIGHT
        }
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            repository.getDashboardData().collect { resource ->
                _dashboardData.emit(resource)
            }
        }
    }

    companion object {
        private const val GOOD_AFTERNOON = "Good afternoon"
        private const val GOOD_MORNING = "Good morning"
        private const val GOOD_EVENING = "Good evening"
        private const val GOOD_NIGHT = "Good night"
    }
}