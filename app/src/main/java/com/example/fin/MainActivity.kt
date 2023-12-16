package com.example.fin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fin.customAdapter.RealmAdapter
import com.example.fin.login.LoginActivity
import com.example.fin.realm.RealmHelperClass
import com.example.fin.realm.RealmModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmChangeListener


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var realmAdapter: RealmAdapter
    private lateinit var realmHelper: RealmHelperClass
    private lateinit var realm: Realm
    private lateinit var updateButton: Button
    private lateinit var name: TextInputEditText
    private lateinit var age: TextInputEditText
    private lateinit var city: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initValues()

        // Set an onClickListener for the "Update" button to save data to the Realm database.
        updateButton.setOnClickListener {
            saveData()
        }

        // Initialize the Realm adapter, retrieve data, and set up the RecyclerView.
        realmHelper.selectFromDB()
        realmAdapter = RealmAdapter(this@MainActivity, realmHelper.update())
        recyclerView.adapter = realmAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu resource for the sorting options and sign-out menu.
        menuInflater.inflate(R.menu.sorting_options, menu)
        menuInflater.inflate(R.menu.sign_out_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortByName -> {
                // Sort data by name and update the RecyclerView.
                realmHelper.sortByName()
                realmAdapter = RealmAdapter(this@MainActivity, realmHelper.update())
                recyclerView.adapter = realmAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                update()
                return true
            }

            R.id.sortByAge -> {
                // Sort data by age and update the RecyclerView.
                realmHelper.sortByAge()
                realmAdapter = RealmAdapter(this@MainActivity, realmHelper.update())
                recyclerView.adapter = realmAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                update()
                return false
            }

            R.id.sortByCity -> {
                // Sort data by city and update the RecyclerView.
                realmHelper.sortByCity()
                realmAdapter = RealmAdapter(this@MainActivity, realmHelper.update())
                recyclerView.adapter = realmAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                update()
                return true
            }

            R.id.sign_out -> {
                // Sign out the user and navigate to the LoginActivity.
                FirebaseAuth.getInstance().signOut()
                val loginActivityIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginActivityIntent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Method to update the recycler view when data changes. */
    private fun update() {
        // Set up a RealmChangeListener to automatically update the RecyclerView when data changes.
        val realmChangeListener = RealmChangeListener<Realm> {
            val updatedRealmAdapter = RealmAdapter(this@MainActivity, realmHelper.update())
            recyclerView.adapter = updatedRealmAdapter
        }
        realm.addChangeListener(realmChangeListener)
    }

    /** Save data to the Realm database asynchronously. */
    private fun saveData() {
        realm.executeTransactionAsync({
            var maxId = it.where(RealmModel::class.java).max("id")
            val newKey = maxId?.let { maxId.toInt() + 1 } ?: 1
            val realModel = it.createObject(RealmModel::class.java, newKey)
            realModel.name = name.text.toString()
            realModel.age = age.text.toString().toInt()
            realModel.city = city.text.toString()
            name.text?.clear()
            age.text?.clear()
            city.text?.clear()
        }, {
            // Show a success message when data is saved successfully.
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG).show()
        }, {
            // Handle errors during data saving and show an error message.
            Toast.makeText(this, "Error in Saving : ${it.message}", Toast.LENGTH_LONG).show()
            Log.e("mybug", "saveData: ${it.message}")
        })
    }

    private fun initValues() {
        // Initialize variables and UI elements.
        realm = Realm.getDefaultInstance()
        realmHelper = RealmHelperClass(realm = realm)
        recyclerView = findViewById(R.id.recycler_view)
        updateButton = findViewById(R.id.update_button)
        name = findViewById(R.id.name)
        age = findViewById(R.id.age)
        city = findViewById(R.id.city)
    }
}
