package com.santeut.community.controller;

import com.santeut.community.common.response.BasicResponse;
import com.santeut.community.common.response.ResponseUtil;
import com.santeut.community.service.implementation.PartyServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hiking")
@RequiredArgsConstructor
public class PartyController {
    
    private final PartyServiceImpl partyService;
    
    @PostMapping("/{partyId}/enter/{userId}")
    public ResponseEntity<BasicResponse> enter(@PathVariable int partyId, HttpServletRequest request){
        return ResponseUtil.buildBasicResponse(HttpStatus.OK,
                partyService.enterParty(partyId,Integer.parseInt(request.getHeader("userId"))));
    }
}
