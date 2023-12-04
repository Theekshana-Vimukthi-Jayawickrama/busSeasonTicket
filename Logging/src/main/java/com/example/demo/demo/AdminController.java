package com.example.demo.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping
    @PreAuthorize( "hasAuthority('admin:read')")
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    @PreAuthorize( "hasAuthority('admin:create')")
    public String post() {
        return "Post:: admin controller";
    }
    @PutMapping
    @PreAuthorize( "hasAuthority('admin:update')")
    public String put() {
        return "put:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize( "hasAuthority('admin:delete')")
    public String delete() {
        return "delete:: admin controller";
    }


}
