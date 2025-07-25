//package com.example.auth.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
///**
// * *@Description TODO
// * *@Author wuka
// * *@Date 2025/7/21
// * *@Version 1.0
// **/
//
//@Configuration
////@EnableWebSecurity
//@RequiredArgsConstructor
//public class CustomSecurityConfig {

//    private final UserDetailsServiceImpl userDetailsServiceImpl;
//
//    private final CustomAuthenticationProvider customAuthenticationProvider;
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  // spring security 5.4
//        // .x版本以上不需要WebSecurityConfigurerAdapter
//        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
//
//        http.csrf().disable()
//                .authorizeRequests().antMatchers("/auth/**", "/public").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
////                .authenticationProvider(daoAuthenticationProvider()); //使用默认的认证逻辑
//                .authenticationProvider(customAuthenticationProvider)  // 使用自定义的认证逻辑
//                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    // 认证管理器配置--启动会进行配置
////    @Bean
////    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
////        return http.getSharedObject(AuthenticationManagerBuilder.class)
////                .userDetailsService(userDetailsServiceImpl)
////                .passwordEncoder(passwordEncoder())
////                .and().build();
////    }
//
//
//
//    // --启动会进行配置
////    @Bean
////    public DaoAuthenticationProvider daoAuthenticationProvider() {
////        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
////        provider.setUserDetailsService(userDetailsServiceImpl);
////        provider.setPasswordEncoder(passwordEncoder());
////        return provider;
////    }
//}
