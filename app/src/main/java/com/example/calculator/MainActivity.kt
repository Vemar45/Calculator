package com.example.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val decorView: View = this.window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
        decorView.systemUiVisibility = newUiOptions


        var dialText = ""
        var helpString:String
        var index:Int
        var x:Double
        val calculator = Calculator()


        fun setText() {
            dial.text = dialText
        }

        fun сanPlaceCommand():Boolean{
            return if(dialText.isNotEmpty()) {
                return when(dialText.last()){
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')' -> true
                    else -> false
                }
            } else {
                false
            }
        }

        fun сanPlaceDigit(x: Char):Boolean{
            return if(dialText.isNotEmpty()) {
                if(x!='0') {
                    index = dialText.lastIndexOfAny(charArrayOf('+', '-', '*', '/')) + 1
                    if(index==-1) index = 1
                    helpString = dialText.substring(index)
                    if(helpString.length==1&&helpString.last()=='0'){
                        dialText = dialText.substring(0, dialText.length - 1)
                        return true
                    }
                    when (dialText.last()) {
                        '*', '/', '+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',' -> true
                        else -> false
                    }
                } else {
                    index = dialText.lastIndexOfAny(charArrayOf('+', '-', '*', '/')) + 1
                    if(index==-1) index = 1
                    helpString = dialText.substring(index)
                    if(helpString.contains(Regex("[1-9]"))|| helpString.isEmpty()) {
                        when (dialText.last()) {
                            '*', '/', '+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',' -> true
                            else -> false
                        }
                    } else {
                        false
                    }
                }
            } else {
                true
            }
        }


        fun correct(s: String):String{
            var str = s.replace(")", "").replace("(-", "_")
            str = str.replace(',', '.').replace("E-", "E_")
            if (str.first()=='-') str = str.replaceFirst('-', '_')
            return str
        }

        fun canPlaceComma():Boolean{
            helpString = correct(dialText)
            index = helpString.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
            if (index==-1) {
                return !dialText.contains(',')
            }else {
                return !dialText.substring(index + 1).contains(',')
            }
        }


        fun setCommand(x: Char){
            if(сanPlaceCommand()){
                if(x=='='){
                    dialText = calculator.solve(correct(dialText))
                } else {
                    dialText += x
                }
                setText()
            }
        }

        fun setDigit(x: Char){
            if(сanPlaceDigit(x)) {
                dialText += x
                setText()
            }
        }

        zero.setOnClickListener {
            setDigit('0')
        }
        one.setOnClickListener {
            setDigit('1')
        }
        two.setOnClickListener {
            setDigit('2')
        }
        three.setOnClickListener {
            setDigit('3')
        }
        four.setOnClickListener {
            setDigit('4')
        }
        five.setOnClickListener {
            setDigit('5')
        }
        six.setOnClickListener {
            setDigit('6')
        }
        seven.setOnClickListener {
            setDigit('7')
        }
        eight.setOnClickListener {
            setDigit('8')
        }
        nine.setOnClickListener {
            setDigit('9')
        }
        delete_all.setOnClickListener{
            dialText = ""
            setText()
        }
        changing_the_sign.setOnClickListener {
            if (dialText.isNotEmpty()&&сanPlaceCommand()) {
                index = dialText.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
                dialText = if (index == -1) {
                    "(-$dialText)"
                } else if (index == 0) {
                    dialText.substring(index + 1)
                } else if (dialText[index - 1] == '(') {
                    if (index == 1) {
                        dialText.substring(index + 1, dialText.indexOf(')')) + dialText.substring(dialText.indexOf(')') + 1)
                    } else {
                        dialText.substring(0, index - 1) + dialText.substring(index + 1, dialText.indexOf(')')) + dialText.substring(dialText.indexOf(')') + 1)
                    }
                } else {
                    when (dialText[index]) {
                        '+' -> dialText.substring(0, index) + "-" + dialText.substring(index + 1)
                        '-' -> dialText.substring(0, index) + "+" + dialText.substring(index + 1)
                        else -> dialText.substring(0, index + 1) + "(-" + dialText.substring(index + 1) + ")"
                    }
                }
            }
            setText()
        }
        percent.setOnClickListener {
            if(сanPlaceCommand()){
                helpString = correct(dialText)
                index = helpString.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
                if (index==-1) {
                    dialText = calculator.solve("$helpString/100")
                }else {
                    x = calculator.solve(helpString.substring(0, index)).toDouble()
                    dialText = dialText.substring(0, index + 1)+calculator.solve("$x*" + helpString.substring(index + 1) + "/100")
                }
                setText()
            }
        }
        divide.setOnClickListener {
            setCommand('/')
        }
        multiply.setOnClickListener {
            setCommand('*')
        }
        minus.setOnClickListener {
            setCommand('-')
        }
        plus.setOnClickListener {
            setCommand('+')
        }
        equal.setOnClickListener {
            setCommand('=')
        }
        comma.setOnClickListener{
            if(canPlaceComma()) {
                setCommand(',')
            }
        }

    }
}