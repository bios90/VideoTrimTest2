package com.dimfcompany.alisa2auditor.logic.utils

import android.text.TextUtils
import java.lang.StringBuilder
import java.util.*

class StringManager
{
    companion object
    {
        private val allChars: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        fun randomString(): String
        {
            return randomString(20)
        }

        fun randomString(len: Int): String
        {
            val sb: StringBuilder = StringBuilder(len)
            val random: Random = Random()

            for (i in 0..len)
            {
                sb.append(allChars.get(random.nextInt(allChars.length)))
            }

            return sb.toString()
        }

        fun listOfStringToSingle(strings: List<String>): String
        {
            return Companion.listOfStringToSingle(strings, "\n")
        }

        fun listOfStringToSingle(strings: List<String>, separator: String): String
        {
            val sb = StringBuilder()
            for (element in strings)
            {
                sb.append(element)
                if (strings.indexOf(element) != strings.size - 1)
                {
                    sb.append(separator)
                }
            }

            return sb.toString()
        }

        fun formatSeconds(timeInSeconds: Int): String
        {
            val hours = timeInSeconds / 3600
            val secondsLeft = timeInSeconds - hours * 3600
            val minutes = secondsLeft / 60
            val seconds = secondsLeft - minutes * 60

            var formattedTime = ""
            if (hours > 0)
            {
                if (hours < 10)
                    formattedTime += "0"
                formattedTime += "$hours:"
            }

            if (minutes < 10)
            {
                formattedTime += "0"
            }
            formattedTime += "$minutes:"

            if (seconds < 10)
            {
                formattedTime += "0"
            }

            formattedTime += seconds

            return formattedTime
        }

        fun formatFioForOthcet(fio: String): String
        {
            if (TextUtils.isEmpty(fio))
            {
                return ""
            }
            var formated: String? = null

            val wordsAsList = java.util.ArrayList(Arrays.asList(*fio.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))

            for (i in wordsAsList.indices)
            {
                var word = wordsAsList[i]
                word = word.trim { it <= ' ' }
                word = uppercaseFirstLetter(word)
                if (i > 0)
                {
                    word = word.substring(0, 1)
                    word += "."
                }

                wordsAsList[i] = word
            }

            formated = listOfStringToSingle(wordsAsList, " ")
            return formated
        }

        fun uppercaseFirstLetter(str: String): String
        {
            return str.substring(0, 1).toUpperCase() + str.substring(1)
        }


        fun transliterate(message: String): String
        {
            val abcCyr = charArrayOf(' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
            val abcLat = arrayOf(" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
            val builder = StringBuilder()
            for (i in 0 until message.length)
            {
                var haveToAdd = true

                for (x in abcCyr.indices)
                {
                    if (message[i] == abcCyr[x])
                    {
                        builder.append(abcLat[x])
                        haveToAdd = false
                    }
                }

                if (haveToAdd)
                {
                    builder.append(message[i])
                }
            }
            return builder.toString()
        }
    }
}