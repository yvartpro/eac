package bi.vovota.eac.ui.nav

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bi.vovota.eac.data.model.Product
import bi.vovota.eac.ui.screen.AuthScreen
import bi.vovota.eac.ui.screen.CartScreen
import bi.vovota.eac.ui.screen.CompanyDetailsScreen
import bi.vovota.eac.ui.screen.CompanyScreen
import bi.vovota.eac.ui.screen.HomeScreen
import bi.vovota.eac.ui.screen.MerchantDashboardBody
import bi.vovota.eac.ui.screen.ProductDetailBody
import bi.vovota.eac.ui.screen.ProductScreen
import bi.vovota.eac.ui.screen.ProfileScreen
import bi.vovota.eac.ui.screen.SearchScreen
import bi.vovota.eac.viewmodel.CartViewModel
import bi.vovota.eac.viewmodel.CategoryViewModel
import bi.vovota.eac.viewmodel.CompanyViewModel
import bi.vovota.eac.viewmodel.OrderViewModel
import bi.vovota.eac.viewmodel.ProductViewModel
import bi.vovota.eac.viewmodel.UserViewModel
import kotlin.collections.find

@Composable
fun AppNavGraph(
  modifier: Modifier,
  navController: NavHostController,
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  userViewModel: UserViewModel,
  categoryViewModel: CategoryViewModel,
  companyViewModel: CompanyViewModel,
  orderViewModel: OrderViewModel
) {
  LaunchedEffect(Unit) {
    productViewModel.loadProducts()
    cartViewModel.companies
    companyViewModel.loadCompanies()
  }

  NavHost(navController, startDestination = NavDestinations.HOME, modifier = modifier) {
    composable(NavDestinations.HOME) {
      HomeScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        //onNavigateToCart = { navController.navigate(NavDestinations.CART) },
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel,
        navController = navController
      )
    }
    composable(NavDestinations.CART) {
      CartScreen(
        cartViewModel = cartViewModel,
        userViewModel = userViewModel,
        navController = navController,
        productViewModel = productViewModel
      )
    }
    composable(NavDestinations.COMPANIES)   {
      CompanyScreen(
        navController = navController,
        companies = companyViewModel.companies,
        companyViewModel = companyViewModel
      )
    }
    composable(NavDestinations.PRODUCT) { backStackEntry->
      val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
      val product = productViewModel.products.find { it.id == productId }
      product?.let {
        ProductScreen(
          product = it,
          cartViewModel = cartViewModel,
          productViewModel = productViewModel,
          navController = navController, userViewModel = userViewModel
        )
      }
    }
    composable(NavDestinations.DETAILS) { backStackEntry->
      val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
      val product = productViewModel.products.find { it.id == productId }
      product?.let {
        ProductDetailBody(
          product,
          cartViewModel,
          productViewModel,
          navController,
          userViewModel,
        )
      }
    }
    composable(NavDestinations.DASHBOARD) { backStackEntry->
        MerchantDashboardBody(
          products = productViewModel.products,
          onEditProduct = { },
          onOrderClick = { }
        )
    }
    composable(NavDestinations.PROFILE) {
      ProfileScreen(
        navController = navController,
        userViewModel = userViewModel,
        orderViewModel = orderViewModel
      )
    }
    composable(NavDestinations.AUTH) {
      AuthScreen(
        modifier = Modifier,
        onBackClick = { navController.navigate(NavDestinations.HOME)},
        navController = navController,
        userViewModel = userViewModel
      )
    }
    composable(NavDestinations.SEARCH) {
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        navController = navController,
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel
      )
    }
    composable(NavDestinations.CATEGORY) { backStackEntry->
      val categoryName = backStackEntry.arguments?.getString("category")
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        categoryName = categoryName.takeIf { it != null },
        navController = navController,
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel
      )
    }
    composable(NavDestinations.COMPANY) { backStackEntry ->
      val companyId = backStackEntry.arguments?.getString("companyId")?.toIntOrNull()
      if (companyId != null) {
        CompanyDetailsScreen(
          companyId = companyId,
          cartViewModel = cartViewModel,
          navController = navController,
          productViewModel = productViewModel,
          companies = companyViewModel.companies,
          user = userViewModel.user
        )
      }
    }
  }
}

