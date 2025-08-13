package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.service.DormitoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dorm")
@RequiredArgsConstructor
public class DormitoryController {
    private final DormitoryService dormitoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Dormitory> createDormitory(@RequestBody @Valid Dormitory dormitory) {
        Dormitory createdDormitory = dormitoryService.createDorm(dormitory);
        return new ResponseEntity<>(createdDormitory, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Dormitory> updateDormitory(@PathVariable Long id,
                                                     @RequestBody @Valid Dormitory updatedDormitory
                                                     ) {
        Dormitory dorm = dormitoryService.updateDorm(id, updatedDormitory);
        return new ResponseEntity<>(dorm, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<Dormitory>> findAllDormitory() {
        return new ResponseEntity<>(dormitoryService.findAllDormitory(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Dormitory> deleteDormitory(@PathVariable Long id) {
        dormitoryService.deleteDorm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
