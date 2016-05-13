/**

 */
package com.nalaan.kotimiliar

import java.util.*

/**
 * @author Vu Hoai Nam
 * *
 *
 *This class generate the words not base on similar mapping character
 */
object SimilarGenrator {

    /**
     * The domain name format just include a-z , A-Z , 0-9 , "-" , "_" ( but the "-" and "_" not at the begin or the end )
     */
    private val DOMAIN_NAME_CHAR = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-")
    /**
     * The accessible length of
     */
    private val CHANGE_ONE_ACCESSIBLE = 5
    private val ADD_ONE_ACCESSIBLE = 5
    private val ADD_TWO_ACCESSIBLE = 12
    private val CHANGE_TWO_ACCESSIBLE = 10
    private val CHANGE_TWO_TYPOS_ACCESSIBLE = 5
    private val ADD_ONE_CHANGE_ONE_ACCESSIBLE = 11
    private val SUBTRACT_ONE_ACCESSIBLE = 6
    private val SUBTRACT_TWO_ACCESSIBLE = 14

    /**
     * Max word length accessible
     */
    private val MAX_LENGTH_ACCESSIBLE = 14

    /**
     * The default threshold
     */
    private var threshold: Int = 0

    /**
     * Generate the words with just replace one char
     */
    fun changeOneChar(word: String): Set<String> {
        var word = word
        //check permission
        if (word.length < CHANGE_ONE_ACCESSIBLE) return HashSet()

        word = word.toLowerCase()
        val processBuilder = StringBuilder(word)
        val returnList = HashSet<String>()

        val domainCharLength = DOMAIN_NAME_CHAR.size

        val wordLength = processBuilder.length
        for (i in 0..processBuilder.length - 1) {

            for (j in 0..domainCharLength - 1) {
                val wordBuilder = StringBuilder(word)
                val tmp = wordBuilder.replace(i, i + 1, DOMAIN_NAME_CHAR[j]).toString()

                //Domain format do not accept - and _ at start and end of string
                if (!tmp.matches("^-.*".toRegex()) && !tmp.matches("^\\_.*".toRegex())
                        && tmp.indexOf("-") != wordLength - 1 && tmp.indexOf("_") != wordLength - 1)
                    returnList.add(tmp)
            }
        }

        return returnList
    }

    /**
     * Generate words by changing two character inside
     */
    fun changeTwoChar(word: String): Set<String> {
        var word = word

        if (word.length < CHANGE_TWO_TYPOS_ACCESSIBLE)
            return HashSet()
        else if (word.length >= CHANGE_TWO_TYPOS_ACCESSIBLE && word.length < CHANGE_TWO_ACCESSIBLE)
            return changeTwoNextChar(word)

        word = word.toLowerCase()
        val processBuilder = StringBuilder(word)
        val returnList = HashSet<String>()

        val wordLength = processBuilder.length
        for (i in 0..processBuilder.length - 1) {

            for (str1 in DOMAIN_NAME_CHAR) {
                val wordBuilder = StringBuilder(word)
                val tmp1 = wordBuilder.replace(i, i + 1, str1).toString()
                for (j in i + 1..wordLength - 1) {
                    for (str2 in DOMAIN_NAME_CHAR) {
                        val tmp2 = StringBuilder(tmp1).replace(j, j + 1, str2).toString()
                        //Domain format do not accept - and _ at start and end of string
                        if (!tmp2.matches("^-.*".toRegex()) && !tmp2.matches("^\\_.*".toRegex())
                                && tmp2.indexOf("-") != wordLength - 1 && tmp2.indexOf("_") != wordLength - 1)
                            returnList.add(tmp2)
                    }
                }
            }
        }

        return returnList
    }

