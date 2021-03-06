package io.fajarca.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
import io.fajarca.core.database.NewsDatabase
import io.fajarca.core.di.modules.*
import io.fajarca.core.dispatcher.DispatcherProvider
import retrofit2.Retrofit
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ContextModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        SharedPreferenceModule::class,
        CoroutineDispatcherModule::class
    ]
)
interface CoreComponent {
    fun context() : Context
    fun sharedPreference() : SharedPreferences
    fun sharedPreferenceEditor() : SharedPreferences.Editor
    fun marvelDatabase() : NewsDatabase
    fun retrofit() : Retrofit
    fun dispatcher() : DispatcherProvider


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): CoreComponent
    }
}