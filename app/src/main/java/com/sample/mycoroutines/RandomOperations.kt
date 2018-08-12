package com.sample.mycoroutines

import java.util.*

object RandomOperations {
    private val random = Random()

    fun getRandomNumber(): Int {
        println(">>>>Starting getting random NUMBER")
        val operationTimeSecs = 1 + random.nextInt(4)
        val startTime = System.currentTimeMillis()
        var number = 0
        while (System.currentTimeMillis() < startTime + operationTimeSecs * 1000) {
            number = random.nextInt(100)
        }
        return number
    }

    fun getRandomName(i: Int): String {
        println(">>>>Starting getting random NAME")
        val operationTimeSecs = 1 + random.nextInt(4)
        val startTime = System.currentTimeMillis()
        val namesList = listOf("Alice", "Bob", "Charlie", "Dean", "Eve", "Felix")
        var name = ""
        while (System.currentTimeMillis() < startTime + operationTimeSecs * 1000) {
            name = namesList[(random.nextInt(i * i) + i) % namesList.size]
        }
        return name
    }


    fun getRandomFruit(s: String): String {
        println(">>>>Starting getting random FRUIT")
        val operationTimeSecs = 1 + random.nextInt(4)
        val startTime = System.currentTimeMillis()
        val fruitsList = listOf("Apple", "Banana", "Orange", "Melon", "Pumpkin", "Mango")
        var fruit = "Carrot"
        while (System.currentTimeMillis() < startTime + operationTimeSecs * 1000) {
            fruit = fruitsList[(random.nextInt(fruitsList.size) + s.length) % fruitsList.size]
        }
        return fruit
    }
}