package com.example.mathgame21

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mathgame21.databinding.ActivityPuzzle1Binding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.util.*
import kotlin.math.*


// todo implement saving
// todo adding sounds for buttons
// todo create animation and point system to unlock new levels
// todo adding well done something for successful submition

// todo create a better looking ui
// todo cerate more languages and a language selector
// bug textview text is selectable with doubletap
// make ads appear in dark theme aswell

// coins and a little customizeable character maybe
// return button doesn't work
class puzzle1 : AppCompatActivity() {


    var levelnumber : Int? = null

    //ads
    private var mRewardedAd: RewardedAd? = null
    private var TAG = "puzzle1"
    private lateinit var adRequest : AdRequest

    //sound for success
    lateinit var successSoundMP: MediaPlayer

    private lateinit var currentET : EditText
    val ETlist = arrayListOf<EditText>()
    val crownList = arrayListOf<ImageView>()
    lateinit var xxx3puzzles: List<xxx3dataclass>
    private lateinit var binding : ActivityPuzzle1Binding
    override fun onCreate(savedInstanceState: Bundle?) {




        //click sound for button clicks
        val buttonClickSoundMP: MediaPlayer = MediaPlayer.create(this, R.raw.button_click_sound_short_1)
        successSoundMP = MediaPlayer.create(this, R.raw.success_1)



        super.onCreate(savedInstanceState)
        binding = ActivityPuzzle1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        crownList.addAll(listOf(binding.crownIV0, binding.crownIV1, binding.crownIV2,
            binding.crownIV3, binding.crownIV4, binding.crownIV5, binding.crownIV6,
            binding.crownIV7, binding.crownIV8, binding.crownIV9))

        ETlist.addAll(listOf(binding.equationET0, binding.equationET1, binding.equationET2,
            binding.equationET3, binding.equationET4, binding.equationET5, binding.equationET6,
            binding.equationET7, binding.equationET8, binding.equationET9))
        val filename = intent.getStringExtra("filename")

        levelnumber = intent.getIntExtra("levelNumber", 1)
        println(levelnumber)


        val jsonFileString = filename?.let { getJsonDataFromAsset(applicationContext, it) }
        if (jsonFileString != null) {
            Log.i("data", jsonFileString)
        }

        val gson = Gson()
        val listxxx3Type = object : TypeToken<List<xxx3dataclass>>() {}.type
        xxx3puzzles = gson.fromJson(jsonFileString, listxxx3Type)

        adRequest = AdRequest.Builder().build()

        RewardedAd.load(this,"ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })

        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.")
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.")
                loadAd()
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.")
                loadAd()
                mRewardedAd = null
            }
        }



        binding.levelNumberTV.text = "Level $levelnumber"


