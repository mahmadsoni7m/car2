package tj.soni.card.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import android.content.ContextWrapper
import tj.soni.card.data.local.Preferences
import tj.soni.card.security.BiometricAuth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tj.soni.card.data.repository.DemoRepository
import tj.soni.card.domain.Validation
import tj.soni.card.localization.Strings
import tj.soni.card.navigation.Routes
import tj.soni.card.ui.components.*
import kotlinx.coroutines.launch

private val repo = DemoRepository()

@Composable fun SoniCardApp() {
    val nav = rememberNavController()
    var language by remember { mutableStateOf("tg") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferences = remember { Preferences(context) }
    val savedLanguage by preferences.language.collectAsState(initial = "tg")
    LaunchedEffect(savedLanguage) { language = savedLanguage }
    val updateLanguage: (String) -> Unit = { selected ->
        language = selected
        scope.launch { preferences.setLanguage(selected) }
    }
    var frozen by remember { mutableStateOf(false) }
    var balanceVisible by remember { mutableStateOf(true) }
    val t: (String) -> String = { Strings.get(language, it) }
    val current by nav.currentBackStackEntryAsState()
    val route = current?.destination?.route ?: Routes.DASHBOARD
    Scaffold(containerColor = Color(0xFFF1F6F8), bottomBar = {
        if (route in listOf("dashboard", "cards", "history", "profile")) NavigationBar(containerColor = Color.White) {
            listOf("dashboard" to Icons.Default.Home, "cards" to Icons.Default.CreditCard, "history" to Icons.Default.ReceiptLong, "profile" to Icons.Default.Person).forEach { (r, icon) ->
                NavigationBarItem(selected = route == r, onClick = { nav.navigate(r) { launchSingleTop = true } }, icon = { Icon(icon, null) }, label = { Text(t(when(r){"dashboard"->"home";else->r})) })
            }
        }
    }) { padding ->
        NavHost(navController = nav, startDestination = Routes.DASHBOARD, modifier = Modifier.padding(padding)) {
            Routes.all.forEach { destination -> composable(destination) {
                AnimatedContent(targetState = destination, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "screen") { target ->
                    Screen(target, nav, language, updateLanguage, t, frozen, { frozen = it }, balanceVisible, { balanceVisible = it }, context)
                }
            } }
        }
    }
}

@Composable private fun Screen(route: String, nav: NavHostController, language: String, setLanguage: (String)->Unit, t:(String)->String, frozen:Boolean, setFrozen:(Boolean)->Unit, balanceVisible:Boolean, setBalanceVisible:(Boolean)->Unit, context: android.content.Context) {
    var query by remember(route) { mutableStateOf("") }
    var filter by remember(route) { mutableStateOf("all") }
    var amount by remember(route) { mutableStateOf("") }
    var phone by remember(route) { mutableStateOf("") }
    var holder by remember(route) { mutableStateOf("Маҳмад") }
    var showDialog by remember(route) { mutableStateOf(false) }
    val visibleTransactions = repo.transactions.filter { tx ->
        (filter == "all" || (filter == "income" && tx.incoming) || (filter == "expense" && !tx.incoming)) && (query.isBlank() || tx.title.contains(query, true) || tx.id.contains(query, true))
    }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                if (route == "dashboard" || route == "splash" || route == "welcome") SoniLogo() else Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.Default.ArrowBack, t("back")) }; Text(screenTitle(route,t), fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color(0xFF102D3A)) }
                if (route == "dashboard") IconButton(onClick = { nav.navigate("notifications") }) { Icon(Icons.Default.NotificationsNone, "Notifications", tint = Color(0xFF102D3A)) }
            }
        }
        when(route) {
            "dashboard", "splash", "welcome", "onboarding" -> {
                item { Text("${t("welcome")}, Маҳмад 👋", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF102D3A)) }
                item { Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF087C70))) { Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(t("balance"), color = Color.White.copy(.8f)); Row(verticalAlignment = Alignment.CenterVertically) { Text(if (balanceVisible) "4 820.50 TJS" else "•••••• TJS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold); IconButton(onClick = { setBalanceVisible(!balanceVisible) }) { Icon(if(balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Toggle balance", tint = Color.White) } }; Text(t("demo"), color = Color.White.copy(.85f), fontSize = 11.sp) } } }
                item { PremiumCard("Маҳмад С." , frozen = frozen) }
                item { SectionTitle(t("quick")) }
                item { Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { listOf(Triple(t("transfer"), Icons.Default.SwapHoriz, "transfers"), Triple(t("topup"), Icons.Default.AddCircleOutline, "top_up"), Triple(t("payments"), Icons.Default.Receipt, "payments"), Triple(t("qr"), Icons.Default.QrCodeScanner, "qr_scanner")).forEach { (label, icon, target) -> Card(Modifier.weight(1f).clickable { nav.navigate(target) }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Column(Modifier.padding(vertical=14.dp).fillMaxWidth(), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(8.dp)) { Icon(icon, null, tint=Color(0xFF087C70)); Text(label, fontSize=10.sp, fontWeight=FontWeight.SemiBold) } } } } }
                item { SectionTitle(t("transactions"), t("see_all")) { nav.navigate("history") } }
                items(repo.transactions.take(3)) { tx -> TransactionRow(tx.title, tx.subtitle, tx.signedAmount, tx.incoming) { nav.navigate("transaction_details") } }
                item { Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) { InfoTile("Даромад", "+2 782.50", Color(0xFF21865C)); InfoTile("Хароҷот", "−309.50", Color(0xFFC74747)) } }
            }
            "cards" -> {
                item { PremiumCard(holder, frozen=frozen) }
                item { Card(shape=RoundedCornerShape(18.dp), colors=CardDefaults.cardColors(containerColor=Color.White)) { Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) { Text(t("card_status"), color=Color.Gray); Text(if(frozen) "Frozen · DEMO" else t("active"), fontWeight=FontWeight.Bold); Button(onClick={setFrozen(!frozen)}, modifier=Modifier.fillMaxWidth()) { Icon(if(frozen) Icons.Default.LockOpen else Icons.Default.Lock, null); Spacer(Modifier.width(8.dp)); Text(if(frozen)t("unfreeze") else t("freeze")) }; OutlinedButton(onClick={nav.navigate("card_details")}, modifier=Modifier.fillMaxWidth()) { Text(t("details")) } } } }
                item { OutlinedButton(onClick={nav.navigate("add_card")}, modifier=Modifier.fillMaxWidth()) { Icon(Icons.Default.Add,null); Text(t("add_card")) } }
            }
            "history" -> {
                item { OutlinedTextField(query, { query=Validation.search(it) }, modifier=Modifier.fillMaxWidth(), placeholder={Text(t("search"))}, leadingIcon={Icon(Icons.Default.Search,null)}, shape=RoundedCornerShape(16.dp), singleLine=true) }
                item { Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("all","income","expense").forEach { key -> FilterChip(selected=filter==key,onClick={filter=key},label={Text(t(key))}) } } }
                if(visibleTransactions.isEmpty()) item { EmptyState(t("empty")) }
                items(visibleTransactions) { tx -> TransactionRow(tx.title,tx.subtitle,tx.signedAmount,tx.incoming) { nav.navigate("transaction_details") } }
            }
            "profile" -> {
                item { Card(shape=RoundedCornerShape(22.dp), colors=CardDefaults.cardColors(containerColor=Color.White)) { Row(Modifier.padding(18.dp), verticalAlignment=Alignment.CenterVertically) { Box(Modifier.size(54.dp).background(Color(0xFFE0F2EE),CircleShape),contentAlignment=Alignment.Center){Text("М",fontSize=24.sp,color=Color(0xFF087C70),fontWeight=FontWeight.Bold)}; Spacer(Modifier.width(14.dp)); Column { Text("Маҳмад",fontWeight=FontWeight.Bold,fontSize=18.sp); Text("+992 ** *** ** 21",color=Color.Gray) } } } }
                listOf("security","settings","language","notifications","support","faq","about","transfers","top_up","withdraw","payments","qr_scanner","registration","phone_login","sms_verification","pin_login","biometric","onboarding","welcome").forEach { item { MenuRow(t(it), Icons.Default.ChevronRight) { nav.navigate(it) } } }
            }
            "language" -> {
                item { Text("Интихоби забон / Выбор языка / Choose language",color=Color.Gray) }
                listOf("tg" to "Тоҷикӣ", "ru" to "Русский", "en" to "English").forEach { (code,label) -> item { Card(Modifier.fillMaxWidth().clickable { setLanguage(code); Toast.makeText(context,"Language: $label",Toast.LENGTH_SHORT).show() }, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color.White)) { Row(Modifier.padding(18.dp), horizontalArrangement=Arrangement.SpaceBetween) { Text(label,fontWeight=FontWeight.SemiBold); if(language==code) Icon(Icons.Default.Check,color=Color(0xFF087C70),contentDescription=null) } } } }
            }
            "transfers", "top_up", "withdraw", "payments", "registration", "phone_login", "sms_verification", "pin_login", "transfer_confirmation" -> {
                item { Text(t("demo_action"), color=Color(0xFF66818A), fontSize=13.sp) }
                if(route=="registration") item { OutlinedTextField(holder,{holder=it},label={Text(t("name"))},modifier=Modifier.fillMaxWidth(),singleLine=true) }
                if(route in listOf("phone_login","registration","sms_verification")) item { OutlinedTextField(phone,{phone=it},label={Text(t("phone"))},modifier=Modifier.fillMaxWidth(),keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone),singleLine=true) }
                if(route in listOf("transfers","top_up","withdraw","payments","transfer_confirmation")) item { OutlinedTextField(amount,{amount=it},label={Text("Amount (TJS)")},modifier=Modifier.fillMaxWidth(),keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true) }
                item { Button(onClick={ showDialog=true },modifier=Modifier.fillMaxWidth(),enabled= if(route in listOf("phone_login","registration")) Validation.phone(phone) else true) { Text(if(route=="phone_login")t("continue") else t("confirm")) } }
            }
            "qr_scanner" -> { item { Box(Modifier.fillMaxWidth().height(250.dp).background(Color(0xFFE4EFED),RoundedCornerShape(24.dp)),contentAlignment=Alignment.Center) { Column(horizontalAlignment=Alignment.CenterHorizontally) { Icon(Icons.Default.QrCode2,null,tint=Color(0xFF087C70),modifier=Modifier.size(90.dp)); Text("QR demo preview",fontWeight=FontWeight.Bold) } } }; item { Text("Камера дар версияи намоишӣ фаъол нест.",color=Color.Gray) } }
            "statistics" -> { item { Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) { InfoTile(t("income"),"2 782.50 TJS",Color(0xFF21865C)); InfoTile(t("expense"),"309.50 TJS",Color(0xFFC74747)) } }; item { Text("Омори намунавӣ · Demo analytics",color=Color.Gray) }; item { repeat(4) { idx -> Column(verticalArrangement=Arrangement.spacedBy(6.dp)) { Text(listOf("Хӯрок","Харид","Хизматрасонӣ","Дигар")[idx],fontWeight=FontWeight.Medium); LinearProgressIndicator(progress={listOf(.72f,.48f,.3f,.18f)[idx]},modifier=Modifier.fillMaxWidth(),color=Color(0xFF36B99A),trackColor=Color(0xFFDCE8EA)) } } } }
            "notifications" -> item { EmptyState(t("notices")) }
            "transaction_details" -> { item { InfoTile("Transaction ID","SC-1001") }; item { InfoTile("Status","Completed · DEMO",Color(0xFF21865C)) }; item { Text("Маълумоти амалиётӣ танҳо барои намоиш аст.",color=Color.Gray) } }
            "security" -> { item { MenuRow("Biometric authentication",Icons.Default.Fingerprint) { nav.navigate("biometric") } }; item { MenuRow("Device protection",Icons.Default.Shield) { showDialog=true } }; item { Text("Маълумоти ҳассос нигоҳ дошта намешавад. PIN-и воқеӣ ворид накунед.",color=Color.Gray) } }
            "settings" -> { item { MenuRow(t("language"),Icons.Default.Language){nav.navigate("language")} }; item { MenuRow("Theme · Light",Icons.Default.LightMode){Toast.makeText(context,"Light theme is active",Toast.LENGTH_SHORT).show()} }; item { MenuRow(t("security"),Icons.Default.Security){nav.navigate("security")} } }
            "support" -> { item { Text("Дастгирии SONI CARD",fontWeight=FontWeight.Bold,fontSize=18.sp) }; item { MenuRow("Email: support@sonicard.demo",Icons.Default.Email){Toast.makeText(context,"Demo contact only",Toast.LENGTH_SHORT).show()} }; item { MenuRow("FAQ",Icons.Default.Help){nav.navigate("faq")} } }
            "faq" -> { listOf("Оё маблағ воқеӣ аст?" to "Не. Ҳама маълумот ва амалиётҳо demo мебошанд.","Оё интиқол кор мекунад?" to "Не. API-и бонкӣ пайваст нашудааст.","Оё маълумоти корт нигоҳ дошта мешавад?" to "Не. Рақами пурра ва CVV талаб карда намешавад.").forEach { (q,a) -> item { var expanded by remember { mutableStateOf(false) }; Card(Modifier.fillMaxWidth().clickable { expanded=!expanded },shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(containerColor=Color.White)) { Column(Modifier.padding(16.dp)) { Text(q,fontWeight=FontWeight.Bold); if(expanded) Text(a,color=Color.Gray,modifier=Modifier.padding(top=8.dp)) } } } } }
            "about" -> { item { SoniLogo() }; item { Text("SONI CARD v1.0",fontWeight=FontWeight.Bold,fontSize=20.sp) }; item { Text("Digital wallet interface concept. Original branding. Offline demo only.",color=Color.Gray) }; item { Text("© 2026 SONI CARD",color=Color.Gray) } }
            "card_details", "add_card" -> { item { PremiumCard(holder,frozen=frozen) }; item { Text("Барои амният танҳо рақами ниқобшуда нишон дода мешавад.",color=Color.Gray) }; item { InfoTile(t("card_status"),if(frozen) "Frozen · DEMO" else t("active")) } }
            "biometric" -> { item { Icon(Icons.Default.Fingerprint,null,modifier=Modifier.size(80.dp).align(Alignment.CenterHorizontally),tint=Color(0xFF087C70)) }; item { Text("Биометрия танҳо дар дастгоҳи дастгиришаванда дастрас аст.",color=Color.Gray) }; item { Button(onClick={ val activity = context.findFragmentActivity(); if (activity != null && BiometricAuth.isAvailable(activity)) BiometricAuth.prompt(activity) { ok -> Toast.makeText(context, if(ok) "Authentication successful" else "Authentication not completed", Toast.LENGTH_SHORT).show() } else Toast.makeText(context,"Biometric authentication is not available on this device",Toast.LENGTH_SHORT).show() },modifier=Modifier.fillMaxWidth()){Text(t("verify"))} } }
            else -> { item { EmptyState(t("empty")) } }
        }
        item { Spacer(Modifier.height(8.dp)); Text(t("demo"),fontSize=10.sp,color=Color(0xFF81969B),modifier=Modifier.fillMaxWidth()) }
    }
    if(showDialog) AlertDialog(onDismissRequest={showDialog=false},title={Text("SONI CARD · DEMO")},text={Text(t("demo_action"))},confirmButton={TextButton(onClick={showDialog=false}){Text(t("confirm"))}})
}

