import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {
    init {
        LocaleUtils.updateConfig(this)
    }
}