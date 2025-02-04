package org.ptithcm2021.fashionshop.enums;

import org.ptithcm2021.fashionshop.model.Role;

public enum RoleEnum {
    ADMIN("Administrator role with full access"),
    STAFF("Staff role with limited permissions"),
    CUSTOMER("Customer role with basic access");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
