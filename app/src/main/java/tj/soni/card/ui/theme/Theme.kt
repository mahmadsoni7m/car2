package tj.soni.card.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Teal = Color(0xFF087C70)
private val Navy = Color(0xFF102D3A)
private val Scheme = lightColorScheme(primary = Teal, onPrimary = Color.White, secondary = Color(0xFF36B99A), background = Color(0xFFF1F6F8), surface = Color.White, onSurface = Navy, error = Color(0xFFC74747))
@Composable fun SoniTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = Scheme, content = content) }
