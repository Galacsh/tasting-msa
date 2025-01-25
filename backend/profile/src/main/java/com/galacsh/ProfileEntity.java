package com.galacsh;

public class ProfileEntity {
    private Long id;
    private String name;

    // Owner of the profile
    private String username;

    protected ProfileEntity() {}

    public ProfileEntity(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