@Composable private fun TransactionRow(title:String,subtitle:String,amount:String,incoming:Boolean,onClick:()->Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick=onClick),shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(containerColor=Color.White)) { Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically) { Box(Modifier.size(42.dp).background(if(incoming)Color(0xFFE4F4ED) else Color(0xFFFCECEC),CircleShape),contentAlignment=Alignment.Center){Icon(if(incoming)Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,null,tint=if(incoming)Color(0xFF21865C) else Color(0xFFC74747))}; Column(Modifier.weight(1f).padding(start=12.dp)) { Text(title,fontWeight=FontWeight.SemiBold,color=Color(0xFF102D3A)); Text(subtitle,fontSize=11.sp,color=Color.Gray) }; Text(amount,fontWeight=FontWeight.Bold,color=if(incoming)Color(0xFF21865C) else Color(0xFF102D3A)) } }
}
@Composable private fun MenuRow(title:String,icon: androidx.compose.ui.graphics.vector.ImageVector,onClick:()->Unit) { Card(Modifier.fillMaxWidth().clickable(onClick=onClick),shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(containerColor=Color.White)) { Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically) { Icon(icon,null,tint=Color(0xFF087C70)); Text(title,Modifier.weight(1f).padding(start=12.dp),fontWeight=FontWeight.Medium); Icon(Icons.Default.ChevronRight,null,tint=Color.Gray) } } }
@Composable private fun EmptyState(text:String) { Column(Modifier.fillMaxWidth().padding(28.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)) { Icon(Icons.Default.Inbox,null,tint=Color(0xFF9BB1B5),modifier=Modifier.size(42.dp)); Text(text,color=Color.Gray) } }
private fun screenTitle(route:String,t:(String)->String):String = when(route) { "card_details"->t("details");"top_up"->t("topup");"qr_scanner"->t("qr");"history"->t("history");else->route.split('_').joinToString(" "){it.replaceFirstChar(Char::uppercase)} }

private fun android.content.Context.findFragmentActivity(): FragmentActivity? {
    var current: android.content.Context = this
    while (current is ContextWrapper) {
        if (current is FragmentActivity) return current
        current = current.baseContext
    }
    return current as? FragmentActivity
}
