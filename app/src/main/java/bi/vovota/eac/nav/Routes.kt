package bi.vovota.eac.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bi.vovota.eac.R

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val CART = "cart"
    const val COMPANIES = "companies"
}

data class BottomNavItem(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(R.string.b_home, R.drawable.home, Routes.HOME),
    BottomNavItem(R.string.b_industry, R.drawable.business, Routes.COMPANIES),
    BottomNavItem(R.string.b_cart, R.drawable.shopping_cart, Routes.CART),
    BottomNavItem(R.string.b_profile, R.drawable.person, Routes.PROFILE)
)