package bi.vovota.eac.ui.nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import bi.vovota.eac.data.model.TokenManager
import bi.vovota.eac.ui.theme.FontSizes
import bi.vovota.eac.viewmodel.CategoryViewModel
import bi.vovota.eac.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import bi.vovota.eac.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
  navController: NavController,
  userViewModel: UserViewModel,
  categoryViewModel: CategoryViewModel
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val scope = rememberCoroutineScope()
  val showTopBar = currentRoute != NavDestinations.AUTH


  val showBackArrow = currentRoute == NavDestinations.SEARCH ||
      currentRoute == NavDestinations.CART ||
      currentRoute == NavDestinations.CATEGORY ||
      currentRoute == NavDestinations.PRODUCT ||
      currentRoute == NavDestinations.COMPANY ||
      currentRoute == NavDestinations.DASHBOARD ||
      currentRoute == NavDestinations.PROFILE ||
      currentRoute == NavDestinations.EDITPROD ||
      currentRoute == NavDestinations.DETAILS

  if(showTopBar) {
    TopAppBar(
      title = {
        when (currentRoute) {
          NavDestinations.CART -> Text(stringResource(R.string.t_your_cart))
          NavDestinations.SEARCH -> Text(stringResource(R.string.t_search))
          NavDestinations.CATEGORY -> Text(stringResource(R.string.t_cat))
          NavDestinations.PRODUCT -> Text(stringResource(R.string.t_product))
          NavDestinations.PROFILE -> Text("Profile")
          NavDestinations.EDITPROD -> Text("Edit product")
          NavDestinations.DETAILS -> Text("Product details")
          NavDestinations.DASHBOARD -> Text("Dashboard")
          else -> {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
          }
        }
      },
      navigationIcon = {
        if (true) {
          IconButton(onClick = {
//          navController.popBackStack()
            categoryViewModel.setNotActive()
            navController.navigate(NavDestinations.HOME) {
              popUpTo(NavDestinations.AUTH) { inclusive = true }
              launchSingleTop = true
            }
          }) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      },
      actions = {
        if (currentRoute == NavDestinations.HOME) {
          IconButton(onClick = { navController.navigate(NavDestinations.SEARCH) }) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
          }
        } else if (currentRoute == NavDestinations.PROFILE) {
          Row {
            TextButton(onClick = { userViewModel.toggleEdit() }) {
              Text(
                text = stringResource(R.string.t_edit),
                fontWeight = FontWeight.SemiBold,
                fontSize = FontSizes.body(),
                color = MaterialTheme.colorScheme.onBackground
              )
            }
            IconButton(onClick = {
              scope.launch {
                TokenManager.clearTokens()
                userViewModel.clearUser()
                navController.navigate(NavDestinations.HOME)
              }
            }) {
              Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.onBackground
              )
            }
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
  }
}