package com.jipariz.flatmanager

import android.util.Log
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
        //fetchUser()
        userListener()
    }

    fun userListener() {
        userId?.let {
            val docRef = databaseService.users.document(it)
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("", "Current data: ${snapshot.data}")
                    val user = snapshot.toObject<User?>()
                    state = state.copy(
                        user = user
                    )
                    user?.flatId?.let { flatId ->
                        flatListener(flatId)
                    }
                } else {
                    Log.d("", "Current data: null")
                }
            }
        }
    }

    fun flatListener(id: String) {
        try {
            state.user?.flatId?.let {
                val dockRef = databaseService.flats.document(it)
                dockRef.addSnapshotListener { snapshot, e ->

                    if (e != null) {
                        Log.w("", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("", "Current data: ${snapshot.data}")
                        val flat = snapshot.toObject<Flat?>()
                        state = state.copy(
                            flat = flat
                        )
                    } else {
                        Log.d("", "Current data: null")
                    }

                }
            }
        } catch (e: Exception) {
        }
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


    fun createNewFlat(name: String) {
        viewModelScope.launch {
            databaseService.writeFlat(name)
        }

    }
}

data class PageState(
    val user: User? = null,
    val flat: Flat? = null,
    val loading: Boolean? = false
)