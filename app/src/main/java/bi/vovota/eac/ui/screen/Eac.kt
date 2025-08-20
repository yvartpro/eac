package bi.vovota.eac.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bi.vovota.eac.data.model.Order
import bi.vovota.eac.data.model.Product
import bi.vovota.eac.viewmodel.CartViewModel
import bi.vovota.eac.viewmodel.OrderViewModel
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

@Composable
fun MerchantProductCompactCard(
    product: Product,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
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

            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}

@Composable
fun EditProduct(
    product: Product,
    productViewModel: ProductViewModel,
    onImageEditClick: () -> Unit,
    onFieldChange: (field: String, value: String) -> Unit,
    onRelatedProductEdit: (Product) -> Unit
) {
    val products = productViewModel.products
    val relatedProducts = products.filter { it.category == product.category && it.id != product.id }
    // Use vertical scroll for the whole content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Image with Edit button overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onImageEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Product Image",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Editable fields
        OutlinedTextField(
            value = product.name,
            onValueChange = { onFieldChange("name", it) },
            label = { Text("Product Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = product.bdiPrice,
            onValueChange = { onFieldChange("price", it) },
            label = { Text("Price") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = product.name ?: "",
            onValueChange = { onFieldChange("description", it) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(Modifier.height(24.dp))

        // Related Products Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Related Products",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            // Optional: maybe a see all button or icon can go here
        }

        Spacer(Modifier.height(12.dp))

        // Related products horizontally scrollable row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(relatedProducts) { relatedProduct ->
                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { onRelatedProductEdit(relatedProduct) },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = relatedProduct.image,
                            contentDescription = relatedProduct.name,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            relatedProduct.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                        Text(
                            relatedProduct.bdiPrice,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(6.dp))
                        IconButton(
                            onClick = { onRelatedProductEdit(relatedProduct) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Related Product",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommandCompactCard(
    order: Order,
    isExpanded: Boolean,
    clientName: String,
    amount: String,
    itemCount: Int,
    onMarkPaid: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(clientName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text("$amount • $itemCount items", style = MaterialTheme.typography.labelSmall)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onMarkPaid) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Mark Paid", tint = MaterialTheme.colorScheme.secondary)
                }
                IconButton(onClick = onReject) {
                    Icon(Icons.Default.Close, contentDescription = "Reject", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = order.date ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MerchantDashboardBody(
    merchantName: String = "Namugara Stacy",
    products: List<Product>,
    onEditProduct: (Product) -> Unit,
    onOrderClick: (Order) -> Unit,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    orderViewModel: OrderViewModel
) {
    val user = userViewModel.user

    val orders = orderViewModel.orders
    val ownOrders = orders.filter { it.buyer == user?.id }.sortedByDescending { it.date }

    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
        orderViewModel.loadOrders()
    }
    var expandedOrderId by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header
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
                    MerchantProductCompactCard(product = product, onEditClick = { onEditProduct(product) })
                }
            }
        }

        // Orders Section
        item { SectionHeader("Recent Orders", Icons.Default.FavoriteBorder) }
        item {
            OrdersList(orders = ownOrders, productViewModel = productViewModel, onMarkPaid = { }, onReject = { })
        }
    }
}

@Composable
fun OrdersList(
    orders: List<Order>,
    productViewModel: ProductViewModel,
    onMarkPaid: (Order) -> Unit,
    onReject: (Order) -> Unit
) {
    var expandedOrderId by remember { mutableStateOf<Int?>(null) }
    // You can manage paid state here if needed (or in your ViewModel)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (order in orders) {
            val isExpanded = expandedOrderId == order.id

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedOrderId = if (isExpanded) null else order.id }
                    .padding(vertical = 4.dp)
            ) {
                CommandCompactCard(
                    order = order,
                    isExpanded = isExpanded,
                    clientName = "Client ${order.buyer}",  // adjust as needed
                    amount = "2000 FBu",            // make sure order.amount exists
                    itemCount = 12,          // assuming itemCount in Order model
                    onMarkPaid = { onMarkPaid(order) },
                    onReject = { onReject(order) }
                )
            }
        }
    }
}