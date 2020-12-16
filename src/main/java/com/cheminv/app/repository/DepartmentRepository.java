package com.cheminv.app.repository;

import com.cheminv.app.domain.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "select distinct invDepartment from Department invDepartment left join fetch invDepartment.users",
        countQuery = "select count(distinct invDepartment) from Department invDepartment")
    Page<Department> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct invDepartment from Department invDepartment left join fetch invDepartment.users")
    List<Department> findAllWithEagerRelationships();

    @Query("select invDepartment from Department invDepartment left join fetch invDepartment.users where invDepartment.id =:id")
    Optional<Department> findOneWithEagerRelationships(@Param("id") Long id);
}
