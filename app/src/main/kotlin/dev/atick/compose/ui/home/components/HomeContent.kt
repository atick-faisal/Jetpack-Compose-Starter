package dev.atick.compose.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.atick.compose.R
import dev.atick.compose.data.home.Item
import dev.atick.core.ui.components.LoadingButton

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    item: Item? = null,
    loading: Boolean = false,
    onButtonCLick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item?.let {
            Text(text = it.title)
            Spacer(modifier = Modifier.height(16.dp))
        }
        LoadingButton(onClick = onButtonCLick, loading = loading) {
            Text(text = stringResource(R.string.btn_click_text))
        }
    }
}