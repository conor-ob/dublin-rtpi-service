package io.rtpi.util

import io.rtpi.test.fixtures.createRouteInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RouteComparatorTest {

    @Test
    fun `routes should be sorted alphanumerically`() {
        // arrange
        val routes = listOf(
            createRouteInfo(route = "46E"),
            createRouteInfo(route = "155"),
            createRouteInfo(route = "7B"),
            createRouteInfo(route = "84X"),
            createRouteInfo(route = "118"),
            createRouteInfo(route = "7D"),
            createRouteInfo(route = "116"),
            createRouteInfo(route = "46A"),
            createRouteInfo(route = "145")
        )

        // act
        val sorted = routes.sortedWith(RouteComparator)

        // assert
        assertThat(sorted.map { it.route }).containsExactly(
            "7B",
            "7D",
            "46A",
            "46E",
            "84X",
            "116",
            "118",
            "145",
            "155"
        )
    }
}
