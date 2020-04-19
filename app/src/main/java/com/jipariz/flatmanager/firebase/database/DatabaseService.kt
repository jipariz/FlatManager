package com.jipariz.flatmanager.firebase.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
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


    private var userInternal: User? = null
        set(value) {
            if (field == value) return
            field = value
            user.value = value
        }


    val flat =
        MutableLiveData<Flat?>().apply { value = flatInternal }


    private var flatInternal: Flat? = null
        set(value) {
            if (field == value) return
            field = value
            flat.value = value
        }


    val user =
        MutableLiveData<User?>().apply { value = userInternal }


    fun writeNewUser(userId: String, username: String?, email: String?) {
        val user = User(userId, username, email)
        users.document(userId)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "User successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

    suspend fun writeFlat(name:String){
        userId?.let {
            val data = Flat(name, listOf(it))
            flats.document(data.flatId).set(data, SetOptions.merge())
            assignFlatToUser(data.flatId)

        }

    }

     suspend fun assignFlatToUser(id: String){
        userId?.let {
            users.document(it).update(mapOf(Pair("flatId", id)))
            getFlat(id)
        }
    }

    fun updateFlat(){
        flatInternal = user.value?.flatId?.let { flats.document(it).get().result?.toObject<Flat>() }
    }

    fun removeFlatFromUser(){
        userId?.let {
            users.document(it).update(mapOf(Pair("flatId", null)))
            flatInternal = null
        }
    }

    suspend fun getUser(): User? {
        return try {
          val data = userId?.let { users.document(it).get().await().toObject<User>() }
            userInternal = data
            data
          }   catch (e: Exception){
            null
        }
    }

    suspend fun getFlat(id: String): Flat? {
        return try {
            val data = flats.document(id).get().await().toObject<Flat>()
            flatInternal = data
            data
        } catch (e:Exception){
            null
        }
    }

    companion object {
        private const val TAG = "DatabaseService"
    }

}