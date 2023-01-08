package com.peter.azure.ui.navigation

import androidx.navigation.navArgument
import com.peter.azure.R
import com.peter.azure.data.entity.GameLevel
import com.peter.azure.data.entity.Info
import com.peter.azure.data.util.GAME_EXISTED_SAVED_KEY
import com.peter.azure.data.util.GAME_LEVEL_SAVED_KEY
import com.peter.azure.data.util.INFO_TYPE_SAVED_KEY

sealed interface AzureDestination {

    val route: String
    val textId: Int

    sealed interface Main: AzureDestination {

        val imageId: Int
        val imageContentDescriptionId: Int
        val descriptionId: Int

        object HOME: Main {
            override val route = "home"
            override val imageId = R.drawable.ic_home_nav_24
            override val imageContentDescriptionId = R.string.screen_home_img_cd
            override val textId = R.string.screen_home
            override val descriptionId = R.string.screen_home_description

            fun getNavGameRoute(
                gameExisted: Boolean, gameLevel: GameLevel? = null
            ): String {
                var route = "${General.GAME.route}/$gameExisted" +
                        "?$GAME_LEVEL_SAVED_KEY=null"
                gameLevel?.let {
                    route = route.replace("null", it.name)
                }
                return route
            }
        }

        object PRINT: Main {
            override val route = "print"
            override val imageId = R.drawable.ic_print_nav_24
            override val imageContentDescriptionId = R.string.screen_print_img_cd
            override val textId = R.string.screen_print
            override val descriptionId = R.string.screen_print_description
        }

        object HELP: Main {
            override val route = "help"
            override val imageId = R.drawable.ic_help_nav_24
            override val imageContentDescriptionId = R.string.screen_help_img_cd
            override val textId = R.string.screen_help
            override val descriptionId = R.string.screen_help_description
        }

        object ABOUT: Main {
            override val route = "about"
            override val imageId = R.drawable.ic_about_nav_24
            override val imageContentDescriptionId = R.string.screen_about_img_cd
            override val textId = R.string.screen_about
            override val descriptionId = R.string.screen_about_description

            fun getNavContractRoute(infoType: Info.Type): String {
                return "${General.CONTRACT.route}/${infoType.name}"
            }
        }

        companion object {
            private val topList = listOf(HOME, PRINT, HELP, ABOUT)

            fun getNavigationList(currentMainDestination: Main): List<Main> {
                return topList.filter { it != currentMainDestination }
            }
        }

    }

    sealed interface General: AzureDestination {

        object GAME: General {
            override val route = "game"
            override val textId = R.string.screen_game

            val destRoute = "$route/{$GAME_EXISTED_SAVED_KEY}" +
                    "?$GAME_LEVEL_SAVED_KEY={$GAME_LEVEL_SAVED_KEY}"
            val navArguments = listOf(
                navArgument(GAME_LEVEL_SAVED_KEY) {
                    nullable = true
                }
            )
        }

        object CONTRACT: General {
            override val route = "contract"
            override val textId = R.string.screen_contract

            const val destRoute = "contract/{$INFO_TYPE_SAVED_KEY}"
        }

        object GREETING: General {
            override val route = "greeting"
            override val textId = R.string.screen_greeting
        }

    }

}
