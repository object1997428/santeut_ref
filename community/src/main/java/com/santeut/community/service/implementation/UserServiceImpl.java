package com.santeut.community.service.implementation;

import com.santeut.community.entity.User;
import com.santeut.community.repository.UserRepository;
import com.santeut.community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

}
