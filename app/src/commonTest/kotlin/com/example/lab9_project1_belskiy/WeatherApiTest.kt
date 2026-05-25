/**
 * @author Бельский Тимофей
 * @version 1.0
 * Тесты для API погоды (WeatherApi).
 * Проверяют правильность формирования URL-запросов и обработку сетевых ошибок.
 */
package com.example.lab9_project1_belskiy

import com.example.lab9_project1_belskiy.network.WeatherApi
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherApiTest {

    /**
     * Тест параметров URL.
     * Проверяет, что API-клиент передает правильный хост, путь и параметры (город, единицы).
     */
    @Test
    fun testUrlParameters() = runTest {
        val mockEngine = MockEngine { request ->
            val url = request.url
            assertEquals("api.openweathermap.org", url.host)
            assertEquals("/data/2.5/weather", url.encodedPath)
            assertEquals("Minsk", url.parameters["q"])

            respond(
                content = """{"name":"Minsk","main":{"temp":20.5,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val api = WeatherApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })

        api.fetchWeather("Minsk")
    }

    /**
     * Тест обработки сетевых ошибок (например, HTTP 500).
     */
    @Test
    fun testNetworkErrorHandling() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(content = "Error", status = HttpStatusCode.InternalServerError)
        }

        val api = WeatherApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        })

        try {
            api.fetchWeather("City")
        } catch (_: Exception) {
            // Ожидаем, что возникнет ошибка
        }
    }

    /**
     * Тест корректности десериализации ответа.
     */
    @Test
    fun testDeserialization() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"name":"London","main":{"temp":10.0,"humidity":80},"weather":[{"main":"Clouds","description":"облачно","icon":"04d"}],"wind":{"speed":5.5}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val api = WeatherApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })
        
        val result = api.fetchWeather("London")
        assertEquals("London", result.name)
        assertEquals(10.0, result.main.temp)
        assertEquals("облачно", result.weather[0].description)
    }
}
