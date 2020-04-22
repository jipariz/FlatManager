package com.jipariz.flatmanager.firebase.database

class Flat {
    val flatId: String = List(4) { alphabet.random() }.joinToString("")
    var name: String? = null
    var usersList: List<Map<String, String>>? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(name: String, list: List<Map<String, String>>) {
        this.name = name
        this.usersList = list
    }

    companion object {
        // Descriptive alphabet using three CharRange objects, concatenated
        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }
}