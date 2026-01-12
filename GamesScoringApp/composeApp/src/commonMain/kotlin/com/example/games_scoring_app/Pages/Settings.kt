package com.example.games_scoring_app.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.IconButtonBar
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.SettingsButtonBar
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.cream
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


@Composable
fun SettingsPage(navController: NavController) {
    val appName = stringResource(id = R.string.app_name)
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)

    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    val themeMode by settingsViewModel.themeMode.collectAsState()

    settingsViewModel.getThemeMode()

    val backgroundColor = if (themeMode == 0) black else cream

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        WidgetTitle("SETTINGS", R.drawable.game_topview, navController);

        val themeModeColor = if(themeMode == 0) white else black
        val themeModeTitle = if(themeMode == 0) "Dark Mode" else "Light Mode"
        var themeModeTextColor = if(themeMode == 0) black else white
        var themeModeIcon = if(themeMode == 0) R.drawable.moon else R.drawable.sun_white
        Spacer(modifier = Modifier.height(60.dp))
        Column (Modifier.padding(horizontal = 30.dp )) {
            SettingsButtonBar(
                text = themeModeTitle,
                bgcolor = themeModeColor,
                height = 48.dp,
                textcolor = themeModeTextColor,
                onClick = { settingsViewModel.switchThemeMode() },
                icon = themeModeIcon,
                iconSize = 32.dp,
                doubleIcon = false
            )
        }

    }
}