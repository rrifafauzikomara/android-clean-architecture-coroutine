package io.fajarca.home.di

import dagger.Module
import dagger.Provides
import io.fajarca.core.database.CharacterDao
import io.fajarca.core.database.MarvelDatabase
import io.fajarca.core.di.scope.FeatureScope
import io.fajarca.home.data.CharactersMapper
import io.fajarca.home.domain.repository.CharacterRepository
import io.fajarca.home.domain.usecase.GetCharactersUseCase
import io.fajarca.home.ui.home.HomeViewModel


@Module
class HomeFeatureModule {

    @Provides
    @FeatureScope
    fun provideCharacterDao(db: MarvelDatabase) : CharacterDao = db.characterDao()

    @Provides
    @FeatureScope
    fun provideMapper() : CharactersMapper = CharactersMapper()

    @Provides
    @FeatureScope
    fun provideUseCase(repository: CharacterRepository) : GetCharactersUseCase = GetCharactersUseCase(repository)

    @Provides
    @FeatureScope
    fun provideViewModel(useCase: GetCharactersUseCase) : HomeViewModel = HomeViewModel(useCase)
}