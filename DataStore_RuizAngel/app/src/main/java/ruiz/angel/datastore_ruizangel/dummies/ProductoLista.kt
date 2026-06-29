package ruiz.angel.datastore_ruizangel.dummies

import ruiz.angel.datastore_ruizangel.R
import ruiz.angel.datastore_ruizangel.domain.Producto

val productList = listOf(
    Producto(
        1,
        "Camisa Azul",
        285f,
        R.drawable.camisa_azul,
        "Camisa Oxford Manga Larga para Caballero"
    ),
    Producto(
        2,
        "Pantalón Negro",
        679f,
        R.drawable.pantalon_negro,
        "Pantalón Negro de mezclilla Regular Fit para hombre 36"
    ),
    Producto(
        3,
        "Suéter Rojo",
        649f,
        R.drawable.sueter_rojo,
        "Suéter Rojo con cuello redondo"
    )
)

fun showAllProducts(): List<Producto> {
    return productList
}