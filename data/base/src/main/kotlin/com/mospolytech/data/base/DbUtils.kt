package com.mospolytech.data.base

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.Column

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(value: ID, init: T.() -> Unit): T {
    val existed = findById(value)
    return existed?.apply(init) ?: new(value, init)
}