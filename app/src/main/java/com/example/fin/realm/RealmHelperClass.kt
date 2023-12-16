package com.example.fin.realm

import io.realm.Realm
import io.realm.RealmResults

/**
 * RealmHelperClass is a utility class for performing database operations using Realm in Android.
 * It provides methods to select, update, and sort data in a Realm database.
 *
 * @param realm An instance of the Realm database.
 */
class RealmHelperClass(private val realm: Realm) {

    private lateinit var realmResult: RealmResults<RealmModel>

    /**
     * Select all records from the RealmModel table and store the results in the realmResult field.
     */
    fun selectFromDB() {
        realmResult = realm.where(RealmModel::class.java).findAll()
    }

    /**
     * Retrieve and return a list of RealmModel objects from the realmResult.
     *
     * @return A list of RealmModel objects.
     */
    fun update(): List<RealmModel> {
        val realmList = mutableListOf<RealmModel>()
        for (realmObject in realmResult) {
            realmList.add(realmObject)
        }
        return realmList
    }

    /**
     * Sort the records in the realmResult by the 'name' field in ascending order.
     */
    fun sortByName() {
        realmResult = realm.where(RealmModel::class.java).sort("name").findAll()
    }

    /**
     * Sort the records in the realmResult by the 'age' field in ascending order.
     */
    fun sortByAge() {
        realmResult = realm.where(RealmModel::class.java).sort("age").findAll()
    }

    /**
     * Sort the records in the realmResult by the 'city' field in ascending order.
     */
    fun sortByCity() {
        realmResult = realm.where(RealmModel::class.java).sort("city").findAll()
    }
}
