package com.sparta.travelshooting.user.controller;

import com.sparta.travelshooting.security.UserDetailsImpl;
import com.sparta.travelshooting.user.dto.ApiResponseDto;
import com.sparta.travelshooting.user.dto.EditInfoRequestDto;
import com.sparta.travelshooting.user.dto.PasswordRequestDto;
import com.sparta.travelshooting.user.dto.UserResponseDto;
import com.sparta.travelshooting.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PutMapping("/my-page/edit/password")
    public ResponseEntity<ApiResponseDto> editUserPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        userInfoService.editUserPassword(userDetails.getUser(), requestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto("비밀번호를 수정했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    // IllegalArgumentException 에 대한 예외처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> illegalArgumentException(IllegalArgumentException e) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
    }
}
