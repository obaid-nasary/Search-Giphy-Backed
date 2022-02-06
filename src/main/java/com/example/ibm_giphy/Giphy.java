package com.example.ibm_giphy;

import javax.persistence.*;

@Table
@Entity
public class Giphy {

    @Id
    @SequenceGenerator(
            name = "giphy_sequence",
            sequenceName = "giphy_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "giphy_sequence"
    )

    private Long id;
    private String name;
    private String username;
    private String password;
    private String keywords;

    public Giphy() {
    }

    public Giphy(Long id, String name, String username, String password, String keywords) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.keywords = keywords;
    }

    public Giphy(String name, String username, String password, String keywords) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.keywords = keywords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    @Override
    public String toString() {
        return "Giphy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
