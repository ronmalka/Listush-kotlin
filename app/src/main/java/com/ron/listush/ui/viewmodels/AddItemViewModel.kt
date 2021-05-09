package com.ron.listush.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ron.listush.data.Item
import com.ron.listush.data.ItemDao
import com.ron.listush.utils.DataUtil.ADD_ITEM_RESULT_OK
import com.ron.listush.utils.DataUtil.EDIT_ITEM_RESULT_OK
import com.ron.listush.utils.DataUtil.ITEM_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemDao: ItemDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val item = state.get<Item>("item")

    var category = state.get<String>("category") ?: item?.category ?: ""
        set(value) {
            field = value
            state.set("category", value)
        }

    var itemName = state.get<String>("itemName") ?: item?.name ?: ""
        set(value) {
            field = value
            state.set("itemName", value)
        }

    var itemQuantity = state.get<String>("itemQuantity ") ?: item?.quantity ?: ""
        set(value) {
            field = value
            state.set("itemQuantity", value)
        }

    private val addItemEventChannel = Channel<AddItemEvent>()
    val addItemEvent = addItemEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (itemName.isBlank() || itemQuantity.isBlank()) {
            ShowInvalidInput("Name or Quantity cannot be empty")
            return
        }

        if (item != null) {
            val updateItem = item.copy(name = itemName, quantity = itemQuantity)
            updateItem(updateItem)
        } else {
            val newItem = Item(name = itemName, quantity = itemQuantity, category, ITEM_TYPE)
            createItem(newItem)
        }
    }

    private fun ShowInvalidInput(s: String) = viewModelScope.launch {
        addItemEventChannel.send(AddItemEvent.ShowInvalidInput(s))
    }

    private fun createItem(newItem: Item) = viewModelScope.launch {
        itemDao.insert(newItem)
        addItemEventChannel.send(AddItemEvent.NavigateBackWithResult(ADD_ITEM_RESULT_OK))
    }

    private fun updateItem(updateItem: Item) = viewModelScope.launch {
        itemDao.update(updateItem)
        addItemEventChannel.send(AddItemEvent.NavigateBackWithResult(EDIT_ITEM_RESULT_OK))
    }

    sealed class AddItemEvent {
        data class ShowInvalidInput(val msg: String) : AddItemEvent()
        data class NavigateBackWithResult(val result: Int) : AddItemEvent()
    }
    
}