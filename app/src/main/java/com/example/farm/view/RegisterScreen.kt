package com.example.farm.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
 * RegisterScreen.kt
 * Written By: Jing Wen Ng
 *
 * User able to register an account.
 * Some validation has been done, for example password validation and email validation.
 * All of the information must be filled up.
 *
 *
 *@param navController
 *@param userViewModel
 */


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel,
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var validation by remember { mutableStateOf("") }
    var enable= false


    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp



    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.createAccountTitle),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

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
                text = stringResource(R.string.name),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = name,
                onValueChange = { it ->
                    name = it
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

        Column(
        ) {
            Text(
                text = stringResource(R.string.occupation),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = occupation,
                onValueChange = { it ->
                    occupation = it
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


        //validation check
        val users by userViewModel.users.observeAsState()
        var existEmail = false
        if(email != "" && password != "" && confirmPassword!="" && occupation!="") {
            users?.forEach {
                if (email.trim() == it.email) {
                    existEmail = true
                }
            }

            if(existEmail){
                validation = stringResource(R.string.existUser)
            }
            else if (!checkEmail(email)) {
                validation = stringResource(R.string.invalidEmail)
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
                    userViewModel.insert(
                        email = email,
                        password = hashedPassword,
                        name = name,
                        occupation = occupation,
                        salt = salt
                    )
                    navController.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .width(screenWidth * 0.5f),
            enabled = enable,
            colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor)
        ) {
            Text(
                text = stringResource(R.string.signUp),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = whiteColor
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.backSignIn), color = greyColor, fontSize=14.sp)
            Text(text = " ")
            Text(
                text = stringResource(R.string.login),
                color = primaryColor,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Login.route)
                    },
                fontWeight = FontWeight.Bold,
                fontSize=14.sp
            )

        }
    }

}


fun checkLengthPass(password: String): Boolean {
    return !(password.length in 8..20)
}

fun checkUpperCase(password: String): Boolean {
    return (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null)
}

fun checkLowerCase(password: String): Boolean {
    return (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null)
}

fun checkContainDigit(password: String): Boolean {
    return (password.filter { it.isDigit() }.firstOrNull() == null)
}

fun checkEmail(email: String): Boolean{
    var emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"

    return email.matches(emailPattern.toRegex())

}