package com.zus.assesment.data.repository

import com.squareup.moshi.Moshi
import com.zus.assesment.data.mapper.MenuMapper
import com.zus.assesment.data.remote.MealApiService
import com.zus.assesment.domain.model.MenuCategory
import com.zus.assesment.domain.model.MenuItem
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MenuRepository(
    private val api: MealApiService = defaultApi(),
) {
    suspend fun getCategories(): List<MenuCategory> {
        val response = api.getCategories()
        return response.categories.orEmpty().map(MenuMapper::toCategory)
    }

    suspend fun getItems(category: MenuCategory): List<MenuItem> {
        val response = api.getMealsByCategory(category.name)
        return response.meals.orEmpty().map { meal ->
            MenuMapper.toMenuItem(meal, category.id, category.name)
        }
    }

    companion object {
        private fun defaultApi(): MealApiService {
            val moshi = Moshi.Builder().build()
            return Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MealApiService::class.java)
        }
    }
}
