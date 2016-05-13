package com.nalaan.kotimiliar.core


import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author Vu Hoai Nam
 * *
 *
 *Create date: July 28, 2012
 * *
 * *
 *
 *This class translate the NIST from Python to Java.
 * *
 *
 *It keeping the same function and variables naming from NIST.
 * *
 *
 *And Adding some extra function to generate the words base on the compare algorithm of NIST
 * *
 *
 *For more information about NIST algorithm, please check [NIST](http://hissa.nist.gov/~black/GTLD/tldVisualSimilarity.html)
 */
object CompareNIST {

    private val similarMap = HashMap<String, Set<String>>()
    private val pointMap = HashMap<String, Map<String, Double>>()


    /**
     * Generate String using recursive.

     * @param original The original base word where processed to generate the similar words
     * *
     * @param threshold The bottom limit of percentage.( The similar word will be from threshold to 99% )
     * *
     * @return HashMap< String , Integer >
     * * String is the key of no duplicate words , Integer is the score compared with the word
     */
    fun generateStr(original: String, threshold: Int): HashMap<String, Int> {
        var original = original

        original = original.toLowerCase()

        //create the collection of similar char
        val listSimiliarChr = ArrayList<Set<String>>()
        for (i in 0..original.length - 1) {
            val chr = original[i].toString()
            val setChr = HashSet<String>()
            setChr.add(chr)
            val similarChar = similarMap.get(chr)

            if (similarChar != null)
                setChr.addAll(similarChar)

            listSimiliarChr.add(setChr)
        }

        val tmp = ConcurrentLinkedQueue<String>()
        val tmp2 = ConcurrentLinkedQueue<String>()

        println(listSimiliarChr)

        //Check where recurse to go 
        var charAt = 0

        val doubleCharBuff = StringBuffer(original)
        val originalBuff = StringBuffer(original)

        var doubleSimilar: Set<String>? = null

        //Recurse the char collection list
        for (set in listSimiliarChr) {
            charAt++
            if (tmp.size > 0) {

                //In case replace one char
                for (str in set) {

                    for (tmpStr in tmp) {

						tmpStr.plus(tmp)

                        val builder = StringBuilder()

                        builder.append(tmpStr)

                        builder.append(originalBuff.substring(charAt))

                        val tmpCompareStr = builder.toString()

                        val score = (compareShortStr(original, tmpCompareStr) * 100).toInt()

                        if (score >= threshold)
                            tmp2.add(tmpStr)
                    }
                }

                //In case from second charAt - search the double similar
                if (charAt > 1) {
                    doubleSimilar = similarMap[doubleCharBuff.substring(charAt - 2, charAt)]
                    //For double char replace
                    if (doubleSimilar != null) {
                        for (similarCharWithDouble in doubleSimilar) {
                            val processCharBuff = StringBuffer(original)
                            processCharBuff.replace(charAt - 2, charAt, similarCharWithDouble)
                            val score = (compareShortStr(original, processCharBuff.toString()) * 100).toInt()
                            if (score >= threshold)
                                tmp2.add(processCharBuff.toString())
                        }
                    }
                }

                tmp.clear()
                tmp.addAll(tmp2)
            } else
                tmp.addAll(set)
        }


        val listResult = compareWithNIST(original, tmp2.toArray<String>(arrayOfNulls<String>(tmp2.size)), threshold)

        //Remove the 100% similar item
        listResult.remove(original)

        return listResult
    }

    /**
     * Create by Vu Hoai Nam
     *
     * Create date: Aug 31, 2012
     * @param left: is the basic word
     * *
     * @param right: is the list of words be generated ( All words )
     * *
     * @param threshold : the limit of percentage to add to list
     * *
     * @return : HashMap of list compared word with percentage
     * *
     *
     *Description: This function will compare the list of words with one word, then return
     * * a map of list word with percentage
     */
    fun compareWithNIST(left: String, right: Array<String>, threshold: Int): HashMap<String, Int> {

        CompareNIST.init()
        val resultList = ArrayList<Double>()

        for (i in right.indices) {
            val result = CompareNIST.howConfusableAre(left, right[i])
            resultList.add(result)
        }

        var listResult = HashMap<String, Int>()

        //Populate the list result to HashMap
        if (resultList.size == right.size)
            for (i in resultList.indices) {
                val strScore = (resultList[i] * 100).toInt()
                if (strScore >= threshold)
                    listResult.put(right[i], strScore)
            }

		//Legacy code
        //return listResult = BmaSimilarList.sortHashMapByValues(listResult)
		return listResult;
    }


    /**
     * Compare the short string
     * @param str1
     * *
     * @param str2
     * *
     * @return bouble The percent of similar between str1 and str2
     */
    fun compareShortStr(str1: String, str2: String): Double {

        if (str1 != str2) {
            return howConfusableAre(str1, str2)
        } else
            return 1.0
    }

    /**
     * Compare the short string
     * @param word The word be compared with list string ( lstStr )
     * *
     * @param lstStr List String wish to compare with word.
     * *
     * @return Set The list of lstStr with score compared to word
     */
//    fun compareWithList(word: String, lstStr: Array<String>): List<SimilarWord> {
//
//        if (similarMap == null || pointMap == null) {
//            init()
//        }
//        val returnSet = ArrayList<SimilarWord>()
//
//        for (i in lstStr.indices) {
//            println("LIne" + lstStr[i])
//            if (lstStr[i] != null && lstStr[i] != "") {
//
//                val similarWord = SimilarWord()
//                val str = lstStr[i].toLowerCase()
//                similarWord.setWord(str)
//                similarWord.setScore(howConfusableInt(word, str))
//                similarWord.setTypos(TypoGenerator().isTypos(word, str))
//                returnSet.add(similarWord)
//
//            }
//        }
//        return returnSet
//    }


    /**
     * Init the similar Map
     *
     * Parse the mapping similar char to Map,Set>
     *
     * And similar point to Map, Map, Double>>
     */
    fun init() {
        val loader = CompareNIST::class.java.classLoader

        //TODO - Make dynamic reading the mapping source when run on server on test on local
        //URL location = CompareNIST.class.getProtectionDomain().getCodeSource().getLocation();
        val sc = Scanner(loader.getResourceAsStream("mapping"))
        while (sc.hasNextLine()) {
            val ch1 = sc.next().toLowerCase()
            val ch2 = sc.next().toLowerCase()
            val point = java.lang.Double.parseDouble(sc.next())

            var set1: Set<String>? = similarMap.get(ch1)
            if (set1 == null) {
                set1 = HashSet<String>()
                similarMap.put(ch1, set1)
            }
            set1.plus(ch2)

            var set2: Set<String>? = similarMap.get(ch2)
            if (set2 == null) {
                set2 = HashSet<String>()
                similarMap.put(ch2, set2)
            }
            set2.plus(ch1)

            var map1: Map<String, Double>? = pointMap.get(ch1)
            if (map1 == null) {
                map1 = HashMap<String, Double>()
                pointMap.put(ch1, map1)
            }
            map1.plus( Pair(ch2, point))

            var map2: Map<String, Double>? = pointMap.get(ch2)
            if (map2 == null) {
                map2 = HashMap<String, Double>()
                pointMap.put(ch2, map2)
            }
            map2.plus( Pair(ch1, point))
        }
    }

    /**
     * Calculate the different between two words - Base on NIST code in Python
     * @param str1
     * *
     * @param str2
     * *
     * @return bouble The percent of similar between str1 and str2
     */
    fun howConfusableAre(str1: String, str2: String): Double {
        val score: Double

        val maxlen = Math.max(str1.length, str2.length)
        val lendiff = Math.abs(str1.length - str2.length)
        if (maxlen == 0) {
            score = 1.0
        } else {
            val levDist = levenshtein(str1, str2)
            score = (maxlen - levDist) / (maxlen.toDouble() + 3 * levDist + lendiff * levDist)
        }
        return score
    }

    /**
     * Calculate the different between two words - Base on NIST code in Python
     * @param str1
     * *
     * @param str2
     * *
     * @return int The percent of similar between str1 and str2
     */
    fun howConfusableInt(str1: String, str2: String): Int {
        val score = (howConfusableAre(str1, str2) * 100).toInt()
        return score
    }

    /**
     * The levernshtein calculate - Base on NIST code in Python
     * @param s
     * *
     * @param t
     * *
     * @return bouble The distant between s and t
     */
    private fun levenshtein(s: String, t: String): Double {
        var s = s
        var t = t
        s = s.toLowerCase()
        t = t.toLowerCase()

        val effInfinity = Integer.MAX_VALUE

        val len_s = s.length
        val len_t = t.length
        val max_l = Math.max(len_s, len_t)

        val d = Array(max_l,{ DoubleArray(0) })
        d[0] = DoubleArray(max_l + 1)

        for (i in 0..d[0].size - 1) {
            d[0][i] = i.toDouble()
        }
        for (i in 1..len_s + 1 - 1) {
            d[i] = DoubleArray(max_l + 1)
            d[i][0] = i.toDouble()
        }

        for (i in 0..len_s - 1) {
            for (j in 0..len_t - 1) {
                // System.out.println(i + " " + j);
                var minCost = effInfinity.toDouble()

                // delete
                minCost = Math.min(d[i][j + 1] + 1, minCost)

                // insert
                minCost = Math.min(d[i + 1][j] + 1, minCost)

                // insert after repeat
                val repiCost = repetitionInsert(s, i, t, j)
                if (repiCost >= 0) {
                    minCost = Math.min(d[i + 1][j] + repiCost, minCost)
                }

                // delete after repeat
                val repdCost = repetitionInsert(t, j, s, i)
                if (repdCost >= 0) {
                    minCost = Math.min(d[i][j + 1] + repdCost, minCost)
                }

                // substitute
                val subsCost = 1 - characterSimilarity(s[i].toString(), t[j].toString())
                minCost = Math.min(d[i][j] + subsCost, minCost)

                // compute total costs of 2 for 1, 1 for 2, or 2 for 2
                // substitution
                if (i > 0) {
                    var subs21Cost = 1 - characterSimilarity(s.substring(i - 1, i + 1), t[j].toString())
                    if (subs21Cost == 1.0) {
                        subs21Cost = 2.0
                    }
                    minCost = Math.min(d[i - 1][j] + subs21Cost, minCost)
                }

                if (j > 0) {
                    var subs12Cost = 1 - characterSimilarity(s[i].toString(), t.substring(j - 1, j + 1))
                    if (subs12Cost == 1.0) {
                        subs12Cost = 2.0
                    }
                    minCost = Math.min(d[i][j - 1] + subs12Cost, minCost)
                }

                if (i > 0 && j > 0) {
                    var subs22Cost = 1 - characterSimilarity(s.substring(i - 1, i + 1), t.substring(j - 1, j + 1))
                    if (subs22Cost == 1.0) {
                        subs22Cost = 2.0
                    }
                    minCost = Math.min(d[i - 1][j - 1] + subs22Cost, minCost)

                    if (s[i - 1] == t[j] && s[i] == t[j - 1]) {
                        val transpCost = 1 - characterSimilarity(s[i].toString(), t[j].toString())
                        minCost = Math.min(d[i - 1][j - 1] + transpCost, minCost)
                    }
                }

                d[i + 1][j + 1] = minCost
            }
        }

        return d[len_s][len_t]
    }

    /**
     * repetitionInsert - Base on NIST code in Python
     * @param s
     * *
     * @param i
     * *
     * @param t
     * *
     * @param j
     * *
     * @return double the repetitionInsert between s and t
     */
    private fun repetitionInsert(s: String, i: Int, t: String, j: Int): Double {
        if (i < 1 || j < 2 || s.substring(i - 1, i + 1) != t.substring(j - 2, j) || t.substring(j - 2, j) !== String(charArrayOf(t[j],t[j])) ) {
            return -1.0
        }

        var back = 2
        val mc = t[j]

        while (back <= i && back < j && s[i - back] == mc && t[j - back - 1] == mc) {
            back += 1
        }

        val cost = Math.max(0.0, 1.7 - 0.4 * back)

        return cost
    }

    fun characterSimilarity(ch1: String, ch2: String): Double {
        var point = 0.0
        if (ch1 == ch2) {
            point = 1.0
        } else {
            if (pointMap.containsKey(ch1)) {
                val p = pointMap.get(ch1)?.get(ch2)
                if (p != null) {
                    point = p
                }
            }
        }
        return point
    }

}
