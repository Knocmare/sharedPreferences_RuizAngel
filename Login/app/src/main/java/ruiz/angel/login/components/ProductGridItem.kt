package ruiz.angel.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ruiz.angel.login.domain.Producto

@Composable
fun ProductGridItem(
    producto: Producto,
    onItemClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    Column(modifier = Modifier.clickable { onItemClick() }) {
        Image(painter = painterResource(producto.imagen), contentDescription = null)
        Text(producto.nombre)
        Text("$${producto.precio}")
        Button(onClick = onAddClick) { Text("Agregar") }
    }
}
