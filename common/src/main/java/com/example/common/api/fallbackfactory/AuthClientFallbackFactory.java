package com.example.common.api.fallbackfactory;

import com.example.common.api.dto.AuthResponseDTO;
import com.example.common.api.feignclient.AuthClient;
import com.example.common.result.ErrorInfo;
import com.example.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/29
 * *@Version 1.0
 **/

@Component
@Slf4j
public class AuthClientFallbackFactory implements FallbackFactory<AuthClient> {
    @Override
    public AuthClient create(Throwable cause) {
        return new AuthClient() {
            @Override
            public R<AuthResponseDTO> hasPermission(String username, String uri, String method) {
                log.warn("fallback hasPermission({},{},{}): {}", username, uri, method, cause.getMessage());
                AuthResponseDTO auth = new AuthResponseDTO(false);
                return R.<AuthResponseDTO>fail(ErrorInfo.REQUEST_API_ERROR).setData(auth);
            }
        };
    }
}
