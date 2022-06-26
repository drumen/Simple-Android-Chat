package email.rumen.simpleandroidchat.ui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Singleton
    @Provides
    fun providesRealmDatabase(
        @ApplicationContext context: Context
    ): Realm {
        Realm.init(context)

        val realmConfiguration = RealmConfiguration
            .Builder()
            .name("muzz-realm")
            .allowQueriesOnUiThread(true)
            .compactOnLaunch()
            .build()

        return Realm.getInstance(realmConfiguration)
    }

}
