package ruiz.angel.datastore_ruizangel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import ruiz.angel.datastore_ruizangel.domain.Producto

@Composable
fun CartScreen(
    cartList: List<Producto>,
    onBackClick: () -> Unit = {}
) {
    val total = cartList.sumOf { it.precio.toDouble() }.toFloat()

    Column {
        Button(onClick = onBackClick) { Text("Volver a la Tienda") }
        Text("Mi Carrito de Compras")

        LazyColumn {
            items(cartList) { item ->
                Row {
                    Image(painter = painterResource(item.imagen), contentDescription = null)
                    Column {
                        Text(item.nombre)
                        Text("$${item.precio}")
                    }
                }
            }
        }

        Text("Cantidad de productos: ${cartList.size}")
        Text("Total a pagar: $$total")
    }
}