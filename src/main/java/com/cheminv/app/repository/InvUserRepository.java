package com.cheminv.app.repository;

import com.cheminv.app.domain.InvUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the InvUser entity.
 */
@Repository
public interface InvUserRepository extends JpaRepository<InvUser, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<InvUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    Optional<InvUser> findOneWithAuthoritiesByEmail(String email);

    Optional<InvUser> findOneByEmailIgnoreCase(String email);

    Page<InvUser> findAllByEmailNot(Pageable pageable, String email);

    @Query(value = "select distinct invUser from InvUser invUser left join fetch invUser.authorities left join fetch invUser.invStores",
        countQuery = "select count(distinct invUser) from InvUser invUser")
    Page<InvUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct invUser from InvUser invUser left join fetch invUser.authorities left join fetch invUser.invStores")
    List<InvUser> findAllWithEagerRelationships();

    @Query("select invUser from InvUser invUser left join fetch invUser.authorities left join fetch invUser.invStores where invUser.id =:id")
    Optional<InvUser> findOneWithEagerRelationships(@Param("id") Long id);
}
