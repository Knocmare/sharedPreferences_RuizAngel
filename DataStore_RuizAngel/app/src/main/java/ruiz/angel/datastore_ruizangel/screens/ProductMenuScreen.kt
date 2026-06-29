package ruiz.angel.datastore_ruizangel.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ruiz.angel.datastore_ruizangel.components.ProductGridItem
import ruiz.angel.datastore_ruizangel.domain.Producto
import ruiz.angel.datastore_ruizangel.getProductsByName

@Composable
fun ProductMenuScreen(
    cartList: List<Producto>,
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    onAddToCart: (Producto) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = getProductsByName(searchQuery)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = onCartClick) { Text("Carrito (${cartList.size})") }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar en la tienda") }
            )
            Button(onClick = {}) { Text("Buscar") }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredProducts) { itemProducto ->
                ProductGridItem(
                    producto = itemProducto,
                    onItemClick = { onProductClick(itemProducto.id) },
                    onAddClick = { onAddToCart(itemProducto) }
                )
            }
        }
    }
}
