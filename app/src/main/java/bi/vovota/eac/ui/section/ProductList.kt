package bi.vovota.eac.ui.section

import ProductCardShimmer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import bi.vovota.eac.R
import bi.vovota.eac.data.model.Product
import bi.vovota.eac.data.model.User
import bi.vovota.eac.ui.theme.FontSizes
import bi.vovota.eac.ui.theme.Spacings
import bi.vovota.eac.viewmodel.CartViewModel
import bi.vovota.eac.viewmodel.ProductViewModel
import bi.vovota.eac.viewmodel.UserViewModel

@Composable
fun ProductList(
  cartViewModel: CartViewModel,
  products: List<Product>,
  productViewModel: ProductViewModel,
  navController: NavController,
  user: User?,
) {

  val isLoading = productViewModel.isLoading
  val chunks = if (isLoading) List(5) { listOf(null, null) } else products.chunked(2)

  if (productViewModel.emptyMsg != "") {
    cartViewModel.notifyAdded(productViewModel.emptyMsg.toString())
  }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    chunks.forEach { rowItems ->
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        for (product in rowItems) {
          if (product == null) {
            ProductCardShimmer(modifier = Modifier.weight(1f))
          }else {
            ProductCard(
              cartViewModel =  cartViewModel,
              product = product,
              modifier = Modifier.weight(1f),
              navController = navController,
              user = user,
              productViewModel = productViewModel
            )
          }
        }
        if (rowItems.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
fun ProductCard(
  cartViewModel: CartViewModel,
  product: Product,
  modifier: Modifier = Modifier,
  navController: NavController,
  user: User?,
  productViewModel: ProductViewModel
) {
  val price = productViewModel.getPrice(product, user)
  val currency = productViewModel.getCurrency(user)

  // Stable "random" choice: based on product.id
  val styleType = remember(product.id) { (product.id % 3) } // 0,1,2

  when (styleType) {
    0 -> ProductCardFull(cartViewModel, product, price, currency, modifier, navController)
    1 -> ProductCardCompact(product, price, currency, modifier, navController)
    2 -> ProductCardImageFocus(cartViewModel, product, price, currency, modifier, navController)
  }
}

@Composable
fun ProductCardFull(
  cartViewModel: CartViewModel,
  product: Product,
  price: String,
  currency: String,
  modifier: Modifier,
  navController: NavController
) {
  Card(
    modifier = modifier
      .width(IntrinsicSize.Max)
      .clickable { navController.navigate("product/${product.id}") },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    Column(
      modifier = Modifier.padding(12.dp)
    ) {
      AsyncImage(
        model = product.image,
        contentDescription = product.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .clip(RoundedCornerShape(12.dp))
      )
      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = product.name,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = product.company.name,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "$price $currency",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        FilledIconButton(
          onClick = { cartViewModel.addToCart(product) },
          shape = CircleShape,
          modifier = Modifier.size(36.dp),
        ) {
          Icon(
            painter = painterResource(id = R.drawable.add_to_cart),
            contentDescription = "Add to cart",
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
fun ProductCardCompact(
  product: Product,
  price: String,
  currency: String,
  modifier: Modifier,
  navController: NavController
) {
  Card(
    modifier = modifier
      .clickable { navController.navigate("product/${product.id}") },
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      AsyncImage(
        model = product.image,
        contentDescription = product.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = product.name,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = "$price $currency",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
    }
  }
}

@Composable
fun ProductCardImageFocus(
  cartViewModel: CartViewModel,
  product: Product,
  price: String,
  currency: String,
  modifier: Modifier,
  navController: NavController
) {
  Card(
    modifier = modifier
      .clickable { navController.navigate("product/${product.id}") },
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Box {
      AsyncImage(
        model = product.image,
        contentDescription = product.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
      )
      // Overlay price bar
      Row(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(product.name, fontWeight = FontWeight.SemiBold)
          Text("$price $currency", color = MaterialTheme.colorScheme.primary)
        }
        FilledIconButton(
          onClick = { cartViewModel.addToCart(product) },
          shape = CircleShape,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            painter = painterResource(id = R.drawable.add_to_cart),
            contentDescription = "Add to cart",
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
