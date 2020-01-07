package io.fajarca.home.data.repository

import io.fajarca.core.common.CoroutineDispatcherProvider
import io.fajarca.home.data.mapper.CharactersMapper
import io.fajarca.home.data.source.remote.CharacterRemoteDataSource
import io.fajarca.core.common.Result
import io.fajarca.home.domain.repository.CharacterRepository
import io.fajarca.core.database.CharacterDao
import kotlinx.coroutines.flow.Flow
import io.fajarca.core.database.Character
import javax.inject.Inject
import io.fajarca.core.network.CharacterDto

class CharacterRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcherProvider,
    private val mapper: CharactersMapper,
    private val dao: CharacterDao,
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun insertAllCharacter(characters: List<Character>) {
        dao.insertAll(characters)
    }

    override suspend fun getAllCharactersFromDb(): Flow<List<Character>> {
        return dao.findAll()
    }

    override suspend fun getAllCharacter(): Flow<List<Character>> {
        val apiResult = getAllCharactersFromApi()

        if (apiResult is Result.Success) {
            insertAllCharacter(mapper.map(apiResult.data))
        }

        return dao.findAll()
    }


    override suspend fun getAllCharactersFromApi(): Result<CharacterDto> {
        return remoteDataSource.getCharacters(dispatcher.io)
    }


}