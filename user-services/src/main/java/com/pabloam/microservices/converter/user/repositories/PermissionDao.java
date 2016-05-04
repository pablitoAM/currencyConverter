/**
 * 
 */
package com.pabloam.microservices.converter.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pabloam.microservices.converter.user.model.Permission;

/**
 * @author PabloAM
 *
 */
public interface PermissionDao extends JpaRepository<Permission, Long> {

}
