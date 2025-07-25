package com.example.auth.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.entity.PermissionEntity;
import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import com.example.auth.mapper.UserMapper;
import com.example.auth.security.UserDetailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/18
 * *@Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername,
                username));
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到: " + username);
        }
        // 自定义封装UserDetail
        return new UserDetailImpl(user);
//        // 获取用户角色
//        List<RoleEntity> roles = userMapper.findRolesByUserId(user.getId());
//        // 获取用户权限
//        List<PermissionEntity> permissions = userMapper.findPermissionsByUserId(user.getId());
//        List<String> permissions = permissionMapper.findPermissionsByUserId(user.getId());
//
//        List<GrantedAuthority> authorities = permissions.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                .collect(Collectors.toList());
//        return null;
    }
}
