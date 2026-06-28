package ruiz.angel.login

import ruiz.angel.login.domain.Producto
import ruiz.angel.login.dummies.showAllProducts

fun getProductsByName(nombre: String): List<Producto> {
    if (nombre.isBlank()) {
        return showAllProducts()
    }

    return showAllProducts().filter {
        producto -> producto.nombre.contains(nombre, ignoreCase = true)
    }
}

fun getProductById(id: Int): Producto? {
    return showAllProducts().find { it.id == id }
}