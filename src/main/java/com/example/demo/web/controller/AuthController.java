package com.example.demo.web.controller;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.config.exception.VsException;
import com.example.demo.config.security.AccountDetailsImpl;
import com.example.demo.config.security.JwtUtils;
import com.example.demo.domain.dto.EmailDetails;
import com.example.demo.domain.model.Account;
import com.example.demo.domain.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.SignupRequest;
import com.example.demo.request.account.ChangeInformationRequest;
import com.example.demo.request.account.ChangePassRequest;
import com.example.demo.response.JwtResponse;
import com.example.demo.response.MessageResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.impl.AccountDetailsServiceImpl;
import com.example.demo.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AccountDetailsServiceImpl userDetailsService;

    @Autowired
    EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AccountDetailsImpl userDetails = (AccountDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        Account account = userRepository.getByIdAndIsBLockFalse(userDetails.getId());
        if (account == null) {
            throw new InvalidException("Tài khoản đã bị khóa", "Tài Khoản đã bị khóa");
        }
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles
                , account.getPhone(), account.getAddress(), account.getBod(), account.getCardId(), account.getName(), account.getPoint(), account.getGender(),account.getImage()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tên đăng nhập đã tồn tại!"));
        }

        if (userRepository.existsByEmailAndUsernameNotNull(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại!"));
        }
        if (userRepository.existsByCardIdAndUsernameNotNull(signUpRequest.getCardId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("CMT/CCCD đã tồn tại!"));
        }

        // Create new user's account
        Account user = new Account(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), new Role(signUpRequest.getRole()), false, signUpRequest.getName(), signUpRequest.getPhone(), signUpRequest.getAddress(), signUpRequest.getCardId(), signUpRequest.getGender(), 100L, signUpRequest.getBod());

        String strRole = signUpRequest.getRole().name();
        Role userRole = roleRepository.findByName(signUpRequest.getRole()).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(userRole);

        userRepository.save(user);


        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công!"));
    }

    @PutMapping("/change/password")
    public ResponseEntity<?> changePass(@Valid @RequestBody ChangePassRequest request) {
        Account account = userRepository.getById(request.getId());
        if (account == null) {
            throw new VsException("Tài khoản không tồn tại", "Tài khoản không tồn tại");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getOldPassword()));
            AccountDetailsImpl userDetails = (AccountDetailsImpl) authentication.getPrincipal();
        } catch (Exception e) {
            throw new InvalidException("Mật khẩu cũ không đúng", "Mật khẩu cũ không đúng");
        }

        if (!request.getNewPassword().equals(request.getEnterNewPassword())) {
            throw new InvalidException("Mật khẩu mới không trùng nhau", "Mật khẩu mới không trùng nhau");
        }

        account.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(account);
        return successResponse();
    }

    @PutMapping("/change/information")
    public ResponseEntity<?> changeInformation(@Valid @RequestBody ChangeInformationRequest request) {
        Account account = userRepository.getById(request.getId());
        if (account == null) {
            throw new VsException("Tài khoản không tồn tại", "Ta khoản không tồn tại");
        }

        if (userRepository.existsByEmailAndUsernameNotNull(request.getEmail()) && !account.getEmail().equalsIgnoreCase(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại!"));
        }
        if (userRepository.existsByCardIdAndUsernameNotNull(request.getCardId()) && !account.getCardId().equalsIgnoreCase(request.getCardId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("CMT/CCCD đã tồn tại!"));
        }
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAddress(request.getAddress());
        account.setCardId(request.getCardId());
        account.setBod(request.getBod());
        account.setGender(request.getGender());

        userRepository.save(account);
        return successResponse();
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassByEmail(@Valid @RequestBody ChangePassRequest request) {
        Account account = userRepository.getAccountByEmailAndUsername(request.getEmail(), request.getUserName());
        if (account == null) {
            throw new VsException("Tên đăng nhập và email không khớp", "Tên đăng nhập và email không khớp");
        }

        RandomString randomString = new RandomString();

        String pass = randomString.nextString();
        account.setPassword(encoder.encode(pass));
        userRepository.save(account);

        emailService.sendMail(new EmailDetails(request.getEmail(), "Mật khẩu mới của bạn là : " + pass, "Bokar support"));

        return successResponse();
    }

}