package com.jipariz.flatmanager.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jipariz.flatmanager.firebase.database.DatabaseService
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { DatabaseService(get()) }
}