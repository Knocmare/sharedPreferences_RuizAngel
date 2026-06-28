package ruiz.angel.login.domain

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Float,
    val imagen: Int,
    val descripcion: String
)