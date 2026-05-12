package com.muthia0027.mobpro1.navigation

const val KEY_ID_FINANCIAL = "idFinancial"

sealed class Screen(val route: String ) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_FINANCIAL}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}