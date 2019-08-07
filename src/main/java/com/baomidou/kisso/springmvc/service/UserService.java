package com.baomidou.kisso.springmvc.service;

import com.baomidou.kisso.springmvc.dao.UserMapper;
import com.baomidou.kisso.springmvc.model.User;
import com.baomidou.kisso.springmvc.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小贱
 * @create 2019-08-07 16:11
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public User getUser(String username, String password) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size() > 0){
            return userList.get(0);
        }
        return null;
    }
}
