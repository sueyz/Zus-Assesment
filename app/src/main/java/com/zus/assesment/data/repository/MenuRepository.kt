package com.zus.assesment.data.repository

import com.zus.assesment.data.mapper.MenuMapper
import com.zus.assesment.data.remote.MealApiService
import com.zus.assesment.domain.model.MenuCategory
import com.zus.assesment.domain.model.MenuItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepository @Inject constructor(
    private val api: MealApiService,
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
}
