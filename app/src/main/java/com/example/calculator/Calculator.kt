package com.example.calculator

class Calculator {

    private var substringBeforeSub_example =""
    private var substringAfterSub_example =""
    private var start = 0
    private var index = 0
    private var end = 0
    private lateinit var arrOfCharr: CharArray

    fun solve(example: String): String {

        var answer  = example
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

    private fun solveSub_example(str: String): String {
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
        substringBeforeSub_example = answer.substring(0, index)
        arrOfCharr = when(x){
            '*'-> charArrayOf('+', '-', '/')
            '-'-> charArrayOf('+', '*', '/')
            '+'-> charArrayOf('*', '-', '/')
            '/'-> charArrayOf('+', '-', '*')
            else -> charArrayOf()
        }
        start = substringBeforeSub_example.lastIndexOfAny(arrOfCharr) + 1
        if (start == -1) {
            start = 0
            substringBeforeSub_example = ""
        } else {
            substringBeforeSub_example = substringBeforeSub_example.substring(0, start)
        }
        end = answer.indexOfAny(charArrayOf('+', '-', '/', '*'), index + 1, true)
        if (end == -1) {
            end = answer.length
            substringAfterSub_example = ""
        } else {
            substringAfterSub_example = answer.substring(end)
        }
        return substringBeforeSub_example + solveSub_example(answer.substring(start, end)) + substringAfterSub_example
    }
}