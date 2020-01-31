package io.fajarca.home.domain.usecase

import io.fajarca.home.domain.entities.News
import io.fajarca.home.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(private val repository: NewsRepository) {

    suspend fun execute(): List<News> {
        return repository.getNews().take(5)
    }

}