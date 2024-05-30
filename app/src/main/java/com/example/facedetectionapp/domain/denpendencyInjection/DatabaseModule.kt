package com.example.facedetectionapp.domain.denpendencyInjection

import android.content.Context
import androidx.room.Room
import com.example.facedetectionapp.data.local.Database
import com.example.facedetectionapp.data.local.PersonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "person_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesPersonDao(database: Database): PersonDao {
        return database.personDao()
    }
}