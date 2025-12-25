package com.task4

import android.util.Log

class Car_with_abstract(
    val name: String,
    val color: String,
    val price: Int,
    factory: ParametersFactory
) {
    val country:Country = factory.createCountry()
    val maxSpeed:MaxSpeed=factory.createMaxSpeed()
    fun describe(){
        Log.d("Car", "$name")
        Log.d("Car", "$color")
        Log.d("Car", "$price")
        Log.d("Car", "${country.description()}")
        Log.d("Car", "${maxSpeed.description()}")
    }

}

interface Country{
    val country:String
    fun description(): String
}


interface MaxSpeed{
    val maxSpeed:Int
    fun description(): String
}

interface ParametersFactory{
    fun createMaxSpeed():MaxSpeed
    fun createCountry():Country
}

class RussiaCountry:Country{
    override val country: String = "Russia"

    override fun description() :String {
       return "Страна, выпустившая авто: $country"
    }

}

class RussiaSuperCarMaxSpeed:MaxSpeed{
    override val maxSpeed: Int = 200

    override fun description():String {
     return "Текущая максимальная скорость равна $maxSpeed"
    }

}


class RussiaSuperSpeedRacerFactory:ParametersFactory{
    override fun createMaxSpeed(): MaxSpeed = RussiaSuperCarMaxSpeed()

    override fun createCountry(): Country = RussiaCountry()
}