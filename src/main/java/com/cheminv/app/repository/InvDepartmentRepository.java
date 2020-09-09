package com.cheminv.app.repository;

import com.cheminv.app.domain.InvDepartment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the InvDepartment entity.
 */
@Repository
public interface InvDepartmentRepository extends JpaRepository<InvDepartment, Long> {

    @Query(value = "select distinct invDepartment from InvDepartment invDepartment left join fetch invDepartment.invUsers",
        countQuery = "select count(distinct invDepartment) from InvDepartment invDepartment")
    Page<InvDepartment> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct invDepartment from InvDepartment invDepartment left join fetch invDepartment.invUsers")
    List<InvDepartment> findAllWithEagerRelationships();

    @Query("select invDepartment from InvDepartment invDepartment left join fetch invDepartment.invUsers where invDepartment.id =:id")
    Optional<InvDepartment> findOneWithEagerRelationships(@Param("id") Long id);
}
