package app.mybad.network.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseSignInClient {

    @Singleton
    @Provides
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context
    ) = Identity.getSignInClient(context)

    @Singleton
    @Provides
    fun provideFirebase() = Firebase
}

