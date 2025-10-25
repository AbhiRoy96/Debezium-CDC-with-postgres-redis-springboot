package com.travelerinsider.oahu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "inventory.public.customers", groupId = "cdc-group")
    public void consume(String message) {
        try {
            JsonNode json = mapper.readTree(message);
            JsonNode after = json.path("payload").path("after");

            if (!after.isMissingNode()) {
                String id = after.path("id").asText();
                redisTemplate.opsForHash().put("customers_cache", id, after.toString());
                System.out.println("✅ Cached customer " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
