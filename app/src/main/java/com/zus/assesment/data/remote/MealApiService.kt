package com.zus.assesment.data.remote

import com.zus.assesment.data.remote.dto.CategoriesResponse
import com.zus.assesment.data.remote.dto.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String,
    ): MealsResponse
}
