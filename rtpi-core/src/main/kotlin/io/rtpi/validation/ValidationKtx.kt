package io.rtpi.validation

fun String?.validate(): String = requireNotNull(this).trim()

fun Double?.validate(): Double = requireNotNull(this)

fun Int?.validate(): Int = requireNotNull(this)

fun <T> Collection<T>?.validate(): Collection<T> = requireNotNull(this)

fun validateStrings(vararg strings: String?): Boolean {
    for (string in strings) {
        if (string.isNullOrBlank()) {
            return false
        }
    }
    return true
}

fun validateDoubles(vararg doubles: Double?): Boolean {
    for (double in doubles) {
        if (double == null || double == 0.0) {
            return false
        }
    }
    return true
}

fun <T> validateCollection(collection: Collection<T>?): Boolean {
    return collection != null
}

fun validateObjects(vararg objects: Any?): Boolean {
    for (thing in objects) {
        if (thing == null) {
            return false
        }
    }
    return true
}
