package me.jisung.springplayground.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.JwtProvider;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.user.dto.UserRequest;
import me.jisung.springplayground.user.dto.UserResponse;
import me.jisung.springplayground.user.entity.UserEntity;
import me.jisung.springplayground.user.mapper.UserMapper;
import me.jisung.springplayground.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService")
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 생성
     * @param request 생성 대상 사용자 정보
     * */
    @Transactional
    public void create(UserRequest request) {
        /* 이메일 기준 이미 존재하는 사용자라면 예외처리 */
        Optional<UserEntity> findUser = userRepository.findByEmail(request.getEmail());
        if(findUser.isPresent()) throw new ApiException(Api4xxErrorCode.ALREADY_EXISTS_ENTITY, "이미 존재하는 사용자입니다.");

        UserEntity user = UserMapper.toEntity(request, passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        log.info("사용자 생성 완료: {}", user.getId());
    }

    /**
     * 사용자 토큰 발급
     * @param request 토큰 발급을 위한 정보 (email, password)
     * @return 접근 토큰
     * */
    public UserResponse getToken(UserRequest request) {
        try {
            /* 사용자 인증 */
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String accessToken = jwtProvider.generateAccessToken(request.getEmail());
            return new UserResponse(accessToken);
        } catch (BadCredentialsException e) {
            throw new ApiException(e, Api4xxErrorCode.INVALID_USER_CREDENTIALS, "사용자 인증에 실패하였습니다. 요청 정보를 확인해주세요.");
        }
    }

}
