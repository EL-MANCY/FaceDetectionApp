package com.example.facedetectionapp.domain.denpendencyInjection

import com.example.facedetectionapp.data.repositoriesImpl.PersonRepositoryImpl
import com.example.facedetectionapp.domain.repositories.PersonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePersonRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository = personRepositoryImpl
}








