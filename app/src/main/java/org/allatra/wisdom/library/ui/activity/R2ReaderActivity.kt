package org.allatra.wisdom.library.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_r2_epub.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.allatra.wisdom.library.R
import org.readium.r2.navigator.R2EpubActivity
import org.readium.r2.shared.APPEARANCE_REF
import org.readium.r2.streamer.parser.EpubParser
import kotlin.coroutines.CoroutineContext

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class R2ReaderActivity : R2EpubActivity(), CoroutineScope {

    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var bookId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {


        val publicationPath = intent.getStringExtra("publicationPath")
        bookId = intent.getLongExtra("bookId", -1)

        val parser = EpubParser()
        val pub = parser.parse(publicationPath)
        intent.putExtra("publication", pub!!.publication)

        super.onCreate(savedInstanceState)

        val appearancePref = preferences.getInt(APPEARANCE_REF, 0)
        val backgroundsColors = mutableListOf("#ffffff", "#faf4e8", "#000000")
        val textColors = mutableListOf("#000000", "#000000", "#ffffff")
        resourcePager.setBackgroundColor(Color.parseColor(backgroundsColors[appearancePref]))
        (resourcePager.focusedChild?.findViewById(org.readium.r2.navigator.R.id.book_title) as? TextView)?.setTextColor(
            Color.parseColor(textColors[appearancePref]))

        titleView.text = publication.metadata.title

        //setContentView(R.layout.activity_r2_epub)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up the user interaction to manually show or hide the system UI.
        //fullscreen_content.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //dummy_button.setOnTouchListener(mDelayHideTouchListener)
    }
}
