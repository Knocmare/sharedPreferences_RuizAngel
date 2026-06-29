package ruiz.angel.datastore_ruizangel

import ruiz.angel.datastore_ruizangel.domain.Producto
import ruiz.angel.datastore_ruizangel.dummies.showAllProducts

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