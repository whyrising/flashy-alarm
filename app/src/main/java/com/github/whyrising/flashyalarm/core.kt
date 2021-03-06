package com.github.whyrising.flashyalarm

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.whyrising.flashyalarm.alarmlistener.Ids
import com.github.whyrising.flashyalarm.alarmlistener.Ids.checkDeviceFlashlight
import com.github.whyrising.flashyalarm.alarmlistener.Ids.isNotifAccessEnabled
import com.github.whyrising.flashyalarm.base.HostScreen
import com.github.whyrising.flashyalarm.base.Ids.exitApp
import com.github.whyrising.flashyalarm.base.Ids.initAppDb
import com.github.whyrising.flashyalarm.base.Ids.navigateFx
import com.github.whyrising.flashyalarm.base.appDb
import com.github.whyrising.flashyalarm.home.home
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.github.whyrising.flashyalarm.alarmlistener.init as initAlarmListener
import com.github.whyrising.flashyalarm.base.init as initBase
import com.github.whyrising.flashyalarm.home.homeRoute as home_route
import com.github.whyrising.flashyalarm.home.init as initHome

// -- Entry Point --------------------------------------------------------------

@Composable
fun NoFlashAlertDialog() {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = stringResource(R.string.alert_title_important))
        },
        text = {
            Text(
                text = stringResource(R.string.alert_msg_no_flashlight)
            )
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = {
                    dispatch(v(exitApp, true))
                },
            ) {
                Text(text = stringResource(R.string.alert_btn_exit))
            }
        }
    )
}

fun initAppDb() {
    regEventDb<Any>(initAppDb) { _, _ -> appDb }
    dispatchSync(v(initAppDb))
}

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initAppDb()
    }
}

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initAlarmListener(context = this)
        dispatch(v(checkDeviceFlashlight))
        initBase(this, MainActivity::class.java)
        initHome()

        setContent {
            HostScreen {
                if (!subscribe<Boolean>(v(Ids.isFlashAvailable)).w())
                    NoFlashAlertDialog()
                else {
                    val systemUiController = rememberSystemUiController()
                    val colors = MaterialTheme.colors
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = colors.primary,
                            darkIcons = true
                        )
                    }
                    val navCtrl = rememberAnimatedNavController()
                    LaunchedEffect(navCtrl) {
                        regFx(id = navigateFx) { route ->
                            if (route == null) return@regFx
                            navCtrl.navigate("$route")
                        }
                    }

                    AnimatedNavHost(
                        modifier = Modifier.padding(it),
                        navController = navCtrl,
                        startDestination = home_route
                    ) {
                        home(animOffSetX = 300)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dispatch(v(isNotifAccessEnabled))
    }
}
