package bi.vovota.eac.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bi.vovota.eac.R
import bi.vovota.eac.data.model.UserRegister
import bi.vovota.eac.ui.component.PhoneInputField
import bi.vovota.eac.ui.component.ProfileTextField
import bi.vovota.eac.ui.component.RadioButtons
import bi.vovota.eac.ui.component.RadioCheckBox
import bi.vovota.eac.ui.component.RadioSelect
import bi.vovota.eac.ui.component.SmallText
import bi.vovota.eac.ui.nav.NavDestinations
import bi.vovota.eac.ui.theme.FontSizes
import bi.vovota.eac.viewmodel.AuthViewModel
import bi.vovota.eac.viewmodel.UserViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AuthScreen(
modifier: Modifier = Modifier,
onBackClick: () -> Unit,
navController: NavController,
viewModel: AuthViewModel = hiltViewModel(),
onLoginSuccess: () -> Unit
) {
  var isLogin by rememberSaveable { mutableStateOf(true) }
  var fullName by rememberSaveable { mutableStateOf("") }
  var phone by rememberSaveable { mutableStateOf("") }
  var types by rememberSaveable { mutableStateOf("BUYER") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordV by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  val loading by viewModel.loading.collectAsState()
  val message by viewModel.message.collectAsState()
  val isError by viewModel.isError.collectAsState()
  val loginSuccess by viewModel.loginSuccess.collectAsState()


  LaunchedEffect(loginSuccess) {
    if (loginSuccess) {
      onLoginSuccess()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp)
      .background(color = MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .imePadding(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.Start
  ) {
    Column (
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.Start
    ){
        IconButton(onClick = { navController.navigate(NavDestinations.HOME) }) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
        }
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Row(){
        Text(
          text = if(isLogin) "Welcome to" else "Create your account",
          fontWeight = FontWeight.Bold,
          fontSize = FontSizes.title(),
          color = MaterialTheme.colorScheme.primary
        )
        if( isLogin) {
          Spacer(Modifier.width(4.dp))
          Text(
            text = stringResource(R.string.app_name).capitalize(),
            fontWeight = FontWeight.Bold,
            fontSize = FontSizes.title(),
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
      Spacer(Modifier.height(3.dp))
      Text(
        text = "Shop from anywhere. Grab EAC best products at hand",
        fontSize = FontSizes.caption(),
        fontWeight = FontWeight.Light,
        color = Color.Gray
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Column(
      modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
      val userTypes = listOf("BUYER", "SELLER")

      if(!isLogin) {
        RadioSelect(
          options = userTypes,
          selected = types,
          onOptionSelected = { new -> types = new}
        )
        ProfileTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = stringResource(R.string.f_name),
          leadingIconVector = Icons.Filled.Person,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next,
          isSensitive = false
        )
      }
      CountryDropdownWithFlags(onPhoneNumberChanged = { phone = it })
      ProfileTextField(
        value = password,
        onValueChange = { password = it },
        label = stringResource(R.string.f_pwd),
        leadingIconVector = Icons.Filled.Lock,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
          val description = if (passwordVisible) "Hide password" else "Show password"
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(painter = if (passwordVisible) painterResource(R.drawable.visibility_off) else painterResource(R.drawable.visibility), description)
          }
        },
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next,
        isSensitive = true,
        placeholderText = stringResource(R.string.f_enter_pwd)
      )
      if(!isLogin) {
        ProfileTextField(
          value = passwordV,
          onValueChange = { passwordV = it },
          label = stringResource(R.string.pwd_v),
          leadingIconVector = Icons.Filled.Lock,
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(painter = if (passwordVisible) painterResource(R.drawable.visibility_off) else painterResource(R.drawable.visibility), description)
            }
          },
          keyboardType = KeyboardType.Password,
          imeAction = ImeAction.Done,
          isSensitive = true,
          placeholderText = stringResource(R.string.pwd_v)
        )
        if (viewModel.pwdUnmatch.value) {
          SmallText(text = stringResource(R.string.pwd_unmatch), color = MaterialTheme.colorScheme.error)
        }
      }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        if (isLogin) {
          viewModel.login(phone, password)
        } else {
          viewModel.verifyPwd(password, passwordV)
          if (!viewModel.pwdUnmatch.value) {
            viewModel.register(fullName, phone, types, password)
            isLogin = viewModel.registerOk.value
            fullName = ""
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    ) {
      Text(if (loading) stringResource(R.string.auth_wait) else if (isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_signin))
    }

    // Show message
    if (message.isNotBlank()) {
      SmallText(text = message, color = if (isError) MaterialTheme.colorScheme.onError  else MaterialTheme.colorScheme.primary)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ){
      Text(text = if (isLogin) stringResource(R.string.auth_not_yet_user) else stringResource(R.string.auth_user_yet) )
      TextButton(onClick = { isLogin = !isLogin}){
        Text(text = if(isLogin) stringResource(R.string.auth_create_one) else stringResource(R.string.auth_login_here))
      }
    }
  }
}

data class Country(
  val name: String,
  val code: String,
  @DrawableRes val flag: Int,
  val initial: String,
  val numberLength: Int
)

val countryList = listOf(
  Country("Burundi", "257", R.drawable.bi, "BDI", 8),
  Country("DRC", "243", R.drawable.cd, "RDC", 9),
  Country("Kenya", "254", R.drawable.ke, "KE", 9),
  Country("Ouganda", "256", R.drawable.ug, "UG", 9),
  Country("Rwanda", "250", R.drawable.rw, "RW", 9),
  Country("Tanzania", "255", R.drawable.tz, "TZ", 9),
  Country("South Soudan", "211", R.drawable.ss, "SS", 9),
  Country("Somalia", "252", R.drawable.so, "SO", 9)
)


@Composable
fun CountryDropdownWithFlags(
  onPhoneNumberChanged: (String) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedCountry by remember { mutableStateOf(countryList[0]) }
  var phoneNumber by remember { mutableStateOf("") }
  var isValid by remember { mutableStateOf(true) }

  OutlinedTextField(
    value = phoneNumber,
    onValueChange = {
      phoneNumber = it
      isValid = it.length == selectedCountry.numberLength

      if (isValid) {
        val fullNumber = "${selectedCountry.code}$phoneNumber"
        onPhoneNumberChanged("$fullNumber")
      }
    },
    label = { Text("Phone number") },
    isError = !isValid,
    leadingIcon = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .padding(start = 8.dp)
          .clickable { expanded = true }
      ) {
        Image(
          painter = painterResource(id = selectedCountry.flag),
          contentDescription = null,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
          contentDescription = null,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "+${selectedCountry.code}")
        Spacer(modifier = Modifier.width(4.dp))
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    modifier = Modifier.fillMaxWidth().padding(vertical = 0.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = MaterialTheme.colorScheme.primary,
      unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    )
  )

  DropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false }
  ) {
    countryList.forEach { country ->
      DropdownMenuItem(
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = country.flag),
              contentDescription = null,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${country.name} (+${country.code})")
          }
        },
        onClick = {
          selectedCountry = country
          expanded = false
          phoneNumber = ""
          isValid = true
        }
      )
    }
  }
}