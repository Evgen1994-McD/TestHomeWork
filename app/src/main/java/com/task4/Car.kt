package com.task4

class Car private constructor(
    val name: String,
    val color: String,
    val price: Int,
    val maxSpeed: Int?,
    val country: String?,
    val muscle: Int?
) {

    class Builder(
        private val name: String,
        private val color: String,
        private val price: Int
    ) {
        private var maxSpeed: Int? = null
        private var country: String? = null
        private var muscle: Int? = null

        fun maxSpeed(maxSpeed: Int?) = apply { this.maxSpeed = maxSpeed }
        fun country(country: String?) = apply { this.country = country }
        fun muscle(muscle: Int?) = apply { this.muscle = muscle }
        fun buidl(): Car {
            return Car(name, color, price, maxSpeed, country, muscle)
        }

    }
}

val myTestCar = Car.Builder("Lada", "White", 100)
    .maxSpeed(150)
    .muscle(98)
    .country("Russia")
    .buidl()