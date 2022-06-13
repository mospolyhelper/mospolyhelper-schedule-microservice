package com.mospolytech.data.base

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.upsert(value: ID, init: T.() -> Unit): T {
    val existed = findById(value)
    return existed?.apply(init) ?: new(value, init)
}

fun <T : Entity<ID>, ID : Comparable<ID>> EntityClass<ID, T>.insertIfNotExist(
    op: SqlExpressionBuilder.() -> Op<Boolean>,
    init: T.() -> Unit
): T {
    val existed = find(op).firstOrNull()
    return existed ?: new(init)
}