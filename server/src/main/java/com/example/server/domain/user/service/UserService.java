package com.example.server.domain.user.service;

import com.example.server.domain.admin.entity.Admin;
import com.example.server.domain.admin.repository.AdminRepository;
import com.example.server.domain.user.repository.UserRepository;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import com.example.server.security.util.CustomAuthorityUtils;
import com.example.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;

    public User createUser(User user){
        verifiedByLoginId(user);
//        verifiedByEmail(user);
        setEncodedPassword(user);
        setDefaultRole(user);
        User verifiedUser = verifiedAdmin(user);
        // todo : Roles가 관리자면 관리자 객체를 생성시켜 넣고 판매자면 판매자 객체를 생성시켜 넣기
        // UserRoles 클래스 và UserAuthenticationSuccessHandler.sendAuthorization() 참고!
        makeCart(verifiedUser);
        return userRepository.save(verifiedUser);
    }

    private void setDefaultRole(User user) {
        List<String> roles = customAuthorityUtils.createRoles(user);
        user.setRoles(roles);
//        user.getRoles().add("USER");
    }

    private void setEncodedPassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User updateUser(User user){
        Long userId = user.getUserId();
        User findUser = findVerifiedUserById(userId);

        Optional.ofNullable(user.getPassword())
                .ifPresent(newPassword -> findUser.setPassword(passwordEncoder.encode(newPassword)));
        Optional.ofNullable(user.getAddress())
                .ifPresent(newAddress -> findUser.setAddress(newAddress));
        Optional.ofNullable(user.getEmail())
                .ifPresent(newEmail -> findUser.setEmail(newEmail));

        return userRepository.save(findUser);
    }

    public User findUserByLoginId(String loginId) {
        return findVerifiedUserByLoginId(loginId); // Using the existing method
    }

    public User findVerifiedUserById(Long userId){
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
                });

        verifiedActiveUser(findUser);
        return findUser;
    }

    public User findVerifiedUserByLoginId(String loginId){
        User findUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
                });

        verifiedActiveUser(findUser);
        return findUser;
    }

    private static void verifiedActiveUser(User findUser) {
        if(findUser.getUserStatus().getStatus().equals("삭제된계정")){
            throw new BusinessLogicException(ExceptionCode.REMOVED_USER);
        }else if(findUser.getUserStatus().getStatus().equals("휴면계정")){
            throw new BusinessLogicException(ExceptionCode.SLEEPER_USER);
        }
    }

    public Page<User> findUsers(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("createdAt").descending());
        // Active한 User들만 가져온 후, 페이징 객체로 생성
        return userRepository.findAllByUserStatus(User.UserStatus.USER_ACTIVE, pageRequest);
    }

    public void removeUser(Long userId){
        User findUser = findVerifiedUserById(userId);
        verifiedActiveUser(findUser);

        findUser.setUserStatus(User.UserStatus.USER_DELETE);
        userRepository.save(findUser);
    }

    private void verifiedByEmail(User user) {
        // Email đã tồn tại
        String email = user.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(existingUser -> {
            throw new BusinessLogicException(ExceptionCode.USER_EMAIL_EXISTS);
        });
    }

    private void verifiedByLoginId(User user) {
        // Login ID đã tồn tại
        String loginId = user.getLoginId();
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        optionalUser.ifPresent(existingUser -> {
            throw new BusinessLogicException(ExceptionCode.USER_LOGIN_ID_EXISTS);
        });
    }

    public User verifiedAdmin(User findUser) {
        if (findUser.getRoles().contains("ADMIN")) {
            Admin admin = new Admin();
            admin.setLoginId(findUser.getLoginId());
            admin.setPassword(findUser.getPassword());
            findUser.setAdmin(admin);
        }
        return findUser;
    }

    private void makeCart(User user){
        if(user.getRoles().contains("SELLER")) {
            user.setCart(null);
        }
    }

    public void verifiedAdminRole(User findUser) {
        if(!findUser.getRoles().contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_ADMIN);
        }
    }
}
