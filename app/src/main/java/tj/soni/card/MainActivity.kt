package tj.soni.card

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import tj.soni.card.ui.SoniCardApp
import tj.soni.card.ui.theme.SoniTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SoniTheme { SoniCardApp() } }
    }
}
