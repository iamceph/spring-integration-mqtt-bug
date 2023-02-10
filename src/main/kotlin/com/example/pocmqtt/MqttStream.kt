package com.example.pocmqtt

import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Component

@Component
class MqttStream {

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    fun handleMqttEvent() = MessageHandler {
        println(String(it.payload as ByteArray))
    }
}