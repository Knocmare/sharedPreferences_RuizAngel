package ruiz.angel.login

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ruiz.angel.login.domain.Producto
import ruiz.angel.login.screens.CartScreen
import ruiz.angel.login.screens.ProductDetailScreen
import ruiz.angel.login.screens.ProductMenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager(this)
        val sharedPrefs = getSharedPreferences("tienda_prefs", Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContent {
            var screenState by remember { mutableStateOf(if (prefs.isLoggedIn()) "HOME" else "LOGIN") }

            if (screenState == "LOGIN") {
                LoginScreen(onLoginClick = {
                    prefs.saveLoginStatus(true)
                    screenState = "HOME"
                })
            } else {
                HomeScreen(
                    sharedPrefs = sharedPrefs,
                    onLogoutClick = {
                        prefs.logout()
                        screenState = "LOGIN"
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = "" },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = "" },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Oculta los caracteres
            singleLine = true
        )

        // Mostrar error si los datos son incorrectos
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // VALIDACIÓN: Aqui decides qué datos son correctos
                if (email == "admin@mail.com" && password == "1234") {
                    onLoginClick()
                } else {
                    errorMessage = "Credenciales incorrectas. Intenta de nuevo."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}

@Composable
fun HomeScreen(
    sharedPrefs: android.content.SharedPreferences,
    onLogoutClick: () -> Unit
) {
    val cartList = remember { mutableStateListOf<Producto>() }
    var tiendaScreenState by remember { mutableStateOf("CATALOGO") }
    var selectedProductId by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        val jsonSaved = sharedPrefs.getString("items_carrito", null)
        if (!jsonSaved.isNullOrEmpty()) {
            try {
                val savedProducts: List<Producto> = Json.decodeFromString(jsonSaved)
                cartList.clear()
                cartList.addAll(savedProducts)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val saveCart = {
        val jsonString = Json.encodeToString(cartList.toList())
        sharedPrefs.edit().putString("items_carrito", jsonString).apply()
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLogoutClick) {
                Text("Cerrar Sesión")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (tiendaScreenState) {
                "CATALOGO" -> {
                    ProductMenuScreen(
                        cartList = cartList,
                        onCartClick = { tiendaScreenState = "CARRITO" },
                        onProductClick = { id ->
                            selectedProductId = id
                            tiendaScreenState = "DETALLE"
                        },
                        onAddToCart = { producto ->
                            cartList.add(producto)
                            saveCart()
                        }
                    )
                }
                "DETALLE" -> {
                    ProductDetailScreen(
                        productId = selectedProductId,
                        onBackClick = { tiendaScreenState = "CATALOGO" },
                        onAddToCart = { producto ->
                            cartList.add(producto)
                            saveCart()
                        }
                    )
                }
                "CARRITO" -> {
                    CartScreen(
                        cartList = cartList,
                        onBackClick = { tiendaScreenState = "CATALOGO" }
                    )
                }
            }
        }
    }
}