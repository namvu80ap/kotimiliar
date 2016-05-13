/**

 */
package com.nalaan.kotimiliar

import java.util.Collections
import java.util.HashMap

/**
 * @author Vu Hoai Nam
 * * This class translate from PHP TypoGenerator to Java
 */
class TypoGenerator {

    fun getAllTypos(word: String): Map<String, Map<String, Int>> {


        val lstWrongKeyTypos = getWrongKeyTypos(word)
        val lstMissedCharTypos = getMissedCharTypos(word)
        val lstTransposedTypos = getTransposedCharTypos(word)
        val lstDoubleCharTypos = getDoubleCharTypos(word)

        val listAll = HashMap<String, Map<String, Int>>()

        listAll.put("lstWrongKeyTypos", lstWrongKeyTypos)
        listAll.put("lstMissedCharTypos", lstMissedCharTypos)
        listAll.put("lstTransposedTypos", lstTransposedTypos)
        listAll.put("lstDoubleCharTypos", lstDoubleCharTypos)

        return listAll
    }

    /**

     * Create by Vu Hoai Nam
     * Create date: Aug 29, 2012
     * @param word
     * *
     * @return
     * *
     *
     *Description: Get all Typos into one list all
     */
    fun getAllToOne(word: String): Map<String, Int> {

        val listAll = getWrongKeyTypos(word)
        listAll.putAll(getMissedCharTypos(word))
        listAll.putAll(getTransposedCharTypos(word))
        listAll.putAll(getDoubleCharTypos(word))

        return listAll
    }


    /**
     * Create by Vu Hoai Nam
     * Description: Generate the typos WrongKey
     * @param word
     * *
     * @return HashMap, Integer> list word with score
     */
    fun getWrongKeyTypos(word: String): HashMap<String, Int> {
        var word = word
        word = word.toLowerCase()
        val typos = HashMap<String, Int>()
        val wordBuffer = StringBuffer(word)
        val length = wordBuffer.length

        //check each character
        for (i in 0..length - 1) {
            //if character has replacements then create all replacements
            val charList = SIMILAR_CHAR_TABLE["" + wordBuffer[i]]
            if (charList != null) {
                //temp word for manipulating
                val tempWord = StringBuffer(word)

                for (j in charList.indices) {
                    tempWord.replace(i, i + 1, charList[j])
                    typos.put(tempWord.toString(), CompareNIST.howConfusableInt(word, tempWord.toString()))
                }
            }
        }

		///TODO legacy
        //return BmaSimilarList.sortHashMapByValues(typos)
		return typos
    }

    /**
     * Create by Vu Hoai Nam
     * Description: Generate the typos Missed
     * @param word
     * *
     * @return HashMap, Integer> list word with score
     */
    fun getMissedCharTypos(word: String): HashMap<String, Int> {
        var word = word
        word = word.toLowerCase()
        val typos = HashMap<String, Int>()
        val wordBuffer = StringBuffer(word)
        val length = wordBuffer.length
        //check each character
        for (i in 0..length - 1) {
            var tempWord = ""
            if (i == 0) {
                // at first character
                tempWord = wordBuffer.substring(i + 1)

            } else if (i + 1 == length) {
                // at last character
                tempWord = wordBuffer.substring(0, i)

            } else {
                // in between
                tempWord = wordBuffer.substring(0, i) + wordBuffer.substring(i + 1)


            }
            typos.put(tempWord.toString(), CompareNIST.howConfusableInt(word, tempWord.toString()))
        }

		///TODO legacy
        //return BmaSimilarList.sortHashMapByValues(typos)
		return typos
    }

    /**
     * Create by Vu Hoai Nam
     * Description: Generate the typos transposed
     * @param word
     * *
     * @return HashMap, Integer> list word with score
     */
    fun getTransposedCharTypos(word: String): HashMap<String, Int> {
        var word = word
        word = word.toLowerCase()
        val worfBuffer = StringBuffer(word)
        val typos = HashMap<String, Int>()
        val lenght = worfBuffer.length
        // check each character
        for (i in 0..lenght - 1) {
            if (i + 1 == lenght) {
                // could have simplified the test by throwing it in the for loop but I didn't to keep it readable
                // at the end no transposition
            } else {
                val tempWord = StringBuffer(word)
                val tempChar = "" + tempWord[i]
                val tempNextChar = "" + tempWord[i + 1]

                tempWord.replace(i, i + 1, tempNextChar)
                tempWord.replace(i + 1, i + 2, tempChar)

                typos.put(tempWord.toString(), CompareNIST.howConfusableInt(word, tempWord.toString()))
            }
        }

		///TODO legacy
		//return BmaSimilarList.sortHashMapByValues(typos)
		return typos
    }


