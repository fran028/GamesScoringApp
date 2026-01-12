package com.example.games_scoring_app.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
// Import the utility composables you will be showing
import com.example.games_scoring_app.Utilities.CoinTosser
import com.example.games_scoring_app.Utilities.RollDice
import com.example.games_scoring_app.Utilities.Timer
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Composable
fun UtilitiesPage(navController: NavController, utilityId: Int) {
    val scrollState = rememberScrollState()
    // --- ViewModel and Theme Setup ---
    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))
    val themeMode by settingsViewModel.themeMode.collectAsState()

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    var title = "Utilities"
    var banner = R.drawable.game_topview

    when (utilityId) {
        1 -> {
            title = "Dice Roller"
            banner = R.drawable.dice_far
        }

        2 -> {
            title = "Coin Tosser"
            banner = R.drawable.coins_banner
        }
        3 -> {
            title = "Timer"
            banner = R.drawable.clocks_banner
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        WidgetTitle(title.toUpperCase(), banner, navController);
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- Conditional Rendering based on utilityId ---
            when (utilityId) {
                1 -> {
                    RollDice()
                }

                2 -> {
                    CoinTosser()
                }
                3 -> {
                    Timer()
                }
            }
        }
    }

}