package com.nalaan.kotimiliar

import org.junit.Test


/**
 * Created by nam.vu on 2016/05/13.
 */
class TypoGeneratorTest{
    @Test
    fun getDoubleCharTypos(){
       var  typo = TypoGenerator()
       typo.getWrongKeyTypos("nam").forEach { e -> print(e) }
    }
}