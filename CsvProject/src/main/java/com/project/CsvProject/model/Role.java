package com.project.CsvProject.model;

public enum Role {
    DIRECTOR(0.05),
    MANAGER(0.02),
    EMPLOYEE(0.01);

    private final double baseRate;

    Role(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public static Role fromString(String roleStr) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleStr.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + roleStr);
    }
}