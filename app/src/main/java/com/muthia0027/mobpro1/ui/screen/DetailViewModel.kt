
package com.muthia0027.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthia0027.mobpro1.database.FinancialDao
import com.muthia0027.mobpro1.model.Financial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: FinancialDao) : ViewModel() {

    fun insert(title: String, amount: Int, category: String, date: String) {
        val financial = Financial(
            title = title,
            amount = amount,
            category = category,
            date = date
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(financial)
        }
    }

    suspend fun getFinancialById(id: Long): Financial? {
        return dao.getFinancialById(id)
    }

    fun update(id: Long, title: String, amount: Int, category: String, date: String) {
        val financial = Financial(
            id = id,
            title = title,
            amount = amount,
            category = category,
            date = date
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(financial)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}

