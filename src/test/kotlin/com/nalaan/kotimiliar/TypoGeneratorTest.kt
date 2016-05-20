package com.nalaan.kotimiliar

import org.junit.Test
import java.util.*


/**
 * Created by nam.vu on 2016/05/13.
 */
class TypoGeneratorTest{

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

//    @Test
//    fun testSimiliarGenerator(){
//
//        println(SimilarGenrator.getAll( "vuhoainam" , 20, true ))
//        var compareNIST = CompareNIST()
//        compareNIST.init()
//        println( compareNIST.generateSimiliarStr("vuhoainam", 0 ) )
//    }


    fun move( start : String, end : String , totalDistance : Int ) : Int{
//        for( nextMap in map.entries ) {
//            if (nextMap.key.equals(end)) {
//                return totalDistance;
//            }
//            //Find submap
//        }
        return totalDistance;
    }
}
