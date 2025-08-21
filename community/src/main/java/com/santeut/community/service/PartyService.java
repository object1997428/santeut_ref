package com.santeut.community.service;

import com.santeut.community.dto.PartyEnterResponseDto;

public interface PartyService {
    public PartyEnterResponseDto enterParty(int partyId, int userId);
}
