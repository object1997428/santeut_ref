package com.santeut.community.service.implementation;

import com.santeut.community.common.JwtUtil;
import com.santeut.community.dto.CreatePartyRequestDto;
import com.santeut.community.entity.Party;
import com.santeut.community.entity.PartyUser;
import com.santeut.community.entity.User;
import com.santeut.community.repository.PartyRepository;
import com.santeut.community.repository.PartyUserRepository;
import com.santeut.community.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyServiceImplTest {

    @Mock
    private PartyRepository partyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PartyUserRepository partyUserRepository;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private PartyServiceImpl partyService;


    @Test
    void 파티_입장_성공() {
        //given
        Party party = new Party();
        party.setPartyStatus('P');
        int partyId = party.getPartyId();

        User user = new User();
        user.updateUserNickname("테스트유저");
        user.updateUserProfile("profile.jpg");
        int userId = user.getUserId();

        PartyUser partyUser = new PartyUser();

        //Mocking
        when(partyRepository.findByPartyId(partyId)).thenReturn(Optional.of(party));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(partyUserRepository.findByPartyIdAndUserId(partyId, userId)).thenReturn(Optional.of(partyUser));

        var claims = new HashMap<String, Object>();
        claims.put("user_id", user.getUserId());
        claims.put("user_nickname", user.getUserNickname());
        claims.put("party_id", party.getPartyId());
        claims.put("user_party_id", partyUser.getPartyUserId());
        claims.put("user_profile", user.getUserProfile());
        when(jwtUtil.createToken(60000, claims)).thenReturn("test-jwt-token");

        //when
        String jwtToken = partyService.enterParty(partyId, userId);

        //then
        assertEquals("test-jwt-token", jwtToken);
    }

    @Test
    void 파티장이_아닌데_파티가_시작전() {
        //given
        Party party = new Party();
        party.setPartyStatus('B');
        int partyId = party.getPartyId();

        User user = new User();
        user.updateUserNickname("테스트유저");
        user.updateUserProfile("profile.jpg");
        int userId = user.getUserId();

        PartyUser partyUser = new PartyUser();

        //Mocking
        when(partyRepository.findByPartyId(partyId)).thenReturn(Optional.of(party));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(partyUserRepository.findByPartyIdAndUserId(partyId, userId)).thenReturn(Optional.empty());


        //when & then
        assertThrows(RuntimeException.class, () -> partyService.enterParty(partyId, userId));
    }

    @Test
    void 파티장이_파티를_시작함() {
        //given
        User user = new User();
        user.updateUserNickname("테스트유저");
        user.updateUserProfile("profile.jpg");
        int userId = user.getUserId();


        CreatePartyRequestDto requestDto = new CreatePartyRequestDto();
        requestDto.setSchedule(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Party party = Party.createEntity(userId, requestDto, "[1]");
        int partyId = party.getPartyId();

        PartyUser partyUser = new PartyUser();

        //Mocking
        when(partyRepository.findByPartyId(partyId)).thenReturn(Optional.of(party));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(partyUserRepository.findByPartyIdAndUserId(partyId, userId)).thenReturn(Optional.of(partyUser));

        var claims = new HashMap<String, Object>();
        claims.put("user_id", user.getUserId());
        claims.put("user_nickname", user.getUserNickname());
        claims.put("party_id", party.getPartyId());
        claims.put("user_party_id", partyUser.getPartyUserId());
        claims.put("user_profile", user.getUserProfile());
        when(jwtUtil.createToken(60000, claims)).thenReturn("test-jwt-token");

        //when
        String jwtToken = partyService.enterParty(partyId, userId);

        //then
        assertEquals("test-jwt-token", jwtToken);
        assertEquals(party.getStatus(), 'P');
        assertTrue(LocalDateTime.now().isAfter(party.getStarted_at()));
    }

    @Test
    void 아직_파티를_시작할_수_없는데_파티장이_시작하려함() {
        //given
        User user = new User();
        user.updateUserNickname("테스트유저");
        user.updateUserProfile("profile.jpg");
        int userId = user.getUserId();

        CreatePartyRequestDto requestDto = new CreatePartyRequestDto();
        requestDto.setSchedule(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Party party = Party.createEntity(userId, requestDto, "[1]");
        int partyId = party.getPartyId();

        PartyUser partyUser = new PartyUser();

        //Mocking
        when(partyRepository.findByPartyId(partyId)).thenReturn(Optional.of(party));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(partyUserRepository.findByPartyIdAndUserId(partyId, userId)).thenReturn(Optional.of(partyUser));

        //when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> partyService.enterParty(partyId, userId));
        assertEquals("Party is not validate", exception.getMessage());
    }

    @Test
    void 이미_파티가_끝남() {
        //given
        Party party = new Party();
        party.setPartyStatus('E');
        int partyId = party.getPartyId();

        User user = new User();
        user.updateUserNickname("테스트유저");
        user.updateUserProfile("profile.jpg");
        int userId = user.getUserId();

        PartyUser partyUser = new PartyUser();

        //Mocking
        when(partyRepository.findByPartyId(partyId)).thenReturn(Optional.of(party));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(partyUserRepository.findByPartyIdAndUserId(partyId, userId)).thenReturn(Optional.of(partyUser));

        //when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> partyService.enterParty(partyId, userId));
        assertEquals("Party is not validate", exception.getMessage());
    }
}