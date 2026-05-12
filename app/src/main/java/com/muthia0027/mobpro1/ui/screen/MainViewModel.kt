package com.muthia0027.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthia0027.mobpro1.database.FinancialDao
import com.muthia0027.mobpro1.model.Financial
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.emptyList


class MainViewModel(dao: FinancialDao) : ViewModel() {

    val data: StateFlow<List<Financial>> = dao.getFinancial().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


}