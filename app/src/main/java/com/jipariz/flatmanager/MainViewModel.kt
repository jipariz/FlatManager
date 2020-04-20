package com.jipariz.flatmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import com.jipariz.flatmanager.firebase.database.DatabaseService
import com.jipariz.flatmanager.firebase.database.Flat
import com.jipariz.flatmanager.firebase.database.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MainViewModel(val databaseService: DatabaseService) : ViewModel() {

    fun getData() {
        fetchUser()
    }


    private var state = PageState()
        set(value) {
            if (field == value) return
            field = value
            stateInternal.value = value
        }
    private val stateInternal = MutableLiveData<PageState>().apply { value = state }
    val liveState: LiveData<PageState> = stateInternal

    val userId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid


    fun fetchUser() {
        try {
            userId?.let {
                databaseService.users.document(it).get().addOnSuccessListener { snapshot ->
                    val user = snapshot.toObject<User?>()
                    state = state.copy(
                        user = user
                    )
                    fetchFlat()
                }
            }

        } catch (e: Exception) {
        }
    }

    fun fetchFlat() {
        try {
            state.user?.flatId?.let {
                databaseService.flats.document(it).get().addOnSuccessListener { snapshot ->
                    val flat = snapshot.toObject<Flat?>()
                    state = state.copy(
                        flat = flat
                    )
                    viewModelScope.launch {
                        fetchFlatmatesNames()
                    }

                }
            }
        } catch (e: Exception) {
        }
    }

    suspend fun fetchFlatmatesNames() {
        val flatmatesList = state.flat?.usersList?.map {
            databaseService.users.document(it).get().await().toObject<User>()?.name ?: "FlatMate"
        } ?: emptyList()
        state = state.copy(
            flatmates = flatmatesList
        )

    }

    fun createNewFlat(name: String) {
        viewModelScope.launch {
            databaseService.writeFlat(name)
            fetchUser()
        }

    }
}

data class PageState(
    val user: User? = null,
    val flat: Flat? = null,
    val loading: Boolean? = false,
    val flatmates: List<String>? = null
)