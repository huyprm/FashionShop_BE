package org.ptithcm2021.fashionshop.repository;

import org.ptithcm2021.fashionshop.enums.RoleEnum;
import org.ptithcm2021.fashionshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, String>{

}
