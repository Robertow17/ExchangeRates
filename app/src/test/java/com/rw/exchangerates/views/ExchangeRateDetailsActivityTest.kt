package com.rw.exchangerates.views

import junit.framework.Assert.assertEquals
import org.junit.Test

class ExchangeRateDetailsActivityTest {

    @Test
    fun testNewLineAfterComma() {
        assertEquals("piątek, 19 listopada 2021".newLineAfterComma(), "piątek,\n 19 listopada 2021")
        assertEquals("wtorek, 17 marca 2020".newLineAfterComma(), "wtorek,\n 17 marca 2020")
        assertEquals("niedziela, 24 czerwca 2001".newLineAfterComma(), "niedziela,\n 24 czerwca 2001")
        assertEquals("Hello World!".newLineAfterComma(), "Hello World!")
        assertEquals("Hello World, Hello Space, Hello Universe!".newLineAfterComma(), "Hello World,\n Hello Space,\n Hello Universe!")
        assertEquals("".newLineAfterComma(), "")
        assertEquals(",,,".newLineAfterComma(), ",\n,\n,\n")

    }
}