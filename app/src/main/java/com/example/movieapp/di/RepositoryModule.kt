package com.example.movieapp.di

import com.example.movieapp.core.datastore.DataStoreRepository
import com.example.movieapp.core.datastore.DataStoreRepositoryImpl
import com.example.movieapp.core.network.repository.AuthRepository
import com.example.movieapp.core.network.repository.AuthRepositoryImpl
import com.example.movieapp.core.network.repository.MovieDetailRepository
import com.example.movieapp.core.network.repository.MovieDetailRepositoryImpl
import com.example.movieapp.core.network.repository.MovieRepository
import com.example.movieapp.core.network.repository.MovieRepositoryImpl
import com.example.movieapp.core.network.repository.SearchHistoryRepository
import com.example.movieapp.core.network.repository.SearchHistoryRepositoryImpl
import com.example.movieapp.core.network.repository.StorageRepository
import com.example.movieapp.core.network.repository.StorageRepositoryImpl
import com.example.movieapp.core.network.repository.WishListRepository
import com.example.movieapp.core.network.repository.WishListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSearchRepository(repository: MovieRepositoryImpl): MovieRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindMovieDetailRepository(repository: MovieDetailRepositoryImpl): MovieDetailRepository

    @Singleton
    @Binds
    abstract fun bindWishlistRepository(repository: WishListRepositoryImpl): WishListRepository

    @Singleton
    @Binds
    abstract fun bindSearchHistoryRepository(repository: SearchHistoryRepositoryImpl): SearchHistoryRepository

    @Singleton
    @Binds
    abstract fun bindDataStoreRepository(repository: DataStoreRepositoryImpl): DataStoreRepository

    @Singleton
    @Binds
    abstract fun bindDataStorageRepository(repository: StorageRepositoryImpl): StorageRepository
}