package com.example.farm.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
 * ForgotPasswordScreen.kt
 * Written By: Jing Wen Ng
 *
 * In this page user able to change their password.
 * Password validation(Password Must Contain Numbers, Uppercase, LowerCase and Length Should be between 8 to 20.) has been done and password and confirm password must be match before submit to database.
 * User must had been created an account to change the password.
 * Password will be hashed before saved password into the database.
 *
 *
 *@param navController
 *@param userViewModel
 */


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(navController : NavController,
                         userViewModel : UserViewModel){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var enable by remember { mutableStateOf(false) }
    var validation by remember { mutableStateOf("") }
    var getPassword by remember { mutableStateOf("") }
    var notExistEmail by remember { mutableStateOf(true) }

    val user by userViewModel.users.observeAsState()


    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    // get the screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp


    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.forgetPasswordTitle),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = validation, color = redColor, textAlign = TextAlign.Center)
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
                    .wrapContentHeight()
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
                    .wrapContentHeight()
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

        Column(
        ) {
            Text(
                text = stringResource(R.string.confirmPassword),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { it ->
                    confirmPassword = it
                },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp)
                    .width(screenWidth * 0.8f)
                    .wrapContentHeight()
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = @Composable {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            painter = if (showConfirmPassword)
                                painterResource(id = R.drawable.eye_off)
                            else
                                painterResource(id = R.drawable.eye_show),
                            contentDescription = "",
                            Modifier.size(24.dp)
                        )
                    }
                },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = greyColor,
                    cursorColor = primaryColor,
                    focusedLabelColor = primaryColor
                )

            )
        }

        //validation
        if(email != "" && password != "" && confirmPassword!="") {
            user?.forEach {
                if (email.trim() == it.email) {
                    notExistEmail = false
                }
            }
            if(notExistEmail){
                validation = stringResource(R.string.notExistUser)
            }
           else if (password != confirmPassword) {
                validation = stringResource(R.string.passwordNotMatch)
            }
            else if (checkLengthPass(password) || checkUpperCase(password) || checkLowerCase(password) || checkContainDigit(password)) {
                validation = stringResource(R.string.passwordValidation)
            }
            else {
                validation = ""
                enable = true
            }
        }



        Button(
            onClick = {
                    val salt = BCrypt.gensalt()
                    val hashedPassword = BCrypt.hashpw(password, salt)
                    user?.forEach {
                        if (email.trim() == it.email) {
                            getPassword = it.password
                            var getUser = it
                            getUser.password = hashedPassword
                            userViewModel.update(getUser)
                        }
                    }
                navController.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(screenWidth * 0.5f),
            colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
            enabled = enable
        ) {
            Text(
                text = stringResource(R.string.updatePassword),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = whiteColor
            )
        }

        Text(
            text = stringResource(R.string.back),
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate(Screen.Login.route)
            }
        )

    }

}