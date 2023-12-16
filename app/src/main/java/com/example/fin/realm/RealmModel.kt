package com.example.fin.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * `RealmModel` is a RealmObject class that represents a model for storing data in a Realm database.
 *
 * @param id    The primary key of the model. Defaults to 0 if not provided explicitly.
 * @param name  The name associated with the model. Defaults to an empty string if not provided explicitly.
 * @param age   The age associated with the model. Defaults to 0 if not provided explicitly.
 * @param city  The city associated with the model. Defaults to an empty string if not provided explicitly.
 */
open class RealmModel(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var age: Int = 0,
    var city: String = ""
) : RealmObject()
