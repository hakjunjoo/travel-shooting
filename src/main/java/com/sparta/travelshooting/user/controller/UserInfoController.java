package com.sparta.travelshooting.user.controller;

import com.sparta.travelshooting.security.UserDetailsImpl;
import com.sparta.travelshooting.user.dto.EditInfoRequestDto;
import com.sparta.travelshooting.user.dto.UserResponseDto;
import com.sparta.travelshooting.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    // 마이페이지 : 자신의 정보 조회
    @GetMapping("/my-page")
    public ResponseEntity<UserResponseDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto userResponseDto = userInfoService.getUserInfo(userDetails.getUser());

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    // 마이페이지 : 자신의 정보 수정 (nickname, region)
    @PutMapping("/my-page/edit")
    public ResponseEntity<UserResponseDto> editUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EditInfoRequestDto requestDto) {
        UserResponseDto userResponseDto = userInfoService.editUserInfo(userDetails.getUser(), requestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }


    // 마이페이지 : 비밀번호 수정
}
