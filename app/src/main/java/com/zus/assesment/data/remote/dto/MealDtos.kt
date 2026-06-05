package com.zus.assesment.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesResponse(
    val categories: List<CategoryDto>?,
)

@JsonClass(generateAdapter = true)
data class CategoryDto(
    @param:Json(name = "idCategory") val idCategory: String,
    @param:Json(name = "strCategory") val strCategory: String,
    @param:Json(name = "strCategoryThumb") val strCategoryThumb: String,
    @param:Json(name = "strCategoryDescription") val strCategoryDescription: String,
)

@JsonClass(generateAdapter = true)
data class MealsResponse(
    val meals: List<MealSummaryDto>?,
)

@JsonClass(generateAdapter = true)
data class MealSummaryDto(
    @param:Json(name = "idMeal") val idMeal: String,
    @param:Json(name = "strMeal") val strMeal: String,
    @param:Json(name = "strMealThumb") val strMealThumb: String,
    @param:Json(name = "strArea") val strArea: String?,
    @param:Json(name = "strCountry") val strCountry: String?,
)
