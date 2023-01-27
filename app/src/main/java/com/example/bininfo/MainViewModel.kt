package com.example.bininfo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bininfo.network.BINInfo
import com.example.bininfo.network.BINRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel : ViewModel() {
    private val binRepository = BINRepository()

    private val _binInfo = mutableStateOf(BINInfo())
    var binInfo: State<BINInfo> = _binInfo

    private val _binNumber = mutableStateOf("")
    var binNumber: State<String> = _binNumber

    fun onBinNumberChange(number: String) {
        _binNumber.value = number

        if (number.isEmpty()) return
        viewModelScope.launch {
            try {
                binRepository.getInfo(number).collect {
                    _binInfo.value = it
                }
            }
            catch (e: HttpException) {
                _binInfo.value = BINInfo()
            }
        }
    }
}