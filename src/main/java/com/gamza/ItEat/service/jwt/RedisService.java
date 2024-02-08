package com.gamza.ItEat.service.jwt;

import com.gamza.ItEat.error.ErrorCode;
import com.gamza.ItEat.error.exeption.InvalidTokenException;
import com.gamza.ItEat.error.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    public void setValues(String token, String email) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        operations.set(token, map, Duration.ofDays(7));
    }

    public Map<String, String> getValues(String token){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Object object = operations.get(token);
        if (object instanceof Map) {
            return (Map<String, String>) object;
        }
        return null;
    }

    public boolean isRefreshTokenValid(String token, String ipAddress) {
        Map<String, String> values = getValues(token);
        if (values == null) {
            return false;
        }
        String storedIpAddress = values.get("ipAddress");
        return ipAddress.equals(storedIpAddress);
    }

    public boolean isTokenInBlacklist(String token) {
        if (redisTemplate.hasKey(token)) {
            throw new InvalidTokenException("401_Invalid", ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
        return false;
    }

    public void addTokenToBlacklist(String token, long expiration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token, true, expiration, TimeUnit.MILLISECONDS);
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }

}
