package io.fajarca.home.data

import io.fajarca.home.data.mapper.NewsMapper
import io.fajarca.home.data.source.NewsRemoteDataSource
import io.fajarca.core.database.TopHeadlineDao
import io.fajarca.core.network.HttpResult
import io.fajarca.home.util.TestUtil
import io.fajarca.home.util.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import io.fajarca.core.vo.Result


@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var dao: TopHeadlineDao
    @Mock
    lateinit var mapper: NewsMapper
    @Mock
    lateinit var detailMapper : CharacterDetailMapper
    @Mock
    lateinit var seriesMapper : CharacterSeriesMapper
    @Mock
    lateinit var remoteDataSource: NewsRemoteDataSource

    private lateinit var repository: NewsRepositoryImpl


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        repository = NewsRepositoryImpl(
            provideFakeCoroutinesDispatcherProvider(testCoroutineDispatcher),
            mapper,
            detailMapper,
            seriesMapper,
            dao,
            remoteDataSource
        )
    }


    @Test
    fun `when inserting new characters, should call dao insert method`() = runBlockingTest {
        repository.insertAllCharacter(emptyList())
        verify(dao).insertAll(emptyList())
    }


    @Test
    fun `when get all characters from db, should invoke dao find all method`() = runBlockingTest {
        repository.getAllCharactersFromDb()
        verify(dao).findAll()
    }

    @Test
    fun `when get all characters from remote is success, should insert the characters to db`() =
        runBlockingTest {
            //Given
            val characters = TestUtil.generateDummyCharacters(numOfIteration = 0)
            val response = Result.Success(CharacterDto())

            `when`(remoteDataSource.getCharacters(testCoroutineDispatcher)).thenReturn(response)

            //When
            repository.getAllCharacter()

            //Then
            verify(dao).insertAll(characters)
        }

    @Test
    fun `when get all characters from remote is error, insert character to db won't executed`() =
        runBlockingTest {
            //Given
            val characters = TestUtil.generateDummyCharacters(numOfIteration = 5)
            val response = Result.Error(HttpResult.NO_CONNECTION)

            `when`(remoteDataSource.getCharacters(testCoroutineDispatcher)).thenReturn(response)

            //When
            repository.getAllCharacter()

            //Then
            verify(dao, never()).insertAll(characters)
        }

    @Test
    fun `when get list of series from a character, should fetch from network`() = runBlockingTest {
        //Given
        val characterId = 409

        //When
        repository.getCharacterSeries(characterId)

        //Then
        verify(remoteDataSource).getCharacterSeries(characterId, testCoroutineDispatcher)
    }


    @Test
    fun `when get detail of a character, should fetch from network`() = runBlockingTest {
        //Given
        val characterId = 409

        //When
        repository.getCharacterDetail(characterId)

        //Then
        verify(remoteDataSource).getCharacterDetail(characterId, testCoroutineDispatcher)
    }

}