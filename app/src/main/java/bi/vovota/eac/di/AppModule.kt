package bi.vovota.eac.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import bi.vovota.eac.data.repo.AuthRepo
import bi.vovota.eac.data.repo.AuthRepositoryImpl
import bi.vovota.eac.data.TokenManager
import bi.vovota.eac.data.remote.ApiService
import bi.vovota.eac.data.repo.ProductRepo
import bi.vovota.eac.data.repo.ProductRepoImpl
import bi.vovota.eac.data.repo.ProfileRepo
import bi.vovota.eac.data.repo.ProfileRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("auth_prefs")}
        )
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: HttpClient): ApiService = ApiService(client)

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService): AuthRepo = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideTokenManager(dataStore: DataStore<Preferences>): TokenManager {
        return TokenManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideProfile(api: ApiService): ProfileRepo = ProfileRepoImpl(api)

    @Provides
    @Singleton
    fun provideProductRepo(api: ApiService): ProductRepo = ProductRepoImpl(api)
}
