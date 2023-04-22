package com.clean.architecture.data.types.order

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}