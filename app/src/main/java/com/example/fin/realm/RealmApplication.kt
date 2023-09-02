package com.example.fin.realm

import android.app.Application
import com.google.firebase.FirebaseApp
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * `RealmApplication` is a custom `Application` class responsible for initializing the Realm database and Firebase.
 */
class RealmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase.
        FirebaseApp.initializeApp(this@RealmApplication)

        // Initialize the Realm database.
        Realm.init(this)

        // Create a Realm configuration specifying the database name and set it as the default configuration.
        val realmConfiguration = RealmConfiguration.Builder()
            .name("RealmDataBase") // Specify the name of the database file.
            .build()

        Realm.setDefaultConfiguration(realmConfiguration)
    }
}
