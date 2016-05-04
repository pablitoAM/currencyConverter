/**
 * 
 */
package com.pabloam.microservices.converter.user.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pabloam.microservices.converter.user.model.Permission;

/**
 * @author PabloAM
 *
 */
public interface PermissionDao extends CrudRepository<Permission, Long> {

}
