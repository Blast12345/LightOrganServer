package gui.basicComponents

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SimpleText(
    text: String,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        modifier = modifier
    )
}
