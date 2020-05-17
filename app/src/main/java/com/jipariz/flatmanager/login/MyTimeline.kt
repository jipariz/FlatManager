package com.jipariz.flatmanager.login

import me.jerryhanks.timelineview.model.Status
import me.jerryhanks.timelineview.model.TimeLine

class MyTimeLine(status: Status, var name: String?) : TimeLine(status) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyTimeLine

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "MyTimeLine(name=$name)"
    }


}