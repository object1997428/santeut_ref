package com.santeut.community.service;

import com.santeut.community.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartyService {

    final private PartyRepository partyRepository;

    public String enterParty(int partyId, int userId){
        try{


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

}
