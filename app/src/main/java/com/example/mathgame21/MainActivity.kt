package com.example.mathgame21
// no saving yet
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.mathgame21.databinding.ActivityMainBinding
import com.example.mathgame21.databinding.ThemeSettingsDialogBinding
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var myTheme = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val buttonClickSoundMP: MediaPlayer = MediaPlayer.create(this, R.raw.button_click_sound_short_1)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.settingsIV.setOnClickListener {
            buttonClickSoundMP.start()

            val ThemeDialogView = LayoutInflater.from(this).inflate(R.layout.theme_settings_dialog, null)


            var binding_theme_layout : ThemeSettingsDialogBinding = ThemeSettingsDialogBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(this)
                .setView(ThemeDialogView)
                .setTitle("Select theme")
                .setPositiveButton("Ok") { dialog, which ->
                    if (myTheme == 0) {
                        sysDefTheme()
                    }else if (myTheme == 1) {
                        lightTheme()
                    }else {
                        darkTheme()
                    }

                }


            val AlertDialog = builder.show()





        }
        binding.xxxButonXXX.setOnClickListener {
            buttonClickSoundMP.start()
            startActivity(Intent(this, chooseLevel::class.java))
        }
        binding.tutorialButton.setOnClickListener{
            buttonClickSoundMP.start()
            startActivity(Intent(this, TutorialActivity::class.java))
        }






    }
    fun darkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    fun lightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
    fun sysDefTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }





    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {


            setTitle("Settings")

            setNegativeButton("Close" ) {dialog, which->

            }

            setPositiveButton( "Ok") {dialog, which->

            }


            show()
        }
    }

    fun sysDefTheme(view: View) {myTheme=0}
    fun lightTheme(view: View) {myTheme=1}
    fun darkTheme(view: View) {myTheme=2}


}