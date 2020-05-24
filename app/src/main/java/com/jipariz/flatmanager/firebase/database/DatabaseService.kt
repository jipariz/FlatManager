package com.jipariz.flatmanager.firebase.database

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.iid.FirebaseInstanceId
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
        if (!snapshot.exists()) {
            users.document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "User successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
        getToken()
    }

    suspend fun writeFlat(name: String) {
        userId?.let {
            val data = Flat(
                name,
                listOf(
                    mapOf(
                        Pair("userId", it),
                        Pair("userName", auth.currentUser?.displayName ?: "")
                    )
                )
            )
            flats.document(data.flatId).set(data, SetOptions.merge()).await()
            assignFlatToUser(data.flatId)

        }

    }

    private fun getToken(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                userId?.let {
                    users.document(it).update(mapOf(Pair("token", task.result?.token)))
                }

            })
    }

    suspend fun assignFlatToUser(id: String) {
        userId?.let {
            users.document(it).update(mapOf(Pair("flatId", id))).await()
            flats.document(id).update("usersList", (FieldValue.arrayUnion(mapOf(
                Pair("userId", it),
                Pair("userName", auth.currentUser?.displayName ?: "")
            ))) )
        }
    }



    //    fun updateFlat(){
//        flatInternal = user.value?.flatId?.let { flats.document(it).get().result?.toObject<Flat>() }
//    }
//
    fun removeFlatFromUser(userId: String, flatId: String) {

        users.document(userId).update(mapOf(Pair("flatId", null)))
        flats.document(flatId).update("usersList", (FieldValue.arrayRemove(mapOf(
            Pair("userId", userId),
            Pair("userName", auth.currentUser?.displayName ?: "")
        ))) )

    }

    companion object {
        private const val TAG = "DatabaseService"
    }

}