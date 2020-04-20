package com.jipariz.flatmanager.firebase.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class DatabaseService(val auth: FirebaseAuth) {
    val database = Firebase.firestore
    val users: CollectionReference
        get() = database.collection("users")
    val flats: CollectionReference
        get() = database.collection("flats")

    val userId: String?
        get() = auth.currentUser?.uid


    suspend fun writeNewUser(userId: String, username: String?, email: String?) {
        val user = User(userId, username, email)
        val snapshot = users.document(userId).get().await()
        if(!snapshot.exists()) {
            users.document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "User successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

    }

    suspend fun writeFlat(name:String){
        userId?.let {
            val data = Flat(name, listOf(it))
            flats.document(data.flatId).set(data, SetOptions.merge()).await()
            assignFlatToUser(data.flatId)

        }

    }

     suspend fun assignFlatToUser(id: String){
        userId?.let {
            users.document(it).update(mapOf(Pair("flatId", id))).await()
        }
    }

//    fun updateFlat(){
//        flatInternal = user.value?.flatId?.let { flats.document(it).get().result?.toObject<Flat>() }
//    }
//
//    fun removeFlatFromUser(){
//        userId?.let {
//            users.document(it).update(mapOf(Pair("flatId", null)))
//            flatInternal = null
//        }
//    }

    companion object {
        private const val TAG = "DatabaseService"
    }

}