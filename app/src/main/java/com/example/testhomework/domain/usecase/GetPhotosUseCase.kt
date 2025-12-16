package com.example.testhomework.domain.usecase

import com.example.testhomework.domain.model.PhotosPage
import com.example.testhomework.domain.repository.PhotosRepository

/**
 * Use Case для получения фотографий с пагинацией
 */
class GetPhotosUseCase(
    private val repository: PhotosRepository
) : UseCase<GetPhotosUseCase.Params, PhotosPage>() {

    /**
     * Параметры для получения фотографий
     */
    data class Params(
        val page: Int,
        val pageSize: Int
    )

    override suspend fun execute(params: Params): PhotosPage {
        return repository.searchPhotos(
            page = params.page,
            pageSize = params.pageSize
        ).getOrThrow()
    }
}