    /**
     * Changing two next char, copied form Typo Transpose
     * @param word
     * *
     * @return List No duplicate words
     */
    fun changeTwoNextChar(word: String): Set<String> {
        var word = word
        word = word.toLowerCase()
        val worfBuffer = StringBuffer(word)
        val typos = HashSet<String>()
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

                typos.add(tempWord.toString())
            }
        }

        return typos
    }

    /**
     * Generate words by adding one character
     * @param word
     * *
     * @return List No duplicate words
     */
    fun addOneChar(word: String): Set<String> {
        var word = word

        if (word.length < ADD_ONE_ACCESSIBLE) return HashSet()

        word = word.toLowerCase()
        val processBuilder = StringBuilder(word)
        val returnList = HashSet<String>()

        //process
        for (i in 0..processBuilder.length) {
            for (str in DOMAIN_NAME_CHAR) {
                val tmp = StringBuilder(word).insert(i, str).toString()
                if (!tmp.matches("^-.*".toRegex()) && !tmp.matches("^\\_.*".toRegex()))
                    returnList.add(tmp)
            }
        }


        return returnList
    }

    /**
     * Generate words by changing one char and adding one char
     * @param word
     * *
     * @return List No duplicate words
     */
    fun addOneChangeOne(word: String): Set<String> {
        var word = word

        //check permission
        if (word.length < ADD_ONE_CHANGE_ONE_ACCESSIBLE)
            return HashSet()

        word = word.toLowerCase()
        val processBuilder = StringBuilder(word)
        val wordLength = processBuilder.length
        val returnList = HashSet<String>()

        //process change one then add one
        for (i in 0..processBuilder.length - 1) {
            for (str1 in DOMAIN_NAME_CHAR) {
                val tmp1 = StringBuilder(word).replace(i, i + 1, str1).toString()
                for (j in i + 1..wordLength - 1) {
                    for (str2 in DOMAIN_NAME_CHAR) {
                        val tmp2 = StringBuilder(tmp1).insert(j + 1, str2).toString()
                        //Domain format do not accept - and _ at start and end of string
                        if (!tmp2.matches("^-.*".toRegex()) && !tmp2.matches("^\\_.*".toRegex())
                                && tmp2.indexOf("-") != tmp2.length - 1 && tmp2.indexOf("_") != tmp2.length - 1)
                            returnList.add(tmp2)
                    }
                }
            }
        }

        //process add one then change one
        for (i in 0..processBuilder.length - 1) {
            for (str1 in DOMAIN_NAME_CHAR) {
                val tmp1 = StringBuilder(word).insert(i, str1).toString()
                for (j in i + 1..wordLength - 1) {
                    for (str2 in DOMAIN_NAME_CHAR) {
                        val tmp2 = StringBuilder(tmp1).replace(j, j + 1, str2).toString()
                        //Domain format do not accept - and _ at start and end of string
                        if (!tmp2.matches("^-.*".toRegex()) && !tmp2.matches("^\\_.*".toRegex())
                                && tmp2.indexOf("-") != tmp2.length - 1 && tmp2.indexOf("_") != tmp2.length - 1)
                            returnList.add(tmp2)
                    }
                }
            }
        }

        return returnList
    }

    /**
     * Subtract by one char or missing one char
     *
     * This function copied form typos missing
     * @param word
     * *
     * @return List No duplicate words
     */
    fun subtractOne(word: String): Set<String> {
        var word = word

        //check permission
        if (word.length < SUBTRACT_ONE_ACCESSIBLE) return HashSet()

        word = word.toLowerCase()
        val typos = HashSet<String>()
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
            typos.add(tempWord)
        }

        return typos
    }

    /**
     * Subtract by one char or missing one char
     *
     * This function copied form typos missing
     */
    fun subtractTwo(word: String): Set<String> {
        var word = word

        //check permission
        if (word.length < SUBTRACT_TWO_ACCESSIBLE) return HashSet()

        word = word.toLowerCase()
        val typos = HashSet<String>()
        val wordBuffer = StringBuffer(word)
        val length = wordBuffer.length
        //check each character
        for (i in 0..length - 1 - 1) {
            val tmp1: String
            if (i == 0)
                tmp1 = wordBuffer.substring(i + 1)
            else
                tmp1 = StringBuilder(word).delete(i, i + 1).toString()

            for (j in 0..tmp1.length - 1 - 1) {
                val tempWord = StringBuilder(tmp1).delete(j, j + 1).toString()
                typos.add(tempWord)
            }

        }

        return typos
    }

    /**
     * Generate words by changing two character
     * @param word
     * *
     * @return List No duplicate words
     */
    fun addTwoChar(word: String): Set<String> {
        var word = word

        //check permission
        if (word.length < ADD_TWO_ACCESSIBLE) return HashSet()

        word = word.toLowerCase()
        val processBuilder = StringBuilder(word)
        val wordLength = processBuilder.length
        val returnList = HashSet<String>()

        //processing
        for (i in 0..wordLength - 1) {
            for (str1 in DOMAIN_NAME_CHAR) {
                val tmp = StringBuilder(word).insert(i, str1).toString()
                for (j in i + 1..wordLength) {
                    for (str2 in DOMAIN_NAME_CHAR) {
                        val tmp2 = StringBuilder(tmp).insert(j + 1, str2).toString()
                        if (!tmp2.matches("^-.*".toRegex()) && !tmp2.matches("^\\_.*".toRegex())
                                && tmp2.indexOf("-") != tmp2.length - 1 && tmp2.indexOf("_") != tmp2.length - 1)
                            returnList.add(tmp2)
                    }
                }
            }
        }


        return returnList
    }


    /**
     * Compare list of words with original words.
     *
     * Just compare the necessary words
     * @return Map, Integer> list word with score
     */
    fun runCompare(list: Set<String>, word: String): Map<String, Int> {

		val array = list.toTypedArray()
        val returnList = CompareNIST.compareWithNIST(word, array, 80)

        //Remove the 100% similar item
        returnList.remove(word)

        return returnList
    }

    /**
     * Compare list of words with original word.
     *
     * Just compare the necessary words
     * @return Map, Integer> list word with score
     */
    fun runCompare(list: Set<String>, word: String, threshold: Int, isAll: Boolean): Map<String, Int> {

        val array = list.toTypedArray()
        val returnList = CompareNIST.compareWithNIST(word, array, threshold)

        //Remove the 100% similar item
        returnList.remove(word)

        return returnList
    }

    /**
     * Override runCompare
     * @param list
     * *
     * @param word
     * *
     * @param threshold
     * *
     * @param isAll
     * *
     * @return Map, Integer> list word with score
     */
    fun runCompare(list: Queue<String>, word: String): Map<String, Int> {
        val set = HashSet<String>()
        for (str in list) {
            set.add(str)
        }
        return runCompare(set, word)
    }

    /**
     * Generate All the case base on all these function
     * @param isRunAll : is get all the generator from changeTwoChar function
     * *
     * @return Map, Map, Integer>> include many maps base on type of SimilarGenerator
     * * ( ex: changeOneChar, addOneChar, subtractOne ..)
     */
    fun getAll(word: String, threshold: Int, isRunAll: Boolean): Map<String, Map<String, Int>> {

        SimilarGenrator.threshold = threshold

        val returnList = HashMap<String, Map<String, Int>>()

        val changeOneChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(changeOneChar(word), word, threshold, isRunAll) else HashMap<String, Int>()
        returnList.put("changeOneChar", changeOneChar)

        val addOneChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(addOneChar(word), word, threshold, isRunAll) else HashMap<String, Int>()
        returnList.put("addOneChar", addOneChar)

        val subtractOneChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(subtractOne(word), word, threshold, isRunAll) else HashMap<String, Int>()
        returnList.put("subtractOneChar", subtractOneChar)

        val addTwoChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(addTwoChar(word), word, threshold, isRunAll) else HashMap<String, Int>()
        returnList.put("addTwoChar", addTwoChar)

        val subtractTwo = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(subtractTwo(word), word, threshold, isRunAll) else HashMap<String, Int>()
        returnList.put("subtractTwo", subtractTwo)

        if (isRunAll) {

            val changeTwoChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(changeTwoChar(word), word) else HashMap<String, Int>()
            returnList.put("changeTwoChar", changeTwoChar)

            val addOneChangeOne = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(addOneChangeOne(word), word) else HashMap<String, Int>()
            returnList.put("addOneChangeOne", addOneChangeOne)

        } else {
            //GET SMALL lIST 

            val changeTwoChar = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(changeTwoChar(word), word, threshold, isRunAll) else HashMap<String, Int>()
            returnList.put("changeTwoChar", changeTwoChar)

            val addOneChangeOne = if (word.length < MAX_LENGTH_ACCESSIBLE) runCompare(addOneChangeOne(word), word, threshold, isRunAll) else HashMap<String, Int>()
            returnList.put("addOneChangeOne", addOneChangeOne)
        }


        return returnList
    }

}
