package com.example.permission.service.impl;

import com.example.permission.domain.vo.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
////        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
////        System.out.println(usernamePasswordAuthenticationFilter.getPasswordParameter());
////         new DaoAuthenticationProvider();
////        new UsernamePasswordAuthenticationToken("456", "123");
////        new Authentication();
//        //1、根据username查询数据库
//        System.out.println(username);
//
//        //2、判断密码
//
//
//        return new LoginUser(username);
//    }
//}
