package com.ltu.m7019e.forktales.database

import com.ltu.m7019e.forktales.model.Category
import com.ltu.m7019e.forktales.network.ForkTalesApiService

/**
 * An interface that defines the contract for a categories repository.
 * It has a single function that fetches a list of categories.
 */
interface CategoriesRepository {
    /**
     * A suspend function that fetches a list of categories.
     *
     * @return A list of Category objects.
     */
    suspend fun getCategories(): List<Category>
}

/**
 * A class that implements the CategoriesRepository interface.
 * It uses a ForkTalesApiService to fetch the categories from the network.
 *
 * @property apiService The ForkTalesApiService used to fetch the categories.
 */
class NetworkCategoriesRepository(
    private val apiService: ForkTalesApiService
) : CategoriesRepository {
    /**
     * A suspend function that fetches a list of categories from the network.
     *
     * @return A list of Category objects.
     */
    override suspend fun getCategories(): List<Category> {
        return apiService.getCategories().categories
    }
}