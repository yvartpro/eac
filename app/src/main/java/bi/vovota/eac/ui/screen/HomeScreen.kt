package bi.vovota.eac.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bi.vovota.eac.R
import bi.vovota.eac.ui.section.CategoryRow
import bi.vovota.eac.ui.section.ProductImageRow
import bi.vovota.eac.ui.section.ProductList
import bi.vovota.eac.ui.section.Recommended
import bi.vovota.eac.viewmodel.CartViewModel
import bi.vovota.eac.viewmodel.CategoryViewModel
import bi.vovota.eac.viewmodel.ProductViewModel
import bi.vovota.eac.viewmodel.UserViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun HomeScreen(
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  categoryViewModel: CategoryViewModel,
  userViewModel: UserViewModel,
  navController: NavController
) {

  val activity = (LocalContext.current as? Activity)
  BackHandler { activity?.finish() }

  val user = userViewModel.user
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(vertical = 8.dp, horizontal = 8.dp)
  ) {
    HomeSection(title = stringResource(R.string.category)) {
      CategoryRow(
        navController = navController,
        viewModel = categoryViewModel
      )
    }
//    HomeSection(title = stringResource(R.string.latest)) {
//      ProductImageRow(
//        productViewModel = productViewModel,
//        userViewModel = userViewModel,
//        navController = navController
//      )
//    }
    HomeSection(title = stringResource(R.string.recommended)) {
      Recommended(
        productViewModel = productViewModel,
        navController = navController,
        userViewModel = userViewModel
      )
    }
    HomeSection(title = stringResource(R.string.products)) {
      ProductList(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        products =  productViewModel.products,
        user = user,
        navController = navController
      )
    }
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
