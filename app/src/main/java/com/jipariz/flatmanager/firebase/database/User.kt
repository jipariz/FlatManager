package com.jipariz.flatmanager.firebase.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {
    var userId: String? = null
    var name: String? = null
    var email: String? = null
    var flatId: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(userId: String?, name: String?, email: String?) {
        this.userId = userId
        this.name = name
        this.email = email
    }
}