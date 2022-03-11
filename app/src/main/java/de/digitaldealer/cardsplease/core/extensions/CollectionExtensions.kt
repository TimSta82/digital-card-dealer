package de.digitaldealer.cardsplease.extensions

fun <T> List<T>.second(): T {
    if (isEmpty()) {
        throw NoSuchElementException("List is empty.")
    }
    return this[1]
}

fun <T> List<T>.replace(index: Int, new: T): List<T> = this.mapIndexed { i, item -> if (i == index) new else item }
