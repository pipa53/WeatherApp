/**
 * @author Бельский Тимофей
 * @version 1.0
 * Модель данных для ответа от API погоды
 */
package com.example.lab9_project1_belskiy.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String,               // Название города
    val main: MainData,             // Основные данные (температура, влажность)
    val weather: List<WeatherDescription>, // Описание погоды (ясно, дождь и т.д.)
    val wind: WindData              // Данные о ветре
)

@Serializable
data class MainData(
    val temp: Double,      // Текущая температура в градусах Цельсия
    val humidity: Int      // Влажность в процентах
)

@Serializable
data class WeatherDescription(
    val main: String,        // Группа параметров (Rain, Snow, Extreme и т.д.)
    val description: String, // Описание погоды на русском языке
    val icon: String         // ID иконки для загрузки изображения с сервера
)

@Serializable
data class WindData(
    val speed: Double      // Скорость ветра в м/с
)
