package com.tap.tapper.favv

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.onesignal.OneSignal
import me.jingbin.web.ByWebTools
import me.jingbin.web.ByWebView
import me.jingbin.web.OnByWebClientCallback
import me.jingbin.web.OnTitleProgressCallback

class ValidActivity : AppCompatActivity() {
    private lateinit var byWebView: ByWebView

    private val ONESIGNAL = "7b7dd83c-6558-46c2-b27d-17383a8934a2"
    private val PREFS = "mnfdhu"
    private val LINK = "path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL)

        val container = LinearLayout(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        setContentView(container, params)

        val onTitleProgressCallback: OnTitleProgressCallback = object : OnTitleProgressCallback() {
            override fun onReceivedTitle(title: String) {}

            override fun onProgressChanged(newProgress: Int) {}
        }

        val onByWebClientCallback: OnByWebClientCallback = object : OnByWebClientCallback() {
            override fun onPageFinished(view: WebView, url: String) {
            }

            override fun isOpenThirdApp(url: String): Boolean {
                return ByWebTools.handleThirdApp(this@ValidActivity, url)
            }
        }

        byWebView = ByWebView
            .with(this)
            .setWebParent(container, LinearLayout.LayoutParams(-1, -1))
            .useWebProgress(ContextCompat.getColor(this, R.color.com_facebook_blue))
            .setOnTitleProgressCallback(onTitleProgressCallback)
            .setOnByWebClientCallback(onByWebClientCallback)
            .loadUrl(getSharedPreferences(PREFS, 0).getString(LINK, ""))
    }

    override fun onPause() {
        super.onPause()
        byWebView.onPause()
    }

    override fun onResume() {
        super.onResume()
        byWebView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        byWebView.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (byWebView.handleKeyEvent(keyCode, event)) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        byWebView.handleFileChooser(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ), 10
        )
        intent?.putExtra(Intent.EXTRA_TITLE, "Upload file")
        super.startActivityForResult(intent, requestCode)
    }
}