package com.clean.architecture.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clean.architecture.presentation.editNotes.screen.AddEditNoteScreen
import com.clean.architecture.presentation.editNotes.screen.AddEditNoteViewModel
import com.clean.architecture.presentation.notes.screen.NotesScreen
import com.clean.architecture.presentation.screen.Screen
import com.clean.architecture.ui.theme.ArchitectureTemplatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArchitectureTemplatesTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }

    }

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.NotesScreen.route
        ) {

            composable(route = Screen.NotesScreen.route) {
                NotesScreen {
                    navController.navigate(it)
                }
            }

            composable(
                route = Screen.AddEditNoteScreen.route +
                        "?noteId={noteId}&noteColor={noteColor}",
                arguments = listOf(
                    navArgument(
                        name = "noteId"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(
                        name = "noteColor"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                )
            ) {
                val color = it.arguments?.getInt("noteColor") ?: -1
                val viewModel = hiltViewModel<AddEditNoteViewModel>()
                AddEditNoteScreen(
                    addEditNoteContract = viewModel,
                    noteColor = color,
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }

        }
    }
}