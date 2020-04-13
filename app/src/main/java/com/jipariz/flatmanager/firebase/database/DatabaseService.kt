package com.jipariz.flatmanager.firebase.database

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DatabaseService() {
    val database = Firebase.firestore
    private val users: CollectionReference
        get() = database.collection("users")
    private val flats: CollectionReference
        get() = database.collection("flats")



    fun writeNewUser(userId: String, username: String?, email: String?) {
        val user = User(username, email)
        users.document(userId)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "User successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

    companion object {
        private const val TAG = "DatabaseService"
    }

}