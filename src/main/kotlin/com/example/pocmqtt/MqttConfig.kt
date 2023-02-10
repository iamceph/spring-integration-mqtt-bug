package com.example.pocmqtt

import org.eclipse.paho.mqttv5.client.IMqttAsyncClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.core.ClientManager
import org.springframework.integration.mqtt.core.Mqttv5ClientManager
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter
import org.springframework.messaging.MessageChannel


@Configuration
class MqttConfig {

    companion object {
        private val URL = "tcp://localhost:1883"
        private val CLIENT = "TEST"
        private val TOPIC = "whoknows"

    }

    @EventListener(MqttConnectionFailedEvent::class)
    fun test() {
        print("KOKOOOOT")
    }

    @Bean
    fun mqttInputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun clientManager(): ClientManager<IMqttAsyncClient, MqttConnectionOptions> {
        val connectionOptions = MqttConnectionOptions()
        connectionOptions.serverURIs = arrayOf(URL)

        connectionOptions.connectionTimeout = 3000
        connectionOptions.maxReconnectDelay = 1000
        connectionOptions.isAutomaticReconnect = true
        connectionOptions.isCleanStart = false

        return Mqttv5ClientManager(connectionOptions, CLIENT)
    }

    @Bean
    fun inbound(): MessageProducer {
        val manager = clientManager()

        val adapter = Mqttv5PahoMessageDrivenChannelAdapter(
            manager,
            TOPIC
        )

        adapter.setCompletionTimeout(1000)
        adapter.setPayloadType(String::class.java)
        adapter.setQos(0)
        adapter.outputChannel = mqttInputChannel()

        manager.addCallback {
            if (it && !adapter.connectionInfo.isCleanStart) {
                adapter.connectComplete(false)
            }
        }

        return adapter
    }
}