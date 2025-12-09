package com.example.study.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.demo.mapper.TestUserMapper;
import com.example.demo.model.TestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final TestUserMapper testUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TestUser user = new LambdaQueryChainWrapper<>(testUserMapper).eq(
                TestUser::getUsername, username
        ).one();
        if(user==null) {
            throw new UsernameNotFoundException("用户不存在: "+username);
        }
        return user;
    }
}
