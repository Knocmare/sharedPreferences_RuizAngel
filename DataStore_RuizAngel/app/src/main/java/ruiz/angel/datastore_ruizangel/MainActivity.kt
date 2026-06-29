package ruiz.angel.datastore_ruizangel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import ruiz.angel.datastore_ruizangel.domain.Producto
import ruiz.angel.datastore_ruizangel.screens.CartScreen
import ruiz.angel.datastore_ruizangel.screens.ProductDetailScreen
import ruiz.angel.datastore_ruizangel.screens.ProductMenuScreen
import ruiz.angel.datastore_ruizangel.ui.theme.DataStore_RuizAngelTheme

class MainActivity : ComponentActivity() {

    private lateinit var dsManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        dsManager = DataStoreManager(this)

        setContent {
            DataStore_RuizAngelTheme {
                val isLoggedIn by dsManager.isLoggedIn.collectAsStateWithLifecycle(initialValue = false)

                if(isLoggedIn) {
                    HomeScreen(
                        dsManager = dsManager,
                        onLogout = {
                            lifecycleScope.launch {
                                dsManager.logout()
                            }
                        }
                    )
                } else {
                    LoginScreen(
                        onLogin = {
                            lifecycleScope.launch {
                                dsManager.setLoggedIn(true)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
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
                    onLogin()
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
    dsManager: DataStoreManager,
    onLogout: () -> Unit
) {
    val cartList = remember { mutableStateListOf<Producto>() }
    var tiendaScreenState by remember { mutableStateOf("CATALOGO") }
    var selectedProductId by remember { mutableStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val jsonSaved = dsManager.cartFlow.first()
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
        coroutineScope.launch {
            dsManager.saveCartJson(jsonString)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onLogout) {
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