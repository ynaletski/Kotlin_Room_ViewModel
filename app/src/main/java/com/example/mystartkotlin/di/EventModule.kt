package com.example.mystartkotlin.di

import com.example.mystartkotlin.data.repository.EventRepository
import com.example.mystartkotlin.data.repository.HelloRepository
import com.example.mystartkotlin.data.repository.HelloRepositoryImpl
import com.example.mystartkotlin.data.repository.IEventRepository
import com.example.mystartkotlin.data.room.EventRoomDatabase
import com.example.mystartkotlin.presentation.viewmodel.EventViewModel
import com.example.mystartkotlin.presentation.viewmodel.HelloViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventModule = module {

    single<HelloRepository> { HelloRepositoryImpl() }

    viewModel { HelloViewModel(get()) }

    single { EventRoomDatabase.getDatabase(get(), CoroutineScope(SupervisorJob())) }

    single { get<EventRoomDatabase>().eventDao() }

    single<IEventRepository> { EventRepository(get()) }

    viewModel { EventViewModel(get()) }
}
