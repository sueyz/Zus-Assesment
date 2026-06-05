package com.zus.assesment.data.mapper

import com.zus.assesment.data.remote.dto.CategoryDto
import com.zus.assesment.data.remote.dto.MealSummaryDto
import com.zus.assesment.domain.model.MenuCategory
import com.zus.assesment.domain.model.MenuItem
import kotlin.math.absoluteValue

object MenuMapper {
    fun toCategory(dto: CategoryDto): MenuCategory = MenuCategory(
        id = dto.idCategory,
        name = dto.strCategory,
        thumbnailUrl = dto.strCategoryThumb,
        description = dto.strCategoryDescription.trim(),
    )

    fun toMenuItem(dto: MealSummaryDto, categoryId: String, categoryName: String): MenuItem {
        val area = dto.strArea?.takeIf { it.isNotBlank() }
        val country = dto.strCountry?.takeIf { it.isNotBlank() }
        val description = buildDescription(area, country, categoryName)
        val seed = dto.idMeal.toIntOrNull() ?: dto.idMeal.hashCode()

        return MenuItem(
            id = dto.idMeal,
            categoryId = categoryId,
            name = dto.strMeal,
            description = description,
            imageUrl = dto.strMealThumb,
            price = simulatedPrice(seed),
            isAvailable = seed % 9 != 0,
            tags = buildTags(area, country),
        )
    }

    private fun buildDescription(area: String?, country: String?, categoryName: String): String {
        return when {
            area != null && country != null -> "$categoryName specialty from $area, $country."
            country != null -> "Classic $categoryName dish from $country."
            else -> "A handcrafted $categoryName favourite."
        }
    }

    private fun buildTags(area: String?, country: String?): List<String> {
        return listOfNotNull(area, country).take(2)
    }

    private fun simulatedPrice(seed: Int): Double {
        val cents = (seed.absoluteValue % 2000) + 690
        return cents / 100.0
    }
}
