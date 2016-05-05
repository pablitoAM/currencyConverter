package com.pabloam.microservices.converter.user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pabloam.microservices.converter.user.model.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

	/**
	 * Finds a group by the given description
	 * 
	 * @param groupDescritpion
	 * @return
	 */
	@Query("select g from Group g where g.description = ?1")
	Group findByDescription(String groupDescritpion);

	/**
	 * Finds a list of groups by the given description list
	 * 
	 * @param descriptionList
	 * @return
	 */
	@Query("select g from Group g where g.description in (?1)")
	List<Group> findMultipleByDescriptionList(List<String> descriptionList);

}
