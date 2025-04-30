package com.santeut.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santeut.R

@Composable
fun LoginScreen(
    onNavigateSignUp: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val effect by viewModel.uiEvent.collectAsStateWithLifecycle()

    val userLoginId = viewModel.userLoginId.value
    val userPassword = viewModel.userPassword.value

    val errorMessage = viewModel.errorMessage.value

    LaunchedEffect(effect) {
        if (effect is LoginViewModel.LoginUiEvent.Login) {
            val loginEvent = effect as LoginViewModel.LoginUiEvent.Login
            if (loginEvent.success) {
                Log.d("Login Screen", "성공")
                onNavigateHome()
            } else {
                Log.d("Login Screen", "실패")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.None,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "로그인",
                    fontSize = 24.sp,
                    color = Color(0xFF335C49),
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = userLoginId,
                label = { Text(text = "아이디") },
                placeholder = { Text(text = "아이디") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color(0xFF678C40),
                    unfocusedBorderColor = Color(0xFF678C40),
                    unfocusedLabelColor = Color(0xFF678C40),
                    unfocusedContainerColor = Color(0xFFFBF9ED),

                    focusedTextColor = Color(0xFF678C40),
                    focusedBorderColor = Color(0xFF678C40),
                    focusedLabelColor = Color(0xFF678C40),
                    focusedContainerColor = Color(0xFFFBF9ED),
                ),
                onValueChange = { newValue ->
                    viewModel.onEvent(LoginEvent.EnteredUserLoginId(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = userPassword,
                label = { Text(text = "비밀번호") },
                placeholder = { Text(text = "비밀번호") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color(0xFF678C40),
                    unfocusedBorderColor = Color(0xFF678C40),
                    unfocusedLabelColor = Color(0xFF678C40),
                    unfocusedContainerColor = Color(0xFFFBF9ED),

                    focusedTextColor = Color(0xFF678C40),
                    focusedBorderColor = Color(0xFF678C40),
                    focusedLabelColor = Color(0xFF678C40),
                    focusedContainerColor = Color(0xFFFBF9ED),
                ),
                onValueChange = { newValue ->
                    viewModel.onEvent(LoginEvent.EnteredUserPassword(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "비밀번호를 잊으셨나요?",
                    color = Color(0xFF335C49)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        viewModel.onEvent(LoginEvent.Login)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF678C40),
                        contentColor = Color(0xFFE5DD90),
                    )
                ) {
                    Text(text = "로그인")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        onNavigateSignUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF678C40),
                    ),
                    border = BorderStroke(1.dp, Color(0xFF678C40))
                ) {
                    Text(text = "회원가입")
                }
            }
        }
    }
}