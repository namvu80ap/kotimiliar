package com.nalaan.kotimiliar

/**
 * Created by nam.vu on 2016/05/16.
 */
import java.util.concurrent.ConcurrentLinkedQueue
import java.io.File;
import java.util.*

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
open class CompareNIST {

    open var similarMap = HashMap<String, MutableSet<String>>()
    open var pointMap = HashMap<String, MutableMap<String, Double>>()

    fun generateSimiliarStr(original: String, threshold: Int): HashMap<String, Int> {
        val mapOfSimiliarChar = getMapOfSimiilarSet(original);
        val rtList = HashMap<String, Int>()
        original.indices.forEach {
            charAt -> val charList = mapOfSimiliarChar[original[charAt]]
                charList!!.forEach {
                    //replace first char
                    chr -> val tmp1 = original.replaceRange(charAt,charAt+1,chr)
                    println("tmp1" + tmp1)
                    val score = (compareShortStr(original, tmp1) * 100).toInt()
                    if( score >= threshold )
                      rtList.put(tmp1,score)

                    //replace next char
                    if( charAt < original.lastIndex ) {
                      val charList2 = mapOfSimiliarChar[original[charAt + 1]]
                      charList2!!.forEach {
                          chr2 -> val tmp2 = tmp1.replaceRange(charAt + 1, charAt + 2, chr2)
                          println("tmp2" + tmp2)
                          val score2 = (compareShortStr(original, tmp2) * 100).toInt()
                          if (score2 >= threshold)
                              rtList.put(tmp2, score2)
                      }
                    }

                }

        }
//        println("SIZE : " + rtList.size)
        return rtList
    }

    private fun getMapOfSimiilarSet( str : String ) : MutableMap<Char,MutableSet<String>> {
        val map : MutableMap<Char,MutableSet<String>> = mutableMapOf<Char,MutableSet<String>>()
        for( charAt in str ){
            if( similarMap.containsKey(charAt.toString()) ){
                val similarChar = similarMap[charAt.toString()]!!
                map.put( charAt, similarChar)
            }
        }
        return map;
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

        init()
        val resultList = ArrayList<Double>()

        for (i in right.indices) {
            val result = howConfusableAre(left, right[i])
            resultList.add(result)
        }

        val listResult = HashMap<String, Int>()

        //Populate the list result to HashMap
        if (resultList.size == right.size)
            for (i in resultList.indices) {
                val strScore = (resultList[i] * 100).toInt()
                if (strScore >= threshold)
                    listResult.put(right[i], strScore)
            }

        //        return listResult = BmaSimilarList.sortHashMapByValues(listResult);
        return listResult
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
     * Init the similar Map
     *
     * Parse the mapping similar char to Map,Set>
     *
     * And similar point to Map, Map, Double>>
     */
    fun init() {
        val loader = CompareNIST::class.java!!.getClassLoader()
        val file : File = File(loader.getResource("mapping.txt").getFile());
        //TODO - Make dynamic reading the mapping source when run on server on test on local or get from Memory
        //URL location = CompareNIST.class.getProtectionDomain().getCodeSource().getLocation();

        val sc = Scanner(file)
        while (sc.hasNextLine()) {
            val ch1 = sc.next().toLowerCase()
            val ch2 = sc.next().toLowerCase()
            val point = java.lang.Double.parseDouble(sc.next())

            var set1: MutableSet<String>? = similarMap[ch1]
            if (set1 == null) {
                set1 = HashSet<String>()
                similarMap.put(ch1, set1)
            }
            set1.add(ch2)

            var set2: MutableSet<String>? = similarMap[ch2]
            if (set2 == null) {
                set2 = HashSet<String>()
                similarMap.put(ch2, set2)
            }
            set2.add(ch1)

            var map1: MutableMap<String, Double>? = pointMap.get(ch1)
            if (map1 == null) {
                map1 = HashMap<String, Double>()
                pointMap.put(ch1, map1)
            }
            map1.put( ch2, point)

            var map2: MutableMap<String, Double>? = pointMap.get(ch2)
            if (map2 == null) {
                map2 = HashMap<String, Double>()
                pointMap.put(ch2, map2)
            }
            map2.plus( Pair(ch1, point) )
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

    fun levenshtein(s: String, t: String): Double {
        var s = s
        var t = t
        s = s.toLowerCase()
        t = t.toLowerCase()

        val effInfinity = Integer.MAX_VALUE

        val len_s = s.length
        val len_t = t.length
        val max_l = Math.max(len_s, len_t)

        val d: Array<Array<Double>> = Array(max_l + 1, { i -> Array<Double>(max_l + 1, { x -> x.toDouble()} )} )

        for (i in 0..d[0].size - 1) {
            d[0][i] = i.toDouble()
        }

        for (i in 1..len_s + 1 - 1) {
            d[i] = Array<Double>(max_l + 1, { i -> i.toDouble() })
            d[i][0] = i.toDouble()
        }

        for (i in 0..len_s - 1) {
            for (j in 0..len_t - 1) {
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
        if (i < 1 || j < 2 || s.substring(i - 1, i + 1) != t.substring(j - 2, j) || t.substring(j - 2, j) != t[j].toString().plus(t[j]) ) {
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
                var match = pointMap.get(ch1)
                val p = match?.get(ch2)
                if (p != null) {
                    point = p
                }
            }
        }
        return point
    }

}