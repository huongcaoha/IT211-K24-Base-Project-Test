package org.example.project_base_test.service.impl;

import org.example.project_base_test.model.dto.SocialLoginRequest;
import org.example.project_base_test.model.dto.UserLoginDTO;
import org.example.project_base_test.model.dto.UserLoginResponse;
import org.example.project_base_test.model.dto.UserRegisterDTO;
import org.example.project_base_test.model.entity.User;
import org.example.project_base_test.repository.UserRepository;
import org.example.project_base_test.security.jwt.JWTProvider;
import org.example.project_base_test.security.principle.UserDetailService;
import org.example.project_base_test.security.principle.UserPrinciple;
import org.example.project_base_test.service.EmailService;
import org.example.project_base_test.service.GoogleAuthService;
import org.example.project_base_test.service.RoleService;
import org.example.project_base_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService ;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailService userDetailService;
    private final JWTProvider jwtProvider;
    private final EmailService emailService;
    public AuthServiceImpl(UserRepository userRepository, GoogleAuthService googleAuthService, RoleService roleService, PasswordEncoder passwordEncoder, UserDetailService userDetailService, JWTProvider jwtProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
        this.jwtProvider = jwtProvider;
        this.emailService = emailService;
    }

    @Override
    @Cacheable(cacheNames = "users",key = "#email")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String save(User user) {
        try {
            userRepository.save(user);
            return "User has been saved successfully";
        }catch (Exception ex){
            ex.printStackTrace();
            return ex.getMessage();
        }

    }

    public static long randomCode() {
        Random random = new Random();
        return 1_000_000_000L + (long)(random.nextDouble() * 9_000_000_000L);
    }

    public ResponseEntity<?> register(UserRegisterDTO userRegisterDTO){
        try {
            long otp = randomCode();
            User user = User
                    .builder()
                    .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                    .email(userRegisterDTO.getEmail())
                    .roles(Set.of(roleService.getRole("USER")))
                    .fullName(userRegisterDTO.getFullName())
                    .status(false)
                    .otp(otp)
                    .build();
            userRepository.save(user);
            emailService.sendEmail(user.getEmail(),"Mã OTP kích hoạt tài khoản",String.valueOf(otp));
            return new ResponseEntity<>("Register successfully", HttpStatus.OK);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> loginGoogle(SocialLoginRequest loginRequest) {
        // 1. Xác thực token từ Google gửi lên
        String email = googleAuthService.verifyAndGetEmail(loginRequest.getToken());

        // 2. Tìm User trong DB
        User user = findByEmail(email);
        if (user == null) {
            // Nếu chưa có thì tạo mới (Tự động đăng ký)
            user = new User();
            user.setEmail(email);
            user.setSecret(UUID.randomUUID().toString());
            user.setRoles(Set.of(roleService.getRole("USER"))); // Gán quyền mặc định
            userRepository.save(user);
        }

        // 3. Cấp phát JWT nội bộ của hệ thống thầy cho user này
        UserPrinciple userPrinciple = (UserPrinciple) userDetailService.loadUserByUsername(user.getEmail());
        return ResponseEntity.ok(UserLoginResponse
                .builder()
                .accessToken(jwtProvider.generateToken(userPrinciple))
                .email(user.getEmail())
                .fullName(user.getFullName())
                .RefreshToken(jwtProvider.generateRefreshToken(userPrinciple))
                .build());
    }

    @Override
    public ResponseEntity<?> login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail());
        if (user == null || !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.BAD_REQUEST);
        }
        String uuid = UUID.randomUUID().toString();
        user.setSecret(uuid);
        userRepository.save(user);
        UserPrinciple userPrinciple = (UserPrinciple) userDetailService.loadUserByUsername(user.getEmail());
        return ResponseEntity.ok(UserLoginResponse
                .builder()
                        .accessToken(jwtProvider.generateToken(userPrinciple))
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .RefreshToken(jwtProvider.generateRefreshToken(userPrinciple))
                .build());
    }


}
