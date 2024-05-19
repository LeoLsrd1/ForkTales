package com.ltu.m7019e.forktales.test

import androidx.navigation.NavController
import junit.framework.TestCase.assertEquals


fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}
