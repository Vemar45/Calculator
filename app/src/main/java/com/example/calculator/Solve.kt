package com.example.calculator

class Calculator {

    private var helpStr1 =""
    private var helpStr2 =""
    private var start = 0
    private var index = 0
    private var end = 0
    private lateinit var arrOfCharr: CharArray

    fun solve(example: String): String {

        var answer  = example
        if(answer.first()=='-') answer = answer.replaceFirst('-','_')
        while (answer.contains('/')) {
            answer = getNewAnswer('/',answer)
        }
        while (answer.contains('*')) {
            answer = getNewAnswer('*',answer)
        }
        while (answer.contains('+')) {
            answer = getNewAnswer('+',answer)
        }
        while (answer.contains('-')) {
            answer = getNewAnswer('-',answer)
        }
        if(answer.endsWith(".0")) answer = answer.substring(0,answer.length-2)
        return answer.replace("_","-").replace('.',',')
    }

    private fun solve1(str: String): String {
        val index = str.indexOfAny(charArrayOf('+', '-', '/', '*'))
        var a = str.substring(0, index)
        var b = str.substring(index + 1)
        if (a.contains("_")) a = a.replace("_", "-")
        if (b.contains("_")) b = b.replace("_", "-")
        if (a.contains("E_")) a = a.replace("E_", "E-")
        if (b.contains("E_")) b = b.replace("E_", "E-")
        return when (str[index]) {
            '-' -> (a.toDouble() - b.toDouble()).toString().replace('-','_')
            '+' -> (a.toDouble() + b.toDouble()).toString().replace('-','_')
            '*' -> (a.toDouble() * b.toDouble()).toString().replace('-','_')
            '/' -> ((a.toDouble() / b.toDouble())).toString().replace('-','_').replace("E-","E_")
            else -> "0"
        }
    }

    private fun getNewAnswer(x: Char,answer: String): String {
        index = answer.indexOf(x)
        helpStr1 = answer.substring(0, index)
        arrOfCharr = when(x){
            '*'-> charArrayOf('+', '-', '/')
            '-'-> charArrayOf('+', '*', '/')
            '+'-> charArrayOf('*', '-', '/')
            '/'-> charArrayOf('+', '-', '*')
            else -> charArrayOf()
        }
        start = helpStr1.lastIndexOfAny(arrOfCharr) + 1
        if (start == -1) {
            start = 0
            helpStr1 = ""
        } else {
            helpStr1 = helpStr1.substring(0, start)
        }
        end = answer.indexOfAny(charArrayOf('+', '-', '/', '*'), index + 1, true)
        if (end == -1) {
            end = answer.length
            helpStr2 = ""
        } else {
            helpStr2 = answer.substring(end)
        }
        return helpStr1 + solve1(answer.substring(start, end)) + helpStr2
    }
}