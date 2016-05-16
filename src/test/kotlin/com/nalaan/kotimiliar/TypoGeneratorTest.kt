package com.nalaan.kotimiliar

import org.junit.Test


/**
 * Created by nam.vu on 2016/05/13.
 */
class TypoGeneratorTest{
    @Test
    fun getDoubleCharTypos(){
//       var  typo = TypoGenerator()
//       typo.getWrongKeyTypos("nam").forEach { e -> print(e) }
        var compareNIST = CompareNIST()
        compareNIST.init()
        println( compareNIST.generateSimiliarStr("vuhoainam", 0 ) )
    }

//    @Test
//    fun testReplace(){
//        val test = "12345";
//        test.indices.forEach {
//            item ->  val str = test.replaceRange( item , item + 1 , "a" )
//                     println( str )
//        }
//        println(test)
//    }
}