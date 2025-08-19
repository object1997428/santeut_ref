package com.santeut.community.repository;

import com.santeut.community.entity.PartyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyUserRepository extends JpaRepository<PartyUser,Integer> {
    Optional<PartyUser> findByPartyIdAndUserId(int partyId,int userId);
}
