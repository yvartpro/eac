package bi.vovota.eac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import bi.vovota.eac.nav.AppNavHost
import bi.vovota.eac.ui.screens.auth.AuthViewModel
import bi.vovota.eac.ui.screens.home.ProductViewModel
import bi.vovota.eac.ui.screens.home.ProfileViewModel
import bi.vovota.eac.ui.theme.MvvmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = hiltViewModel()
            val profileViewModel:ProfileViewModel = hiltViewModel()
            val productViewModel: ProductViewModel = hiltViewModel()

            MvvmTheme {
                AppNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    productViewModel = productViewModel
                )
            }
        }
    }
}

