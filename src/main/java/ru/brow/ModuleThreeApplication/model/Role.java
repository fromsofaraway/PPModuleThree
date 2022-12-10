package ru.brow.ModuleThreeApplication.model;

import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private List<User> users;

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {
    }


    @Override
    public String getAuthority() {
        return getName();
    }

    public String getName() {
        return name;
    }


    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
