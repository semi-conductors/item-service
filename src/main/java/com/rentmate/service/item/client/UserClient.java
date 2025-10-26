package com.rentmate.service.item.client;

import com.rentmate.service.item.domain.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8080/users")
public interface UserClient {
//    @GetMapping("/{id}")
//    UserResponseDTO getUserById(@PathVariable("id") Long id);
    @GetMapping("/{id}")
    UserResponseDTO getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token
    );
}
