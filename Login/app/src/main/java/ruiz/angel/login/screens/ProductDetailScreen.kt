package ruiz.angel.login.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import ruiz.angel.login.domain.Producto
import ruiz.angel.login.getProductById

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit = {},
    onAddToCart: (Producto) -> Unit = {}
) {
    val producto = getProductById(productId)

    Column {
        Button(onClick = onBackClick) { Text("Regresar") }

        if (producto == null) {
            Text("Producto no encontrado")
        } else {
            Image(painter = painterResource(producto.imagen), contentDescription = null)
            Text(producto.nombre)
            Text("$${producto.precio}")
            Text(producto.descripcion)
            Button(onClick = { onAddToCart(producto) }) { Text("Agregar al Carrito") }
        }
    }
}