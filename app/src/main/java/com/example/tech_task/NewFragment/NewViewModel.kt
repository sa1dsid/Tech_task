package com.example.tech_task.NewFragment

import NewPagingSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tech_task.api.RetrofitClient
import com.example.tech_task.data.CharacterData
import kotlinx.coroutines.flow.Flow

class NewViewModel : ViewModel() {
    val characters: Flow<PagingData<CharacterData>> = Pager(
        config = PagingConfig(
            pageSize = 6,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewPagingSource(RetrofitClient.api) }
    ).flow
        .cachedIn(viewModelScope)
}
