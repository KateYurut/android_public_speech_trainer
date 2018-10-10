package com.example.company.myapplication

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_start_page.*

const val debugSlides = ""
const val PageCount = 26
// this will be changed once the package name is changed
const val SHARED_PREFERENCES_FILE_NAME = "com.example.company.myapplication.prefs"

class StartPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        val sPref = getPreferences(Context.MODE_PRIVATE)
        val debSl = sPref.edit()
        debSl.putString(getString(R.string.DEBUG_SLIDES), debugSlides)
        debSl.apply()

        val sharedPref = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            if (sharedPref.contains(getString(R.string.audio_recording))) {
                return@with
            }
            putBoolean(getString(R.string.audio_recording), true)
            apply()
        }

        pres1.setOnClickListener{
            val intent = Intent(this, PresentationActivity::class.java)
            startActivity(intent)
        }

        pres2.setOnClickListener{
            val intent = Intent(this, PresentationActivity::class.java)
            startActivity(intent)
        }

        addBtn.setOnClickListener{
            val intent = Intent(this, CreatePresentationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.action_settings -> {
                val intent = Intent(this, PreferenceActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
