package tj.soni.card.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun SoniLogo(compact: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Box(Modifier.size(if (compact) 34.dp else 42.dp).background(Color(0xFF087C70), RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
            Text("S", color = Color.White, fontWeight = FontWeight.Black, fontSize = if (compact) 20.sp else 25.sp)
        }
        Column { Text("SONI CARD", fontWeight = FontWeight.ExtraBold, color = Color(0xFF102D3A), fontSize = if (compact) 15.sp else 19.sp, letterSpacing = 1.sp); if (!compact) Text("DIGITAL WALLET", fontSize = 9.sp, color = Color(0xFF66818A), letterSpacing = 2.sp) }
    }
}

@Composable fun PremiumCard(name: String, number: String = "••••  ••••  ••••  4821", frozen: Boolean = false) {
    Box(Modifier.fillMaxWidth().height(190.dp).background(Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE4EAEC), Color(0xFFF9FBFB))), RoundedCornerShape(24.dp)).padding(22.dp)) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { SoniLogo(true); Text("VIRTUAL", color = Color(0xFF66818A), fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(number, color = Color(0xFF102D3A), fontSize = 18.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Medium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text("CARDHOLDER", fontSize = 8.sp, color = Color.Gray); Text(name.uppercase(), fontSize = 12.sp, color = Color(0xFF102D3A), fontWeight = FontWeight.Bold) }; Column(horizontalAlignment = Alignment.End) { Text("VALID THRU", fontSize = 8.sp, color = Color.Gray); Text("•• / ••", fontSize = 12.sp, color = Color(0xFF102D3A), fontWeight = FontWeight.Bold) } }
            }
        }
        if (frozen) Text("FROZEN · DEMO", Modifier.align(Alignment.Center).background(Color(0xCC102D3A), RoundedCornerShape(8.dp)).padding(10.dp), color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable fun SectionTitle(title: String, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF102D3A)); if (action != null) TextButton(onClick = { onAction?.invoke() }) { Text(action) } }
}

@Composable fun InfoTile(title: String, value: String, tint: Color = Color(0xFF087C70)) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.defaultMinSize(minHeight = 86.dp)) { Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, color = Color(0xFF66818A), fontSize = 12.sp); Text(value, color = tint, fontSize = 17.sp, fontWeight = FontWeight.Bold) } }
}
