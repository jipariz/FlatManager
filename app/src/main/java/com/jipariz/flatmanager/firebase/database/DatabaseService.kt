package com.jipariz.flatmanager.firebase.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception


class DatabaseService(val auth: FirebaseAuth) {
    val database = Firebase.firestore
    private val users: CollectionReference
        get() = database.collection("users")
    private val flats: CollectionReference
        get() = database.collection("flats")

    val userId: String?
        get() = auth.currentUser?.uid


    fun writeNewUser(userId: String, username: String?, email: String?) {
        val user = User(username, email)
        users.document(userId)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "User successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }


    suspend fun getUser(): User? {
        return try {
          val data = userId?.let { database.document(it).get().await().toObject<User>() }
            data
          }   catch (e: Exception){
            null
        }


    }

    companion object {
        private const val TAG = "DatabaseService"
    }

}