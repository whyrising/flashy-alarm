package com.github.whyrising.flashyalarm.flashpattern

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.base.Ids
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable

const val patternsRoute = "flashlight_patterns"

@ExperimentalAnimationApi
fun NavGraphBuilder.flashPatterns(animOffSetX: Int) {
  composable(
    route = patternsRoute,
    exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
    popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
  ) {
    FlashlightPatterns()
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FlashlightPatterns() {
  SideEffect {
    dispatch(v(Ids.updateScreenTitle, "Flash Patterns"))
  }

  Surface {
    Column(modifier = Modifier.fillMaxSize()) {
      ListItem(
        overlineText = { Text(text = "Select") },
        trailing = {
          RadioButton(selected = true, onClick = { /*TODO*/ })
        }
      ) {
        Text(text = "Static")
      }
      ListItem(
        modifier = Modifier.clickable {
          // TODO:
        },
        secondaryText = {
          Text(text = "Tap to customize")
        },
        trailing = {
          RadioButton(selected = false, onClick = { /*TODO*/ })
        }
      ) {
        Text(text = "Blink")
      }
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  initAppDb()
  FlashyAlarmTheme {
    FlashlightPatterns()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
  FlashyAlarmTheme(darkTheme = true) {
    FlashlightPatterns()
  }
}
