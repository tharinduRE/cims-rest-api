package com.cheminv.app.repository;

import com.cheminv.app.domain.InvUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InvUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvUserRepository extends JpaRepository<InvUser, Long> {
}
