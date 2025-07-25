package com.example.common.utils.jwt;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/7/21
 * *@Version 1.0
 **/
public interface UserContext extends Serializable {
    String getUsername();

    String getType();

    List<String> getRoles();
    Instant getExpiration();
}
