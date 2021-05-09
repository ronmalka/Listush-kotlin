package com.ron.listush.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ron.listush.utils.DataUtil.defaultName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class CategoryViewModel : ViewModel() {

    private val categoryEventChannel = Channel<CategoryEvent>()
    val categoryEvent = categoryEventChannel.receiveAsFlow()

    fun NavigateToCategoryScreen(name: String) = viewModelScope.launch {
        categoryEventChannel.send(CategoryEvent.NavigateToCategoryScreen(name))
    }

    fun createNewList() = viewModelScope.launch{
        categoryEventChannel.send(CategoryEvent.NavigateToCategoryScreen(defaultName))
    }

    sealed class CategoryEvent {
        data class NavigateToCategoryScreen(val name: String) : CategoryEvent()
    }

}
