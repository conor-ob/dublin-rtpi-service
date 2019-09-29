package io.rtpi.api

enum class Service(
    val fullName: String,
    val shortName: String,
    val operators: Set<Operator>
) {

    AIRCOACH("Aircoach", "AC", setOf(Operator.AIRCOACH)),
    BUS_EIREANN("Bus Éireann", "BE", setOf(Operator.BUS_EIREANN)),
    DUBLIN_BIKES("Dublin Bikes", "BIKE", setOf(Operator.DUBLIN_BIKES)),
    DUBLIN_BUS("Dublin Bus", "BAC", setOf(Operator.DUBLIN_BUS, Operator.GO_AHEAD)),
    IRISH_RAIL("Irish Rail", "IR", setOf(Operator.COMMUTER, Operator.DART, Operator.INTERCITY)),
    LUAS("Luas", "LUAS", setOf(Operator.LUAS)),
    SWORDS_EXPRESS("Swords Express", "SE", setOf(Operator.SWORDS_EXPRESS));

    override fun toString(): String {
        return fullName
    }

    companion object {

        fun parse(value: String): Service {
            for (operator in values()) {
                if (operator.name.equals(value, ignoreCase = true)
                    || operator.fullName.equals(value, ignoreCase = true)
                    || operator.shortName.equals(value, ignoreCase = true)) {
                    return operator
                }
            }
            throw IllegalArgumentException("Unable to parse Service from string value: $value")
        }

    }

}

enum class Operator(
    val fullName: String,
    val shortName: String
) {

    AIRCOACH("Aircoach", "AC"),
    BUS_EIREANN("Bus Éireann", "BE"),
    COMMUTER("Commuter", "COMM"),
    DART("DART", "DART"),
    DUBLIN_BIKES("Dublin Bikes", "BIKE"),
    DUBLIN_BUS("Dublin Bus", "BAC"),
    GO_AHEAD("Go Ahead", "GAD"),
    INTERCITY("InterCity", "ICTY"),
    LUAS("Luas", "LUAS"),
    SWORDS_EXPRESS("Swords Express", "SE");

    override fun toString(): String {
        return fullName
    }

    companion object {

        fun parse(value: String): Operator {
            for (operator in values()) {
                if (operator.name.equals(value, ignoreCase = true)
                    || operator.fullName.equals(value, ignoreCase = true)
                    || operator.shortName.equals(value, ignoreCase = true)) {
                    return operator
                }
            }
            throw IllegalArgumentException("Unable to parse Operator from string value: $value")
        }

    }

}
