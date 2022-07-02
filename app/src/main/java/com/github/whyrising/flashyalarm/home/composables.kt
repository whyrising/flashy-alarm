package com.github.whyrising.flashyalarm.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmservice.Ids
import com.github.whyrising.flashyalarm.alarmservice.Ids.isFlashServiceRunning
import com.github.whyrising.flashyalarm.alarmservice.regSubs
import com.github.whyrising.flashyalarm.base.Ids.navigate
import com.github.whyrising.flashyalarm.base.Ids.updateScreenTitle
import com.github.whyrising.flashyalarm.flashpattern.patternsRoute
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.ui.theme.SwitchStyled
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.core.v
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
  dispatch(v(updateScreenTitle, stringResource(R.string.home_screen_title)))

//  if (subscribe<Boolean>(v(isDisableServiceDialogVisible)).w())
//    DisableServiceAlertDialog()

  Surface {
    Column(modifier = Modifier.fillMaxSize()) {
      dispatch(v(isFlashServiceRunning))

      ListItem(
        secondaryText = {
          Text(text = stringResource(R.string.flashlight_service_switch_desc))
        },
        overlineText = { Text(text = "Service") },
        trailing = {
          SwitchStyled(
            checked = subscribe<Boolean>(v(isFlashServiceRunning)).w(),
            onCheckedChange = {
              dispatch(v(Ids.toggleFlashyAlarmService, it))
            }
          )
        }
      ) {
        Text(text = "Flashlight Service")
      }

      ListItem(
        modifier = Modifier.clickable {
          dispatch(v(navigate, patternsRoute))
        },
        secondaryText = { Text(stringResource(R.string.flash_pattern_desc)) },
        overlineText = { Text(text = "Configuration") }
      ) {
        Text(text = "Flashlight Pattern")
      }
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  initAppDb()
  initHome(LocalContext.current)
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
