package com.pabloam.microservices.converter.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pabloam.microservices.converter.user.model.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

}
