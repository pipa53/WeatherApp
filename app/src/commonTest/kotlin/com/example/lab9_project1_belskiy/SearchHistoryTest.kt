/**
 * @author Бельский Тимофей
 * @version 1.0
 * Тесты для проверки истории поиска в репозитории (Search History).
 * Проверяют добавление городов в список, ограничение размера истории и обработку дубликатов.
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

class SearchHistoryTest {

    /**
     * Вспомогательный метод для создания HTTP-клиента с поддержкой JSON.
     */
    private fun createMockClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    /**
     * Тест добавления города в историю.
     * Проверяет, что после запроса погоды город корректно добавляется в начало списка.
     */
    @Test
    fun testAddToHistory() = runTest {
        val mockEngine = MockEngine { request ->
            val city = request.url.parameters["q"] ?: "Unknown"
            respond(
                content = """{"name":"$city","main":{"temp":20.5,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = createMockClient(mockEngine)
        val settings = MapSettings()
        val repository = WeatherRepository(WeatherApi(client), settings)

        repository.getWeather("Minsk")
        assertEquals(listOf("Minsk"), repository.searchHistory.value, "Минск должен быть в истории")

        repository.getWeather("Moscow")
        assertEquals(listOf("Moscow", "Minsk"), repository.searchHistory.value, "Москва должна стать первой")
    }

    /**
     * Тест лимита истории поиска.
     * Проверяет, что история хранит не более 5 последних запросов.
     */
    @Test
    fun testHistoryLimit() = runTest {
        val mockEngine = MockEngine { request ->
            val city = request.url.parameters["q"] ?: "Unknown"
            respond(
                content = """{"name":"$city","main":{"temp":20.5,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val settings = MapSettings()
        val repository = WeatherRepository(WeatherApi(createMockClient(mockEngine)), settings)

        // Запрашиваем 6 разных городов
        val cities = listOf("City1", "City2", "City3", "City4", "City5", "City6")
        cities.forEach { repository.getWeather(it) }

        // Должно остаться 5, и City6 — первый
        assertEquals(5, repository.searchHistory.value.size)
        assertEquals("City6", repository.searchHistory.value.first())
    }

    /**
     * Тест обработки дубликатов.
     * Повторный поиск города должен переместить его в начало, а не дублировать.
     */
    @Test
    fun testDuplicateToHistory() = runTest {
        val mockEngine = MockEngine { request ->
            val city = request.url.parameters["q"] ?: "Unknown"
            respond(
                content = """{"name":"$city","main":{"temp":20.5,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val settings = MapSettings()
        val repository = WeatherRepository(WeatherApi(createMockClient(mockEngine)), settings)

        repository.getWeather("City1")
        repository.getWeather("City2")
        repository.getWeather("City1") // Снова City1

        assertEquals(2, repository.searchHistory.value.size)
        assertEquals("City1", repository.searchHistory.value.first())
    }
}