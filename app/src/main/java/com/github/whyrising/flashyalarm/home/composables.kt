package com.github.whyrising.flashyalarm.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmlistener.Ids
import com.github.whyrising.flashyalarm.alarmlistener.Ids.isNotifAccessEnabled
import com.github.whyrising.flashyalarm.alarmlistener.regSubs
import com.github.whyrising.flashyalarm.base.Ids.updateScreenTitle
import com.github.whyrising.flashyalarm.home.Ids.isDisableServiceDialogVisible
import com.github.whyrising.flashyalarm.home.Ids.showDisableServiceDialog
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v
import com.google.accompanist.navigation.animation.composable
import com.github.whyrising.flashyalarm.home.init as initHome

const val homeRoute = "/home"

@ExperimentalAnimationApi
fun NavGraphBuilder.home(animOffSetX: Int) {
    composable(
        route = homeRoute,
        exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
        popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
    ) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    dispatch(v(updateScreenTitle, stringResource(R.string.home_screen_title)))

    if (subscribe<Boolean>(v(isDisableServiceDialogVisible)).w())
        DisableServiceAlertDialog()

    val colors = MaterialTheme.colors
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dispatch(v(isNotifAccessEnabled))
            Text(
                text = stringResource(R.string.service_switch_label),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = subscribe<Boolean>(v(isNotifAccessEnabled)).w(),
                onCheckedChange = {
                    when {
                        it -> dispatch(v(Ids.enableNotificationAccess))
                        else -> dispatch(v(showDisableServiceDialog))
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.primary,
                    checkedTrackColor = colors.primary,
                )
            )
        }
    }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    initAppDb()
    initHome()
    regSubs()

    FlashyAlarmTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
    FlashyAlarmTheme(darkTheme = true) {
        HomeScreen()
    }
}
