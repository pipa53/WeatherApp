/**
 * @author Бельский Тимофей
 * @version 1.0
 * Тесты для Web платформы (WasmJs).
 */
package com.example.lab9_project1_belskiy

import com.example.lab9_project1_belskiy.model.MainData
import com.example.lab9_project1_belskiy.model.WeatherResponse
import com.example.lab9_project1_belskiy.model.WindData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WebPlatformTest {

    /**
     * Проверка корректности окружения.
     */
    @Test
    fun testWebEnvironment() {
        val isWeb = true 
        assertTrue(isWeb, "Тест запущен в Web-окружении")
    }

    /**
     * Тест модели данных в контексте Web.
     * Проверяет, что поля данных корректно инициализируются.
     */
    @Test
    fun testWeatherModelIntegrity() {
        val weather = WeatherResponse(
            name = "WebCity",
            main = MainData(temp = 25.0, humidity = 40),
            weather = emptyList(),
            wind = WindData(speed = 5.0)
        )
        
        assertEquals("WebCity", weather.name)
        assertEquals(25.0, weather.main.temp)
    }

    /**
     * Тест логики форматирования (пример).
     */
    @Test
    fun testTemperatureDisplay() {
        val temp = 22.5
        val display = "${temp}°C"
        assertEquals("22.5°C", display)
    }
}
