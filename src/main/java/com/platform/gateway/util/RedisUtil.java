package com.platform.gateway.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @Description: 批量删除缓存
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * @Description: 批量删除缓存(通配符)
     */
   /* public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }*/

    /**
     * @Description: 删除缓存
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * @Description: 判断缓存中是否有对应的value
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description: 读取缓存
     */
    public Object getStringObject(final String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public Object getHashObject(String key, String hk){
    	return redisTemplate.opsForHash().get(key, hk);
    }
    
    public Map<String, String> getHashObject(String key){
    	Map<String, String> map = new HashMap();
    	Set<Object> set = redisTemplate.opsForHash().keys(key);
    	set.forEach(x->{
    		String hk = String.valueOf(x);
    		String hv = String.valueOf(getHashObject(key, hk));
    		map.put(hk, hv);
    	});
    	return map;
    }

    public boolean set(String key, String hk, String hv){
    	try{
    		redisTemplate.opsForHash().put(key, hk, hv);
    		return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    /**
     * @Description: 写入缓存
     */
    /*public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/

    /**
     * @Description: 写入缓存(可以配置过期时间)
     */
    /*public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }*/
}
