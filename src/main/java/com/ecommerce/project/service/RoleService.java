package com.ecommerce.project.service;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;

public interface RoleService {
    Role findByRoleName(AppRole appRole);
}
