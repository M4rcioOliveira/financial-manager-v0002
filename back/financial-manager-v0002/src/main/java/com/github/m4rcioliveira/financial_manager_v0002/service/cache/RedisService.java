package com.github.m4rcioliveira.financial_manager_v0002.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void salvar(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object buscar(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object buscar(Long key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void atualizar(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void deletar(String key) {
        redisTemplate.delete(key);
    }

    // ✅ EXISTS
    public boolean existe(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

//    // ✅ INCREMENT (muito usado 💥)
//    public Long incrementar(String key) {
//        return redisTemplate.opsForValue().increment(key);
//    }
//
//    // ✅ SAVE com TTL
//    public void salvar(String key, Object value) {
//        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10));
//    }

}
