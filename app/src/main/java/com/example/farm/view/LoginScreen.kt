package com.example.farm.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.navigation.Screen
import com.example.farm.viewmodel.UserViewModel
import com.example.farm.view.*
import org.mindrot.jbcrypt.BCrypt

/**
 * LoginScreen.kt
 * Written By: Jing Wen Ng
 *
 * This is a login page. The first page.
 * In this page can be navigate to Register Page and Forget Password Page.
 * Login authentication has been done in this page.
 *
 *
 *@param navController
 *@param userViewModel
 */



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController:  NavController,
                userViewModel: UserViewModel
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var enable by remember { mutableStateOf(false) }
    var loginSucess by remember { mutableStateOf(false) }
    var validation by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    //get all the users
    val user by userViewModel.users.observeAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp


    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Image Logo",
                contentScale = ContentScale.Fit
            )
        }

        //app's name
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        //validation text
        Column(modifier=Modifier.padding(bottom=10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text=validation, color= redColor,textAlign= TextAlign.Center)
        }

        Column(

        ) {
            Text(
                text = stringResource(R.string.email),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = email,
                onValueChange = { it ->
                    email = it
                },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .width(screenWidth * 0.8f)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = greyColor,
                    cursorColor = primaryColor,
                    focusedLabelColor = primaryColor
                )

            )
        }

        Column(
        ) {
            Text(
                text = stringResource(R.string.password),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = password,
                onValueChange = { it ->
                    password = it
                },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .width(screenWidth * 0.8f)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = @Composable {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = if (showPassword)
                                painterResource(id = R.drawable.eye_off)
                            else
                                painterResource(id = R.drawable.eye_show),
                            contentDescription = "",
                            Modifier.size(24.dp)
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = greyColor,
                    cursorColor = primaryColor,
                    focusedLabelColor = primaryColor
                )

            )
        }




        var getPassword = ""

        if (email != "" && password != "") {
            enable = true
        }


        Button(
            onClick = {
                if (email != "" && password != "") {
                    user?.forEach {
                        if (email.trim() == it.email) {
                            getPassword = it.password
                            if (BCrypt.checkpw(password,it.password)) {
                                loginSucess = true
                            } else {
                                loginSucess = false
                                validation = "Email or Password is Incorrect"
                            }
                        }
                    }
                }
                if(loginSucess){
                    navController.navigate(Screen.FieldList.route)
                    userViewModel.getUser = email
                }
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(screenWidth * 0.5f),
            colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
            enabled = enable
        ) {
            Text(
                text = stringResource(R.string.login),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = whiteColor
            )
        }

        //sign up
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.createAccount), color = greyColor, fontSize=14.sp)
            Text(text = " ")
            Text(
                text = stringResource(R.string.signUp),
                color = primaryColor,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Register.route)
                    },
                fontWeight = FontWeight.Bold,
                fontSize=14.sp
            )

        }

        //forget password
        Column(){
            Text(text= stringResource(R.string.forgotPassword),
                color = primaryColor,fontSize=14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("forgot_password")
            })

        }
    }


}