    /**
     * accepts a string returns array of likely double entered character typos
     * arrays contain only characters that are valid domain names
     * @param word
     * *
     * @return HashMap, Integer> list word with score
     */
    fun getDoubleCharTypos(word: String): HashMap<String, Int> {
        var word = word
        word = word.toLowerCase()
        val wordBuffer = StringBuffer(word)
        val typos = HashMap<String, Int>()
        for (i in 0..wordBuffer.length - 1) {
            val tempWord = StringBuffer(wordBuffer.substring(0, i + 1))
            tempWord.append(wordBuffer[i])

            if (i == wordBuffer.length - 1) {
            } else
                tempWord.append(wordBuffer.substring(i + 1))

            typos.put(tempWord.toString(), CompareNIST.howConfusableInt(word, tempWord.toString()))
        }

		///TODO legacy
		//return BmaSimilarList.sortHashMapByValues(typos)
		return typos
    }

    /**
     *
     * Description: Check whether firstWord is typos of secondWord.
     * @param str1 : First word
     * *
     * @param str2 : Second word
     * *
     * @return True = yes ; False = no
     */
    fun isTypos(str1: String, str2: String): Boolean {

        val lstWrongKeyTypos = getWrongKeyTypos(str1)
        if (lstWrongKeyTypos[str2] != null) {
            return true
        }

        val lstMissedCharTypos = getMissedCharTypos(str1)
        if (lstMissedCharTypos[str2] != null) {
            return true
        }

        val lstTransposedTypos = getTransposedCharTypos(str1)
        if (lstTransposedTypos[str2] != null) {
            return true
        }

        val lstDoubleCharTypos = getDoubleCharTypos(str1)
        if (lstDoubleCharTypos[str2] != null) {
            return true
        }

        return false
    }

    companion object {

        /**
         * This table copy from VisialSimilarity charSimilarity
         */
        private val SIMILAR_CHAR_TABLE = Collections.unmodifiableMap(object : HashMap<String, Array<String>>() {
            init {
                /**
                 * THIS IS THE LIST OF SIMILAR TYPO
                 * BASE ON THE PHP TYPO GENERATOR
                 */
                put("1", arrayOf("2", "q"))
                put("2", arrayOf("1", "q", "w", "3"))
                put("3", arrayOf("2", "w", "e", "4"))
                put("4", arrayOf("3", "e", "r", "5"))
                put("5", arrayOf("4", "r", "t", "6"))
                put("6", arrayOf("5", "t", "y", "7"))
                put("7", arrayOf("6", "y", "u", "8"))
                put("8", arrayOf("7", "u", "i", "9"))
                put("9", arrayOf("8", "i", "o", "0"))
                put("0", arrayOf("9", "o", "p", "-"))
                put("-", arrayOf("0", "p"))

                // 2nd from top
                put("q", arrayOf("1", "2", "w", "a"))
                put("w", arrayOf("q", "a", "s", "e", "3", "2"))
                put("e", arrayOf("w", "s", "d", "r", "4", "3"))
                put("r", arrayOf("e", "d", "f", "t", "5", "4"))
                put("t", arrayOf("r", "f", "g", "y", "6", "5"))
                put("y", arrayOf("t", "g", "h", "u", "7", "6"))
                put("u", arrayOf("y", "h", "j", "i", "8", "7"))
                put("i", arrayOf("u", "j", "k", "o", "9", "8"))
                put("o", arrayOf("i", "k", "l", "p", "0", "9"))
                put("p", arrayOf("o", "l", "-", "0", "q")) // How p and q become typos ??

                // home row
                put("a", arrayOf("z", "s", "w", "q"))
                put("s", arrayOf("a", "z", "x", "d", "e", "w"))
                put("d", arrayOf("s", "x", "c", "f", "r", "e"))
                put("f", arrayOf("d", "c", "v", "g", "t", "r"))
                put("g", arrayOf("f", "v", "b", "h", "y", "t"))
                put("h", arrayOf("g", "b", "n", "j", "u", "y"))
                put("j", arrayOf("h", "n", "m", "k", "i", "u"))
                put("k", arrayOf("j", "m", "l", "o", "i"))
                put("l", arrayOf("k", "p", "o", "i")) // changed 1 to i or just remove i??

                // bottom row
                put("z", arrayOf("x", "s", "a"))
                put("x", arrayOf("z", "c", "d", "s"))
                put("c", arrayOf("x", "v", "f", "d"))
                put("v", arrayOf("c", "b", "g", "f"))
                put("b", arrayOf("v", "n", "h", "g"))
                put("n", arrayOf("b", "m", "j", "h"))
                put("m", arrayOf("n", "k", "j"))

            }
        })

        @JvmStatic fun main(args: Array<String>) {
            val typeos = TypoGenerator()
            val check = typeos.isTypos("namvu", "namvu")
            print(check)
        }
    }
}
