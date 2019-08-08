package com.baomidou.kisso.springmvc.cache;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.springmvc.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 小贱
 * @create 2019-08-07 15:46
 */
public class RedisCache implements SSOCache {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SSOToken get(String s, int i) {
        if(exists(s)){
            String result = redisUtil.get(s);
            return  SSOToken.parser(result, false);
        }
        return null;
    }

    @Override
    public boolean set(String s, SSOToken token, int i) {
        boolean result = false;
        try {
            delete(s);
            redisUtil.set(s, token.getToken(), i);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public boolean delete(String s) {
        boolean result = false;
        try{
            if (exists(s)) {
                redisUtil.delete(s);
                return  true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean exists(final String key) {
        return redisUtil.get(key)!=null;
    }
}
