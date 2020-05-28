package io.rtpi.util

import io.rtpi.api.RouteInfo

object RouteComparator : Comparator<RouteInfo> {

    override fun compare(r1: RouteInfo, r2: RouteInfo): Int = AlphaNumericComparator.compare(r1.route, r2.route)
}

object AlphaNumericComparator : Comparator<String> {

    override fun compare(s1: String, s2: String): Int {
        var thisMarker = 0
        var thatMarker = 0
        // Cache their lengths to avoid looking this up later.
        val s1Length = s1.length
        val s2Length = s2.length

        // Keep looping until the end of either String is reached.
        while (thisMarker < s1Length && thatMarker < s2Length) {
            val thisChunk = getChunk(s1, thisMarker)
            thisMarker += thisChunk.length

            val thatChunk = getChunk(s2, thatMarker)
            thatMarker += thatChunk.length

            // If both chunks contain numeric characters, sort them numerically.
            var result: Int
            if (Character.isDigit(thisChunk[0]) && Character.isDigit(thatChunk[0])) {
                // Simple chunk comparison by length.
                val thisChunkLength = thisChunk.length
                result = thisChunkLength - thatChunk.length
                // If equal, the first different number counts.
                if (result == 0) {
                    for (i in 0 until thisChunkLength) {
                        result = thisChunk[i] - thatChunk[i]
                        if (result != 0) return result
                    }
                }
            } else {
                result = thisChunk.compareTo(thatChunk)
            }

            if (result != 0) return result
        }

        return s1Length - s2Length
    }

    private fun getChunk(s: String, markerTemp: Int): String {
        var marker = markerTemp

        // Cache the character array to avoid repeated calls to String.charAt()
        val chars = s.toCharArray()
        val len = chars.size
        require(!(marker < 0 || marker > len - 1)) { "marker is invalid." }

        val chunk = StringBuilder()
        // The first character will always appear in the chunk.
        chunk.append(chars[marker])
        marker++

        if (Character.isDigit(chars[marker - 1])) {
            // If first character is a digit, keep appending characters until we
            // encounter a non-digit.
            while (marker < len) {
                if (!Character.isDigit(chars[marker])) break
                chunk.append(chars[marker])
                marker++
            }
        } else {
            // If first character is a non-digit, keep appending character until
            // we encounter a digit.
            while (marker < len) {
                if (Character.isDigit(chars[marker])) break
                chunk.append(chars[marker])
                marker++
            }
        }

        return chunk.toString()
    }
}
