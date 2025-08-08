package bi.vovota.eac

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import bi.vovota.eac.data.model.TokenManager
import bi.vovota.eac.data.model.UserManager
import bi.vovota.eac.ui.nav.EcommerceApp
import bi.vovota.eac.ui.theme.EacTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    TokenManager.init(applicationContext)
    UserManager.init(applicationContext)
    setContent {
      EacTheme(darkTheme = false) {
        EcommerceApp()
      }
    }
  }
}