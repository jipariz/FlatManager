package com.jipariz.flatmanager.di

import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.MainViewModel
import com.jipariz.flatmanager.firebase.database.DatabaseService
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { DatabaseService(get()) }

    single { MainViewModel(get()) }
}