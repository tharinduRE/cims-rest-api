package com.cheminv.app.repository;

import com.cheminv.app.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"authorities","invStores"})
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"authorities","invStores"})
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"authorities","invStores"})
    List<User> findAllByEmailNot(String email);

    @Query("select distinct invUser from User invUser left join fetch invUser.authorities left join fetch invUser.stores")
    List<User> findAllWithEagerRelationships();

    @Query("select invUser from User invUser left join fetch invUser.authorities left join fetch invUser.stores where invUser.id =:id")
    Optional<User> findOneWithEagerRelationships(@Param("id") Long id);
}
