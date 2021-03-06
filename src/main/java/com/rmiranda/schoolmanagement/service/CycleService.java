package com.rmiranda.schoolmanagement.service;

import java.util.List;

import com.rmiranda.schoolmanagement.model.entity.Cycle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CycleService {

    public void addCycle(Cycle cycle);

    public Page<Cycle> getAllCycles(Pageable page);

    public void updateCycle(Cycle cycle);

    public Cycle getcycleById(long cycleId);

    public List<Cycle> getActiveCycles();
    
}
