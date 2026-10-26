
package com.example.demo.repository;

import com.example.demo.entity.DarshanSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DarshanSlotRepository
        extends JpaRepository<DarshanSlot, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM DarshanSlot s WHERE s.id = :id")
    Optional<DarshanSlot> findByIdForUpdate(@Param("id") Long id);
}
