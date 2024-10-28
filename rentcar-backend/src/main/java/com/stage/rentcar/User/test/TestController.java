package com.stage.rentcar.User.test;

import com.stage.rentcar.User.test.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    //    Only users with 'ROLE_CLIENT' role can access this end point
    @GetMapping("/client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ApiResponseDto<?>> UserDashboard() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.builder()
                        .isSuccess(true)
                        .message("client dashboard!")
                        .build());
    }

    //    Only users with 'ROLE_ADMIN' role can access this end point'
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> AdminDashboard() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.builder()
                        .isSuccess(true)
                        .message("Admin dashboard!")
                        .build());
    }

    //    Only users with 'ROLE_SUPER_ADMIN' role can access this end point'
    @GetMapping("/chef_agence")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE')")
    public ResponseEntity<ApiResponseDto<?>> SuperAdminDashboard() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.builder()
                        .isSuccess(true)
                        .message("CHEF_AGENCE dashboard!")
                        .build());
    }

    //    Users with 'ROLE_SUPER_ADMIN' or 'ROLE_ADMIN' roles can access this end point'
    @GetMapping("/AdminOrChefAgence")
    @PreAuthorize("hasRole('ROLE_CHEF_AGENCE') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<?>> AdminOrSuperAdminContent() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.builder()
                        .isSuccess(true)
                        .message("AdminOrChefAgence Content!")
                        .build());
    }
}
