package com.rmiranda.schoolmanagement.model.repository;

import java.util.List;

import com.rmiranda.schoolmanagement.model.entity.Cycle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    public List<Cycle> findByClosedAtIsNull();

}
