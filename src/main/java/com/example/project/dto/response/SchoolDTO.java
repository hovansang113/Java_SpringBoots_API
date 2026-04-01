package com.example.project.dto.response;

public class SchoolDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String level;

    public SchoolDTO(Long id, String name, String address, String phone, String email, String level) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLevel() {
        return level;
    }

}
