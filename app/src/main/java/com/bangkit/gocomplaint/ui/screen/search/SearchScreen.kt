package com.bangkit.gocomplaint.ui.screen.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.SearchResultItem
import com.bangkit.gocomplaint.ui.screen.Error
import com.bangkit.gocomplaint.ui.screen.ErrorScreen

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToDetail: (Int) -> Unit
) {
    viewModel.uiSearchState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.emptySearchResponse()
            }
            is UiState.Success -> {
                SearchContent(
                    searchResult = uiState.data,
                    navigateToDetail = navigateToDetail
                )
                Log.d("lala", "lala")
            }
            is UiState.Error -> {
                ErrorScreen(
                    retryAction = { }
                )
            }
        }
    }
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    searchResult: ComplaintResponse,
    navigateToDetail: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(84.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { viewModel.getSearchComplaint(query) },
            )
        }
        if (searchResult.complaints.isNotEmpty()) {
            SearchResultItem(listComplaints = searchResult, navigateToDetail = navigateToDetail)
        } else {
            HomeEmpty()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = false,
        onActiveChange = {},
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Black
            )
        },
        placeholder = {
            Text(stringResource(R.string.plchldr_search))
        },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            inputFieldColors = inputFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    ) {
    }
}

@Composable
fun HomeEmpty(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(stringResource(R.string.search_not_found), color = MaterialTheme.colorScheme.onPrimary)
    }
}