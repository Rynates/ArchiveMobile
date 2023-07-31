package com.example.helper.ui.archiveFeature

import android.util.Log
import androidx.lifecycle.*
import com.example.helper.data.locale.FundDao
import com.example.helper.data.locale.InventoryDao
import com.example.helper.domen.models.*
import com.example.helper.domen.repository.ArchivesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchivesViewModel @Inject constructor(
    private val archivesRepository: ArchivesRepository,
    private val fundsDao: FundDao,
    private val inventoryDao: InventoryDao
) : ViewModel() {

    val readFunds = fundsDao.readFunds()

    private val _archives: MutableLiveData<List<Archive>?> = MutableLiveData()
    val archivesList get() = _archives

    private val _archive: MutableLiveData<Archive?> = MutableLiveData()
    val archiveByDoc get() = _archive

    private val _inventories: MutableSharedFlow<List<Document>> = MutableSharedFlow()
    val inventoryList get() = _inventories.asLiveData()

    private val _fund: MutableSharedFlow<Fund> = MutableSharedFlow<Fund>()
    val funds get() = _fund.asLiveData()

    private val _doc: MutableSharedFlow<List<Document>> = MutableSharedFlow<List<Document>>()
    val  docs get() = _doc.asLiveData()

    fun addFund(fund: Fund) =
        viewModelScope.launch(Dispatchers.IO) {
            fundsDao.addFund(fund)
        }

    suspend fun removeFund(fund: Fund) {
        fundsDao.deleteFund(fund)
    }

    val archives = archivesRepository.getArchives().asLiveData()

    fun archiveUpdate() {
        viewModelScope.launch {
            val archives = archivesRepository.getArchiveUpdate()
            if (archives.info != null) {
                _archives.value = archives.info
            }
        }
    }


    fun getInventories(fundId: String) {
        viewModelScope.launch {
            val inventories = archivesRepository.getInventoryByFundId(fundId)
            inventories.info?.let {
                _inventories.emit(inventories.info)
            }
        }
    }

    fun getFundById(fundId: String): Flow<ApiResponse<Fund>> {
        val fund = archivesRepository.getFundById(fundId)
        return fund
    }


    fun getArchiveById(archiveId:String):Flow<ApiResponse<Archive>>{
        val archive = archivesRepository.getArchiveById(archiveId)
        return archive
    }

    val readInventories = inventoryDao.readInventories()
    val readDoc = inventoryDao.readInventories().asLiveData()

    fun filterInventory(userId:String){
        viewModelScope.launch {
            readInventories.collect { doc ->
                _doc.emit(doc.filter{ it.userId.equals(userId) })
            }
        }
    }
    fun addInventory(inventory: Document) =
        viewModelScope.launch(Dispatchers.IO) {
            inventoryDao.addInventories(inventory)
        }

    suspend fun removeInventory(inventory: Document) {
        inventoryDao.deleteInventories(inventory)
    }
}
