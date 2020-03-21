package io.rtpi.validation

fun String?.validate(): String = requireNotNull(this).trim()

fun validateStrings(vararg strings: String?): Boolean {
    for (string in strings) {
        if (string.isNullOrBlank()) {
            return false
        }
    }
    return true
}
