package com.example.piggyfarmyutil

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThongTinViewModel : ViewModel() {
    val listThongTin: MutableLiveData<List<Pair<String, Int>>> = MutableLiveData()
    private val uniqueItemsMap: MutableMap<String, Int> = mutableMapOf()
    fun addItemToThongTin(item: Pair<String, Int>) {
        if (uniqueItemsMap.containsKey(item.first)) {
            // If the item already exists, update its value in the map
            uniqueItemsMap[item.first] = item.second
        } else {
            // If not a duplicate, add the item to the list and update the map
            val currentList = listThongTin.value ?: emptyList()
            val updatedList = currentList.toMutableList().apply {
                add(item)
            }
            listThongTin.value = updatedList

            // Add the item to the map with its associated value (Int)
            uniqueItemsMap[item.first] = item.second
        }
    }
}

