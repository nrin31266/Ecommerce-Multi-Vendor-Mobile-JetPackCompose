package com.nrin31266.ecommercemultivendor.di
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import com.nrin31266.ecommercemultivendor.domain.repo.RepoImpl
import com.nrin31266.ecommercemultivendor.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Singleton
    @Provides
    fun provideRepo(apiService: ApiService): Repo {
        return RepoImpl(apiService)
    }
}