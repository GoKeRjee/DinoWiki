package fr.uha.hassenforder.team.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.database.MoabDao
import fr.uha.hassenforder.team.database.TeamDao
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.repository.DinoRepository
import fr.uha.hassenforder.team.repository.MoabRepository
import fr.uha.hassenforder.team.repository.TeamRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = TeamDatabase.create(appContext)

    @Singleton
    @Provides
    fun provideDinoDao(db: TeamDatabase) = db.dinoDao

    @Singleton
    @Provides
    fun provideMoabDao(db: TeamDatabase): MoabDao = db.moabDao


    @Singleton
    @Provides
    fun provideTeamDao(db: TeamDatabase) = db.teamDao

    @Singleton
    @Provides
    fun provideDinoRepository(
//        dispatcher: CoroutineDispatcher,
        dinoDao: DinoDao
    ) = DinoRepository(dinoDao)

    @Singleton
    @Provides
    fun provideTeamRepository(
//        dispatcher: CoroutineDispatcher,
        teamDao: TeamDao,
        dinoDao: DinoDao
    ) = TeamRepository(teamDao, dinoDao)

    @Singleton
    @Provides
    fun provideMoabRepository(
        moabDao: MoabDao,
        dinoDao: DinoDao
    ) = MoabRepository(moabDao, dinoDao)
}
