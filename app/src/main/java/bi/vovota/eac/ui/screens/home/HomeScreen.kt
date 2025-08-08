package bi.vovota.eac.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bi.vovota.eac.model.Profile
import bi.vovota.eac.ui.screens.auth.AuthViewModel
import bi.vovota.eac.R
import bi.vovota.eac.model.Product
import bi.vovota.eac.ui.UiState

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    navController: NavController,
) {
    val token by authViewModel.accessToken.collectAsState()
    val productUiState by productViewModel.productUiState.collectAsState()
    val products by productViewModel.products.collectAsState()

    when (productUiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Error -> Text(
            (productUiState as UiState.Error).sapor, // erreur ici aussi
            color = Color.Red
        )
        is UiState.Success -> {
            val productList = (productUiState as UiState.Success<List<Product>>).data
//            ProductList(
//                cartViewModel = cartViewModel,
//                productViewModel = productViewModel,
//                products = productList,
//                user = user,
//                navController = navController
//            )
        }
        else -> {}
    }

    LaunchedEffect(token) {
        token?.let {
            //profileViewModel.loadProfile(it)
            productViewModel.loadProducts(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
//        HomeSection(title = stringResource(R.string.category)) {
//            CategoryRow(
//                navController = navController,
//                viewModel = categoryViewModel
//            )
//        }
        HomeSection(title = stringResource(R.string.latest)) {
            ProductImageRow(
                productViewModel = productViewModel,
                //userViewModel = userViewModel,
                navController = navController,
                products = products
            )
        }
//    HomeSection(title = stringResource(R.string.recommended)) {
//      Recommended(
//        productViewModel = productViewModel,
//        navController = navController,
//        userViewModel = userViewModel
//      )
//    }
//        HomeSection(title = stringResource(R.string.products)) {
//            ProductList(
//                cartViewModel = cartViewModel,
//                productViewModel = productViewModel,
//                products =  productViewModel.products,
//                user = user,
//                navController = navController
//            )
//        }
    }
}


@Composable
fun HomeSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(2.dp)
        )
        content()
        Spacer(Modifier.height(16.dp))
    }
}