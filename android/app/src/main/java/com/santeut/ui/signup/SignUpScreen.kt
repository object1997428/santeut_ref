package com.santeut.ui.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santeut.R

@Composable
fun SignUpScreen(
    onNavigateLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val effect by viewModel.uiEvent.collectAsStateWithLifecycle()

    val userLoginId = viewModel.userLoginId.value
    val userNickName = viewModel.userNickName.value
    val userPassword = viewModel.userPassword.value
    val userPassword2 = viewModel.userPassword2.value
    val userBirth = viewModel.userBirth.value
    val userGender = viewModel.userGender.value

    val errorMessage = viewModel.errorMessage.value

    val snackBarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackBarHostState)

    LaunchedEffect(effect) {
        if (effect is SignUpViewModel.SignUpUiEvent.SignUp) {
            val signUpEvent = effect as SignUpViewModel.SignUpUiEvent.SignUp
            if (signUpEvent.success) {
                Log.d("SignUp Screen", "성공")
                onNavigateLogin()
            } else {
                Log.d("SignUp Screen", "실패")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            viewModel.clearErrorMessage()
        }
    }

    val genderExpanded = remember { mutableStateOf(false) }
    val selectGender = remember { mutableStateOf("성별") }

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
            Box(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillWidth,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "회원가입",
                    fontSize = 24.sp,
                    color = Color(0xFF335C49),
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                    viewModel.onEvent(SignUpEvent.EnteredUserLoginId(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = userNickName,
                label = { Text(text = "닉네임") },
                placeholder = { Text(text = "닉네임") },
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
                    viewModel.onEvent(SignUpEvent.EnteredUserNickName(newValue))
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
                    viewModel.onEvent(SignUpEvent.EnteredUserPassword(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = "  8자리 이상 입력",
                    color = Color(0xFF678C40),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userPassword2,
                label = { Text(text = "비밀번호 확인") },
                placeholder = { Text(text = "비밀번호 확인") },
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
                    viewModel.onEvent(SignUpEvent.EnteredUserPassword2(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = userBirth,
                    label = { Text(text = "생년월일") },
                    placeholder = { Text(text = "생년월일") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(8f),
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
                        viewModel.onEvent(SignUpEvent.EnteredUserBirth(newValue))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(10.dp, 8.dp, 0.dp, 0.dp)
                        .background(Color(0xFFFBF9ED))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF678C40),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { genderExpanded.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectGender.value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF678C40)
                    )
                    DropdownMenu(
                        expanded = genderExpanded.value,
                        onDismissRequest = { genderExpanded.value = false },
                        properties = PopupProperties(focusable = true),
                        modifier = Modifier
                            .width(56.dp)
                            .background(Color(0xFFFBF9ED), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("남", color = Color(0xFF678C40)) },
                            onClick = {
                                genderExpanded.value = false
                                selectGender.value = "남"
                                viewModel.onEvent(SignUpEvent.EnteredUserGender(true))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("여", color = Color(0xFF678C40)) },
                            onClick = {
                                genderExpanded.value = false
                                selectGender.value = "여"
                                viewModel.onEvent(SignUpEvent.EnteredUserGender(false))
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                modifier = Modifier.width(140.dp),
                onClick = {
                    viewModel.onEvent(SignUpEvent.SignUp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF678C40),
                    contentColor = Color(0xFFE5DD90),
                )
            ) {
                Text(text = "회원가입")
            }
        }
    }
}