package com.example.pocmqtt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PocMqttApplication

fun main(args: Array<String>) {
    runApplication<PocMqttApplication>(*args)
}
