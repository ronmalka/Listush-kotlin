package com.ron.listush.ui.viewmodels

import androidx.lifecycle.*
import com.ron.listush.data.Item
import com.ron.listush.data.ItemDao
import com.ron.listush.utils.DataUtil.ADD_ITEM_RESULT_OK
import com.ron.listush.utils.DataUtil.EDIT_ITEM_RESULT_OK
import com.ron.listush.utils.DataUtil.defaultName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemDao: ItemDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val itemsEventChannel = Channel<ItemsEvent>()
    val itemsEvent = itemsEventChannel.receiveAsFlow()

    val allItems = itemDao.getAllItems()

    val name = state.get<String>("name") ?: defaultName

    fun itemsByCategory(category: String): LiveData<List<Item>> =
        itemDao.getItemsByCategory(category).asLiveData()

    fun onCheckboxClicked(item: Item, checked: Boolean) = viewModelScope.launch {
        itemDao.update(item.copy(completed = checked))
    }

    fun onItemClick(item: Item) = viewModelScope.launch {
        itemsEventChannel.send(ItemsEvent.NavigateToEditItemScreen(item))
    }

    fun onItemSwiped(item: Item) = viewModelScope.launch {
        itemDao.delete(item)
    }

    fun onAddNewItemClick() = viewModelScope.launch {
        itemsEventChannel.send(ItemsEvent.NavigateToAddItemScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_ITEM_RESULT_OK -> showItemConfirmation("Item Added")
            EDIT_ITEM_RESULT_OK -> showItemConfirmation("Item Updated")
        }
    }

    private fun showItemConfirmation(msg: String) = viewModelScope.launch {
        itemsEventChannel.send(ItemsEvent.ShowItemConfirmation(msg))
    }


    sealed class ItemsEvent {
        object NavigateToAddItemScreen : ItemsEvent()
        data class NavigateToEditItemScreen(val item: Item) : ItemsEvent()
        data class ShowItemConfirmation(val msg: String) : ItemsEvent()
    }


}