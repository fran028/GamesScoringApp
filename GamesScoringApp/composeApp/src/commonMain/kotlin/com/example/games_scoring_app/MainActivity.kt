 package com.example.games_scoring_app

 import androidx.activity.compose.setContent
 import androidx.activity.enableEdgeToEdge

 class MainActivity : androidx.activity.ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()

         // Initialize your database here (Android side needs context)
         AppDatabase.getDatabase(applicationContext, lifecycleScope)

         setContent {
             App() // Simply calls the shared App() from commonMain
         }
     }
 }
