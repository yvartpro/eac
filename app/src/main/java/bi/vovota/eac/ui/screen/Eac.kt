package bi.vovota.eac.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bi.vovota.eac.data.model.Order
import bi.vovota.eac.data.model.Product
import bi.vovota.eac.viewmodel.CartViewModel
import bi.vovota.eac.viewmodel.ProductViewModel
import bi.vovota.eac.viewmodel.UserViewModel
import coil.compose.AsyncImage

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(product.bdiPrice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Order #${order.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text("Status: ${order.isPayed}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// ---------------- Sample Models ----------------
data class ProductUI(val name: String, val price: String, val description: String, val imageRes: Int)
data class OrderUI(val id: String, val status: String)



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MerchantDashboardBody(
    merchantName: String = "Namugara Stacy",
    products: List<Product>,
//    orders: List<Order>,
    onEditProduct: (Product) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                "Welcome, $merchantName",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Products Section
        item { SectionHeader("Your Products", Icons.Default.Face) }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                products.forEach { product ->
                    MerchantProductCard(
                        product = product,
                        onEditClick = { onEditProduct(product) }
                    )
                }
            }
        }

        // Orders Section
        item { SectionHeader("Recent Orders", Icons.Default.FavoriteBorder) }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                orders.forEach { order ->
                    OrderMiniCard(
                        order = order,
                        onClick = { onOrderClick(order) }
                    )
                }
            }

        }
    }
}

@Composable
fun CustomerProfileBody(
    customerName: String,
    phone: String,
    orders: List<Order>,
    onOrderClick: (Order) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Profile Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(customerName, style = MaterialTheme.typography.titleLarge)
                    Text(phone, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        // Orders
        item {
            SectionHeader("My Orders", Icons.Default.MailOutline)
        }
        items(orders.size) { index ->
            OrderCard(order = orders[index], onClick = { onOrderClick(orders[index]) })
        }
    }
}

@Composable
fun ProductDetailBody(
    product: Product,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    //onProductClick: (Product) -> Unit
) {
    val products = productViewModel.products
    val relatedProducts = products.filter { it.category == product.category && it.id != product.id }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Price: ${product.bdiPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(12.dp))
                Text("Products have no description to show for now but later will do", style = MaterialTheme.typography.bodyMedium)
            }
        }
        // Other products
        item {
            SectionHeader("More from this seller", Icons.Default.MoreVert)
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(relatedProducts.size) { index ->
                    ProductCard(
                        product = relatedProducts[index],
                        onClick = { navController.navigate("details/${(relatedProducts[index])}") }
                    )
                }
            }
        }
    }
}

    @Composable
    fun MerchantProductCard(
        product: Product,
        onEditClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .width(160.dp) // auto width in FlowRow
                .clickable { onEditClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                Box {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Product",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Column(Modifier.padding(8.dp)) {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    Text(
                        product.bdiPrice,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

@Composable
fun OrderMiniCard(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Order #${order.id}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            StatusChip(status = order.isDelivered.toString())
        }
    }
}


@Composable
fun StatusChip(status: String) {
    Surface(
        color = when (status) {
            "Pending" -> MaterialTheme.colorScheme.tertiaryContainer
            "Delivered" -> MaterialTheme.colorScheme.secondaryContainer
            "Cancelled" -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
        val orders = listOf(
    Order(1, "Order description",true, false, 1,"2025-08-4"),
    Order(1, "Order description",true, false, 1,"2025-08-4"),
    Order(1, "Order description",true, false, 1,"2025-08-4"),
    Order(1, "Order description",true, false, 1,"2025-08-6"),
    Order(1, "Order description",true, false, 1,"2025-08-7"),
    Order(1, "Order description",true, false, 1,"2025-08-8"),
)
