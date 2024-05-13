package com.ltu.m7019e.forktales

import android.app.Application
import com.ltu.m7019e.forktales.database.AppContainer
import com.ltu.m7019e.forktales.database.DefaultAppContainer

/**
 * This is the main Application class for the ForkTales application.
 * It initializes the AppContainer which provides access to the repositories used in the application.
 */
class ForkTalesApplication : Application() {
    /**
     * The AppContainer for the application. It provides access to the repositories used in the application.
     */
    lateinit var container: AppContainer

    /**
     * This function is called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     * It initializes the AppContainer.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}