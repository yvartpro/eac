package bi.vovota.eac.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bi.vovota.eac.ui.UiState

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateLogin: () -> Unit
) {
    val registerUiState by authViewModel.registerUiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { authViewModel.register(name, password) }) {
            Text("Register")
        }
        TextButton(onClick = onNavigateLogin) {
            Text("Already have an account? Login")
        }

        when (registerUiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                Text("Registration Successful", color = Color.Green)
            }
            is UiState.Error -> {
                SelectionContainer { Text((registerUiState as UiState.Error).sapor, color = Color.Red) }

            }
            else -> {}
        }
    }
}
