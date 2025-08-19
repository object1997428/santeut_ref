package com.santeut.community.service;

import com.santeut.community.common.JwtUtil;
import com.santeut.community.entity.Party;
import com.santeut.community.entity.PartyUser;
import com.santeut.community.entity.User;
import com.santeut.community.repository.PartyRepository;
import com.santeut.community.repository.PartyUserRepository;
import com.santeut.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartyService {

    final private PartyRepository partyRepository;
    final private UserRepository userRepository;
    final private PartyUserRepository partyUserRepository;
    final private JwtUtil jwtUtil;

    public String enterParty(int partyId, int userId) {
        try {
            Party party = partyRepository.findByPartyId(partyId).orElseThrow(() -> new RuntimeException("Party is not exist"));
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User is not exist"));
            PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(partyId, userId).orElseThrow(() -> new RuntimeException("User is not exist in Party"));

            if (validateParty(party, userId)) {
                //토큰 발급
                return getJwtToken(partyId, userId, user, partyUser);
            } else throw new RuntimeException("Party is not validate");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getJwtToken(int partyId, int userId, User user, PartyUser partyUser) {
        var claims = new HashMap<String, Object>();
        claims.put("user_id", userId);
        claims.put("user_nickname", user.getUserNickname());
        claims.put("party_id", partyId);
        claims.put("user_party_id", partyUser.getPartyUserId());
        claims.put("user_profile", user.getUserProfile());

        return jwtUtil.createToken(60000, claims);
    }

    private static boolean validateParty(Party party, Integer userId) {
        //파티가 이미 진행 중
        if (party.getStatus() == 'P') {
            return true;
        }
        //파티 시작 전
        else if (party.getStatus() == 'B') {
            if (party.getUserId() == userId && party.canPartyStart()) return true;
            return false;
        }
        //파티 종료
        else return false;
    }

}
