package io.rtpi.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AlphaNumericComparatorTest {

    @Test
    fun `strings should be sorted alphanumerically`() {
        // arrange
        val strings = listOf(
            "46A",
            "110",
            "X102",
            "10",
            "hello world",
            "Commuter"
        )

        // act
        val sorted = strings.sortedWith(AlphaNumericComparator)

        // assert
        assertThat(sorted).containsExactly(
            "10",
            "46A",
            "110",
            "Commuter",
            "X102",
            "hello world"
        )
    }
}
