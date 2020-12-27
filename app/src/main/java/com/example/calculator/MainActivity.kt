package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var dialtext = ""
    private var substring:String=""
    private var index:Int=0
    private val calculator = Calculator()

    private fun setText() {
        dialText.text = dialtext
    }

    private fun сanPlaceCommand():Boolean{
        return if(dialtext.isNotEmpty()) {
            return dialtext.last().isDigit()||dialtext.last()==')'
        } else {
            false
        }
    }

    private fun сanPlaceDigit(x: String):Boolean{
        return if(dialtext.isNotEmpty()) {
            index = dialtext.lastIndexOfAny(charArrayOf('+', '-', '*', '/')) + 1
            if(index==-1) index = 1
            substring = dialtext.substring(index)
            if(substring.length==1&&substring.last()=='0'){
                dialtext = dialtext.substring(0, dialtext.length - 1)
                return true
            }
            if(x=="0"&&substring.isNotEmpty()&&!substring.contains(Regex("[1-9,]"))){
                return false
            }
            when (dialtext.last()) {
                '*', '/', '+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',' -> true
                else -> false
            }
        } else {
            true
        }
    }


    private fun correct(s: String):String{
        var str = s.replace(")", "").replace("(-", "_")
        str = str.replace(',', '.').replace("E-", "E_")
        if (str.first()=='-') str = str.replaceFirst('-', '_')
        return str
    }

    private fun canPlaceComma():Boolean{
        return if(dialtext.isNotEmpty()) {
            substring = correct(dialtext)
            index = substring.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
            if (index == -1) {
                !dialtext.contains(',')
            } else {
                !dialtext.substring(index + 1).contains(',')
            }
        } else {
            false
        }
    }


    private fun setCommand(x: String){
        if(сanPlaceCommand()){
            when(x){
                "="->dialtext = calculator.solve(correct(dialtext))
                "x"->dialtext += "*"
                "÷"->dialtext += "/"
                ","-> if(canPlaceComma()) dialtext += ","
                else ->dialtext += x
            }
            setText()
        }
    }

    private fun setDigit(x: String){
        if(сanPlaceDigit(x)) {
            dialtext += x
            setText()
        }
    }

    private fun changingTheSign(){
        if (dialtext.isNotEmpty()&&сanPlaceCommand()) {
            index = dialtext.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
            dialtext = if (index == -1) {
                "(-$dialtext)"
            } else if (index == 0) {
                dialtext.substring(1)
            } else if (dialtext[index - 1] == '(') {
                if (index == 1) {
                    dialtext.substring(index + 1, dialtext.indexOf(')')) + dialtext.substring(dialtext.indexOf(')') + 1)
                } else {
                    dialtext.substring(0, index - 1) + dialtext.substring(index + 1, dialtext.indexOf(')')) + dialtext.substring(dialtext.indexOf(')') + 1)
                }
            } else {
                when (dialtext[index]) {
                    '+' -> dialtext.substring(0, index) + "-" + dialtext.substring(index + 1)
                    '-' -> dialtext.substring(0, index) + "+" + dialtext.substring(index + 1)
                    else -> dialtext.substring(0, index + 1) + "(-" + dialtext.substring(index + 1) + ")"
                }
            }
        }
        setText()
    }

    private fun percent(){
        if(сanPlaceCommand()){
            substring = correct(dialtext)
            index = substring.lastIndexOfAny(charArrayOf('+', '-', '*', '/'))
            dialtext = if (index==-1) {
                calculator.solve("$substring/100")
            }else {
                val x = calculator.solve(substring.substring(0, index)).toDouble()
                dialtext.substring(0, index + 1)+calculator.solve("$x*" + substring.substring(index + 1) + "/100")
            }
            setText()
        }
    }

    fun digitButton(v:View){
        setDigit((v as Button).text.toString())
    }

    fun commandButton(v:View){
        setCommand((v as Button).text.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delete_all_Button.setOnClickListener{
            dialtext = ""
            setText()
        }
        changing_the_sign_Button.setOnClickListener {
            changingTheSign()
        }
        percentButton.setOnClickListener {
            percent()
        }

    }
}