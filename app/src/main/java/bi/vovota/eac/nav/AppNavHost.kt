package bi.vovota.eac.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bi.vovota.eac.ui.screens.auth.AuthViewModel
import bi.vovota.eac.ui.screens.auth.LoginScreen
import bi.vovota.eac.ui.screens.auth.RegisterScreen
import bi.vovota.eac.ui.screens.home.HomeScreen
import bi.vovota.eac.ui.screens.home.ProductViewModel
import bi.vovota.eac.ui.screens.home.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel
) {
    Scaffold(
        topBar = {
            TopBar(navController = navController)
        },
        bottomBar = {
            BottomBar(navController = navController, cartItemCount = 12)
        }
    ) {  innerP->
        NavHost(navController, startDestination = Routes.LOGIN, modifier = Modifier.padding(innerP)) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateHome = { navController.navigate(Routes.HOME) },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onNavigateLogin = { navController.popBackStack() }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    authViewModel = authViewModel,
                    productViewModel = productViewModel,
                    navController = navController
                )
            }
        }
    }

}

