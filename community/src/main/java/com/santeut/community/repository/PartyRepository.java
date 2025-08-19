package com.santeut.community.repository;

import com.santeut.community.entity.Party;
import com.santeut.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {

    Optional<Party> findByPartyId(int partyId);

}
