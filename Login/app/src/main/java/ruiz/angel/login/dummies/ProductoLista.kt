package ruiz.angel.login.dummies

import ruiz.angel.login.R
import ruiz.angel.login.domain.Producto

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