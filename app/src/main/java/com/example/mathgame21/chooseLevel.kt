package com.example.mathgame21

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mathgame21.databinding.ActivityChooseLevel2Binding
import java.io.*

class chooseLevel : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLevel2Binding
    private val buttonList = arrayListOf<Button>()
    var crownNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLevel2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this@chooseLevel, puzzle1::class.java)

        val buttonClickSoundMP: MediaPlayer =
            MediaPlayer.create(this, R.raw.button_click_sound_short_1)

        if (fileExist("crownSave.txt")) {
            crownNumber = readFromFile("crownSave.txt")
            if (crownNumber != null) {
                binding.crownCounterTV.text = crownNumber.toString()
                writeToFile(crownNumber.toString(), "crownSave.txt")
            }
        }

        buttonList.addAll(listOf(binding.level1Button, binding.level2Button,
            binding.level3Button, binding.level4Button, binding.level5Button, binding.level6Button))

        // disabling the buttons if there's not enough crowns
        for (x in 1 until buttonList.size) {

            buttonList[x].setOnClickListener {
                if (!buttonList[x].isActivated) {
                    Toast.makeText(applicationContext, "You need ${x*10-5} crowns to unlock", Toast.LENGTH_LONG).show()
                } else {
                    intent.putExtra("filename", "level${x+1}.json")
                    intent.putExtra("levelNumber", x+1)
                    buttonClickSoundMP.start()
                    startActivity(intent)

                }
            }

                if (crownNumber != null) {
                    if (crownNumber!!.toInt() < x*10-5) {



                        buttonList[x].isActivated = false
                        buttonList[x].setBackgroundColor(
                            ContextCompat.getColor(
                                buttonList[x].context,
                                R.color.disabled_background_color
                            )
                                )
                        buttonList[x].setTextColor(
                            ContextCompat.getColor(
                                buttonList[x].context,
                                R.color.disabled_text_color
                                 )
                             )
                } else {
                        buttonList[x].isActivated = true
                        buttonList[x].setBackgroundColor(
                            ContextCompat.getColor(
                                buttonList[x].context,
                                R.color.purple_500
                            )
                        )
                        buttonList[x].setTextColor(
                            ContextCompat.getColor(
                                buttonList[x].context,
                                R.color.white
                            )
                        )

                    }
            }

        }

        binding.level1Button.setOnClickListener {
            intent.putExtra("filename", "level1.json")
            intent.putExtra("levelNumber", 1)
            buttonClickSoundMP.start()
            startActivity(intent)
        }

/*
        binding.level2Button.setOnClickListener {
            if (!binding.level2Button.isActivated) {
                Toast.makeText(applicationContext, "You need 5 crowns to unlock", Toast.LENGTH_LONG)
                    .show()
            } else {
                intent.putExtra("filename", "level2.json")
                intent.putExtra("levelNumber", 2)
                buttonClickSoundMP.start()
                startActivity(intent)
            }
        }




        binding.level3Button.setOnClickListener {
            intent.putExtra("filename", "level3.json")
            intent.putExtra("levelNumber", 3)
            buttonClickSoundMP.start()
            startActivity(intent)
        }
        binding.level4Button.setOnClickListener {
            intent.putExtra("filename", "level4.json")
            intent.putExtra("levelNumber", 4)
            buttonClickSoundMP.start()
            startActivity(intent)

        }

        binding.level5Button.setOnClickListener {
            intent.putExtra("filename", "level5.json")
            intent.putExtra("levelNumber", 5)
            buttonClickSoundMP.start()
            startActivity(intent)

        }
        binding.level6Button.setOnClickListener {
            intent.putExtra("filename", "level6.json")
            intent.putExtra("levelNumber", 6)
            buttonClickSoundMP.start()
            startActivity(intent)

        }*/



    }

    override fun onRestart() {
// TODO Auto-generated method stub
        super.onRestart()
        if (fileExist("crownSave.txt")) {
            println("exists")
            var crownNumber = readFromFile("crownSave.txt")
            if (crownNumber != null) {
                binding.crownCounterTV.text = crownNumber.toString()
                writeToFile(crownNumber.toString(), "crownSave.txt")


                for (x in 1 until buttonList.size) {
                    if (crownNumber != null) {
                        if (crownNumber!!.toInt() >= x * 10 - 5) {
                            buttonList[x].isActivated = true
                            buttonList[x].setBackgroundColor(
                                ContextCompat.getColor(
                                    buttonList[x].context,
                                    R.color.purple_500
                                )
                            )
                            buttonList[x].setTextColor(
                                ContextCompat.getColor(
                                    buttonList[x].context,
                                    R.color.white
                                )
                            )

                        }

                    }
                }


            }
        }
    }



    fun fileExist(fname: String?): Boolean {
        val file = baseContext.getFileStreamPath(fname)
        return file.exists()
    }

    private fun writeToFile(data: String, filename: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    private fun readFromFile(filename: String): String? {
        var ret = ""
        try {
            val inputStream: InputStream = openFileInput(filename)
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = java.lang.StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: $e")
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: $e")
        }
        return ret
    }
}