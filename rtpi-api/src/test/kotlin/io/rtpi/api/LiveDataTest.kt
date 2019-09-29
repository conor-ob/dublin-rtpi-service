package io.rtpi.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LiveDataTest {

    @Test
    fun `Luas live data with the same operator, route, destination and direction but different due times should have the same identifier`() {
        // arrange
        val luasLiveData = listOf(0, 5, 9, 17).map { createLuasLiveData(nextDueTime = it) }

        // act
        val identifier = luasLiveData.first().identifier

        // assert
        assertThat(luasLiveData.map { it.identifier }).containsExactly(
            identifier,
            identifier,
            identifier,
            identifier
        )
    }

    @Test
    fun `Irish Rail live data with the same operator, route, destination and direction but different due times should have the same identifier`() {
        // arrange
        val irishRailLiveData = listOf(1, 2, 6, 13, 23, 29).map { createLuasLiveData(nextDueTime = it) }

        // act
        val identifier = irishRailLiveData.first().identifier

        // assert
        assertThat(irishRailLiveData.map { it.identifier }).containsExactly(
            identifier,
            identifier,
            identifier,
            identifier,
            identifier,
            identifier
        )
    }
}