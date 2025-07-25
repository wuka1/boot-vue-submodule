package com.example.demoservice.aspect;

import com.example.demoservice.annotation.AuthRequire;
import com.example.demoservice.api.AuthClient;
import com.example.demoservice.dto.AuthRequestDTO;
import com.example.demoservice.dto.AuthResponseDTO;
import com.example.demoservice.result.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/24
 * *@Version 1.0
 **/
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor {

    private final AuthClient authClient;

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-User-Roles";

    @Around("@annotation(com.example.demoservice.annotation.AuthRequire)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取当前请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        // 2. 获取用户身份信息
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null || userId.isEmpty()) {
//            throw new UnauthorizedException("Missing user identity");
            log.error("用户信息缺失");
            throw  new RuntimeException("用户信息缺失");
        }

        // 3. 获取注解配置
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthRequire requiresAuth = method.getAnnotation(AuthRequire.class);

        // 4. 构建权限检查请求
//        AuthRequestDTO authRequest = new AuthRequestDTO();
//        authRequest.setUsername(userId);
//        authRequest.setUri(requiresAuth.resource());
//        authRequest.setMethod(requiresAuth.action());

        // 5. 获取资源ID (如果需要)
//        if (requiresAuth.requiresResourceId()) {
//            String resourceId = resolveResourceId(joinPoint, requiresAuth, request);
//            authRequest.setResourceId(resourceId);
//        }

        // 6. 调用权限服务
        R<AuthResponseDTO> response = authClient.hasPermission(userId, requiresAuth.resource(), requiresAuth.action());

        // 7. 检查权限结果
        if (!response.getData().isAllow()) {
//            throw new ForbiddenException("Permission denied: " +
//                    requiresAuth.action() + " on " + requiresAuth.resource());
            log.error("权限不足");
            throw  new RuntimeException("权限不足");
        }

        // 8. 权限通过，执行原方法
        return joinPoint.proceed();
    }
}
