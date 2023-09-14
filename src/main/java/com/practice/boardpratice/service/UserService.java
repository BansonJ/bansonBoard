package com.practice.boardpratice.service;

import com.practice.boardpratice.domain.Member;
import com.practice.boardpratice.dto.MemberForm;
import com.practice.boardpratice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member UserRegister(MemberForm memberForm) {
        System.out.println(memberForm.getEmail());
        Member member = Member.builder()
                .email(memberForm.getEmail())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .nickname(memberForm.getNickname())
                .build();
        return userRepository.save(member);
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public Member findByUserEmail(String email) {
        Member member = userRepository.findByUserEmail(email);
        return member;
    }

    public String findNickname(String email) {
        Optional<Member> user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return user.get().getNickname();
    }
}
