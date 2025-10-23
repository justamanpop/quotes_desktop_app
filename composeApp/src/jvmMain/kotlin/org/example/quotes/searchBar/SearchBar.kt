import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchText = remember { mutableStateOf("") }
    TextField(
        value = searchText.value,
        onValueChange = { v ->
            searchText.value = v
        },
        placeholder = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, "search") },
        modifier = modifier
    )

}
