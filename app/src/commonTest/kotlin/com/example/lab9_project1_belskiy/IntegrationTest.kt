/**
 * @author Бельский Тимофей
 * @version 1.0
 * Интеграционные тесты (связка Репозиторий + API + Настройки).
 * Проверяют полный цикл: от сетевого запроса до сохранения в кеш и историю.
 */
package com.example.lab9_project1_belskiy

import com.example.lab9_project1_belskiy.network.WeatherApi
import com.example.lab9_project1_belskiy.repository.WeatherRepository
import com.russhwolf.settings.MapSettings
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class IntegrationTest {

    /**
     * Тест полного цикла с кешированием.
     * Проверяет, что данные из сети попадают и в кеш (Settings), и в историю.
     */
    @Test
    fun testFullFlowWithCaching() = runTest {
        val settings = MapSettings()
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"name":"Gomel","main":{"temp":15.0,"humidity":60},"weather":[],"wind":{"speed":5.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val api = WeatherApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })

        val repository = WeatherRepository(api, settings)
        repository.getWeather("Gomel")

        // Проверяем, что в настройках сохранилась строка кеша
        assertNotNull(settings.getStringOrNull("cached_weather"))
        // Проверяем историю
        assertEquals(listOf("Gomel"), repository.searchHistory.value)
    }

    /**
     * Тест офлайн-режима.
     * Проверяет, что при ошибке сети возвращаются данные из ранее заполненного кеша.
     */
    @Test
    fun testOfflineDataLoading() = runTest {
        val settings = MapSettings()
        // Имитируем старый кеш
        settings.putString("cached_weather", """{"name":"Brest","main":{"temp":10.0,"humidity":70},"weather":[],"wind":{"speed":2.0}}""")

        val mockEngine = MockEngine { respond(content = "No Internet", status = HttpStatusCode.ServiceUnavailable) }
        val repository = WeatherRepository(WeatherApi(HttpClient(mockEngine)), settings)

        val cached = repository.getWeather("AnyCity")
        assertEquals("Brest", cached.name, "Должны вернуться данные из кеша")
    }

    /**
     * Тест поврежденного кеша.
     * Если JSON в кеше сломан, репозиторий должен просто загрузить данные из сети.
     */
    @Test
    fun testRepositoryWithInvalidJsonInCache() = runTest {
        val settings = MapSettings()
        settings.putString("cached_weather", "invalid json")

        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"name":"Minsk","main":{"temp":20.0,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val api = WeatherApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })
        val repository = WeatherRepository(api, settings)

        val result = repository.getWeather("Minsk")
        assertEquals("Minsk", result.name, "Должен загрузить из сети, игнорируя битый кеш")
    }
}