        for (x in 0..9) {
            ETlist[x].setOnFocusChangeListener{ view, b ->
                if (b) {
                    SetCurrentET(ETlist[x])
                }
            }
            ETlist[x].showSoftInputOnFocus = false
            ETlist[x].isLongClickable = false
            ETlist[x].setTextIsSelectable(false)
            ETlist[x].setText("""${xxx3puzzles[x].puzzle}=${xxx3puzzles[x].equalsTo}""")
            if (fileExist("saveLevel${levelnumber}.txt")) {
                println(levelnumber)
                val levelsCompleted = readFromFile("saveLevel${levelnumber}.txt")
                if (levelsCompleted != null) {
                    if (levelsCompleted.contains(x.toString())) {
                        crownList[x].visibility = View.VISIBLE
                    }
                }
            }
        }
        binding.CButton.setOnClickListener {
            buttonClickSoundMP.start()
            delete() }
        binding.ACButton.setOnClickListener {
            buttonClickSoundMP.start()
            clearAll() }
        binding.SubmitButton.setOnClickListener {
            buttonClickSoundMP.start()
            checkIfValid() }
        binding.rightArrowButton.setOnClickListener {
            buttonClickSoundMP.start()
            movePosition(false) }
        binding.leftArrowButton.setOnClickListener {
            buttonClickSoundMP.start()
            movePosition(true) }
        binding.dotButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('.') }
        binding.plusButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('+') }
        binding.minusButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('-') }
        binding.exponentialButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('^') }
        binding.mulitplicationButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('*') }
        binding.divisonButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('/') }
        binding.sqrtButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('√') }
        binding.openParenthesisButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('(') }
        binding.closeParenthesisButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols(')') }
        binding.factorialButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('!') }
        binding.openFloorButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols ('⌊') }
        binding.closeFloorButton.setOnClickListener {
            buttonClickSoundMP.start()
            writeSymbols('⌋') }



        binding.hintButton.setOnClickListener {
            buttonClickSoundMP.start()
            if (this::currentET.isInitialized) {
                showWantAdDialog(false)
            }
            else {
                Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
                return@setOnClickListener}
        }
        binding.solutionButtion.setOnClickListener {
            buttonClickSoundMP.start()

            try
            { if (xxx3puzzles[ETlist.indexOf(currentET)].isAvailable) {
                loadAd()
                showWantAdDialog(true)
            }
            else{
                Toast.makeText(applicationContext, "use the hint first", Toast.LENGTH_SHORT).show()
            }
            }
            catch (error: UninitializedPropertyAccessException) {
                Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()}
        }


        //setting crown numbers from save file
        if (fileExist("crownSave.txt")) {
            var crownNumber = readFromFile("crownSave.txt")
            if (crownNumber != null) {
                binding.crownCounterTV.text = crownNumber.toString()
                writeToFile(crownNumber.toString(), "crownSave.txt")
            }
        }
    }
    private fun showWantAdDialog(isSolution: Boolean) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            if (isSolution) {setTitle("Watch an Ad to get the Solution")}
            else{setTitle("Watch an Ad to get a Hint")}

            //val inflater = layoutInflater
            //val dialogLayout = inflater.inflate(R.)
            setPositiveButton("Watch Ad") {dialog, which->


                if (mRewardedAd != null) {
                    mRewardedAd?.show(this@puzzle1
                    ) {
                        if (isSolution) {
                            showTextDialog(true)
                        } else {
                            showTextDialog(false)
                            xxx3puzzles[ETlist.indexOf(currentET)].isAvailable = true
                        }

                        Log.d(TAG, "User earned the reward.")
                        loadAd()
                    }
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.")

                }


                Toast.makeText(applicationContext,"Ad is being shown", Toast.LENGTH_SHORT).show()
            }


            setNegativeButton("Close") {dialog, which->
                Log.d("puzzle1", "Negative button clicked")
            }
            show()
        }

    }
    private fun showTextDialog(isSolution: Boolean) {
        val builder = AlertDialog.Builder(this)
        //val inflater = layoutInflater
        //val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        //only need this if we want to customize how it looks, but then idk how to set the text
        //todo search for android guides dialogs
        with(builder) {
            if (isSolution){
                setTitle("Here's the Solution:")

                setMessage(xxx3puzzles[ETlist.indexOf(currentET)].solution)

                setPositiveButton("Enter solution") { dialog, which ->
                    currentET.setText(xxx3puzzles[ETlist.indexOf(currentET)].solution)
                }
                setNegativeButton("close") {dialog, which ->
                    Log.d("puzzle1", "Negative button clicked")
                }
            }
            else{
                try {
                    setMessage(xxx3puzzles[ETlist.indexOf(currentET)].hint)
                }
                catch (error: UninitializedPropertyAccessException) {
                    Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
                    return@with
                }


            }

            show()
        }


    }
    private fun loadAd() {
        RewardedAd.load(this,"ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })

    }

    private fun SetCurrentET(current: EditText) {
        currentET = current
    }

    private fun writeSymbols(symbol: Char) {
        try {
            if (currentET.selectionStart < (currentET.text.length-1)) {
                currentET.text.insert(currentET.selectionStart, symbol.toString())
            }
            else {
                Toast.makeText(applicationContext, "You can't write here", Toast.LENGTH_SHORT).show()
            }
        }
        catch (error: UninitializedPropertyAccessException) {
            Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SetTextI18n")
    //todo cursor is a little glitchy when deleting text, jumps to the end or beginning

    fun delete() {
        //checks that we're not trying to delete the -1th character (crash) or a part of the original text
        try {
            if ((currentET.selectionStart == 0) or (currentET.selectionStart == (currentET.text.length - 1)) or (currentET.text.toString()[currentET.selectionStart - 1].isDigit())) {
                return
            }
            val remember = currentET.selectionStart
            currentET.setText(
                currentET.text.substring(
                    0,
                    currentET.selectionStart - 1
                ) + currentET.text.substring(currentET.selectionStart, currentET.text.length)
            )
            currentET.setSelection(remember - 1)
        }

        catch (error: UninitializedPropertyAccessException) {
            Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
        }
        catch (error:java.lang.StringIndexOutOfBoundsException) {
            return
        }

    }
    private fun clearAll() {
        try {
            ETlist.indexOf(currentET)
            currentET.setText(xxx3puzzles[ETlist.indexOf(currentET)].puzzle+"="+xxx3puzzles[ETlist.indexOf(currentET)].equalsTo)
        }
        catch (error: UninitializedPropertyAccessException) {
            Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
        }    }

    private fun movePosition(DirectionIsLeft: Boolean){
        try {
            if (DirectionIsLeft and (currentET.selectionStart != 0)) {
                currentET.setSelection(currentET.selectionStart-1)
            }
            else if(!(DirectionIsLeft) and (currentET.selectionStart < (currentET.text.length-2))){
                currentET.setSelection(currentET.selectionStart+1)
            }
        }
        catch (error: UninitializedPropertyAccessException) {
            Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
        }

    }
    private fun checkIfValid() {
        try {
            val equation = currentET.text
            try{


                val list = equation.split("=")
                var leftSide = list[0]
                val rightSide = list[1]
                var count = 0
                for (char in leftSide) {
                    if (char == xxx3puzzles[ETlist.indexOf(currentET)].puzzle[0]) {
                        count += 1
                    }
                }


                if (rightSide == (xxx3puzzles[ETlist.indexOf(currentET)].equalsTo) && count == xxx3puzzles[ETlist.indexOf(currentET)].puzzle.length) {

                    leftSide = leftSide.replace("√√√","tsqrt")
                    leftSide = leftSide.replace("√√","dsqrt")
                    leftSide = leftSide.replace("!!", "?")
                    leftSide = leftSide.replace(".", "dot")
                    leftSide = leftSide.replace("√", "sqrt")
                    leftSide = leftSide.replace('⌋', ')')
                    leftSide = leftSide.replace("⌊", "floor(")
                    try {
                        val result = eval(leftSide)
                        if (result == rightSide.toDouble()) { //this happens if user submits a correct solution

                            currentET.isFocusable = false
                            crownList[ETlist.indexOf(currentET)].visibility = View.VISIBLE
                            Toast.makeText(applicationContext, "well done", Toast.LENGTH_SHORT).show()
                            successSoundMP.start()
                            xxx3puzzles[ETlist.indexOf(currentET)].isCompleted = true

                            //giving crowns to user
                            if (fileExist("saveLevel${levelnumber}.txt")) {
                                val levelsCompleted = readFromFile("saveLevel${levelnumber}.txt")
                                println("save file for level ${levelnumber}.txt exists and these are completed:${levelsCompleted}")
                                if (levelsCompleted != null) {
                                    if (levelsCompleted.contains(ETlist.indexOf(currentET).toString())) {

                                    } else{
                                        giveCrowns()

                                    }
                                } else{
                                    giveCrowns()
                                }
                            }
                            else{
                                giveCrowns()
                            }

                            //saving progress
                            if (fileExist("saveLevel${levelnumber}.txt")) {
                                var temptext : String = readFromFile("saveLevel${levelnumber}.txt")!! + ETlist.indexOf(currentET).toString()
                                println("the weird save progress")
                                println("temptext")
                                println(levelnumber)
                                writeToFile(temptext,"saveLevel${levelnumber}.txt")
                            }
                            else {
                                writeToFile(ETlist.indexOf(currentET).toString(),"saveLevel${levelnumber}.txt")
                            }


                        } else {
                            Toast.makeText(
                                applicationContext,
                                "sorry it's not right (your soultion: $result)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: java.lang.RuntimeException) {
                        println(leftSide)
                        Toast.makeText(
                            applicationContext,
                            "This expression cannot be interpreted",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                else {Toast.makeText(applicationContext, "Cheating is detected", Toast.LENGTH_SHORT).show()}
            } catch (e: java.lang.IndexOutOfBoundsException) {
                Toast.makeText(applicationContext, "Cheating is detected", Toast.LENGTH_SHORT).show()
            }
        }
        catch (error: UninitializedPropertyAccessException) {
            Toast.makeText(applicationContext, "First, select an equation" , Toast.LENGTH_SHORT).show()
        }
    }
    private fun giveCrowns() {
        if (fileExist("crownSave.txt")) {
            println("exists")
            var crownNumber = readFromFile("crownSave.txt")
            println("there are $crownNumber crowns")
            if (crownNumber != null ){
                var crownNumberInt = crownNumber.toInt() + 1
                println("we add 1 crown so there are now $crownNumberInt crowns")
                binding.crownCounterTV.text = crownNumberInt.toString()
                writeToFile(crownNumberInt.toString(), "crownSave.txt")
            }

        }
        else{
            println("doesn't exist")
            var crownNumber = 1
            binding.crownCounterTV.setText(crownNumber.toString())
            writeToFile(crownNumber.toString(), "crownSave.txt")

        }
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

    fun eightRoot(number: Double): Double {
        var result: Double = Math.sqrt(number)
        result = Math.sqrt(result)
        result = Math.sqrt(result)
        return result
    }

    fun fourthRoot(number: Double): Double {
        var result: Double = Math.sqrt(number)
        result = Math.sqrt(result)
        return result
    }


    fun dFactorial(number: Double): Double {
        val numberk = number.toInt()
        var result = 1

        for (x in numberk downTo 1 step 2 ) {
            result *= x
        }
        return result.toDouble()

    }

    fun factorial(number: Double): Double {
        val numberk = number.toLong()
        var result : Long = 1
        for (n in 2..numberk) {
            result *= n
        }
        return result.toDouble()
    }
    fun dot(number: Double) :Double {
        return number / 10.0.pow(ceil(log(number, 10.0)))
    }

    fun fileExist(fname: String?): Boolean {
        val file = baseContext.getFileStreamPath(fname)
        return file.exists()
    }
    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x = (x*1000 * parseFactor()*1000)/1000000 // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    eat(')'.code)
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                    while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()

                    x = if (func == "sqrt") Math.sqrt(x) else if (func == "tsqrt") eightRoot(x) else if (func == "dot") dot(x) else if (func == "sin") Math.sin(Math.toRadians(x)) else if (func == "dsqrt") fourthRoot(x) else if (func == "cos") Math.cos(Math.toRadians(x)) else if (func == "tan") Math.tan(Math.toRadians(x)) else if (func == "floor") floor(x) else throw RuntimeException("Unknown function: $func")
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.code)) x = Math.pow(x, parseFactor()) // exponentiation

                if (eat('!'.code)) x = factorial(x)

                if (eat('?'.code)) x = dFactorial(x)
                return x
            }
        }.parse()
    }

}



// todo create a better level pick activity


