package bob.colbaskin.cookly.common.utils

fun String.getFirstLetter(): String {
   return this.firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "П"
}
