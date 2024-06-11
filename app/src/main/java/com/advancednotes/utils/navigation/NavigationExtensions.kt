package com.advancednotes.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.myNavigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    afterNavigate: (() -> Unit)? = null
) {
    afterNavigate?.invoke()
    this.navigate(route, navOptions, navigatorExtras)
}