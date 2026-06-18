package com.quizly.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from the WPF TextBox/PasswordBox styling used across LoginView/RegisterView. */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, color = QuizlyWhite, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color(0x33FFFFFF),
                unfocusedContainerColor = androidx.compose.ui.graphics.Color(0x33FFFFFF),
                focusedTextColor = QuizlyWhite,
                unfocusedTextColor = QuizlyWhite,
                cursorColor = QuizlyWhite,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
    }
}

@Composable
fun ErrorText(text: String) {
    if (text.isNotBlank()) {
        Text(text, color = androidx.compose.ui.graphics.Color(0xFFFFA3A3), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}
