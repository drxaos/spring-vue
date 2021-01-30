package com.example.demo.db.model.auth;

import com.example.demo.db.model.Persistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails, Persistable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    int version;

    @Column(name = "username", unique = true)
    String username;

    @Column(name = "created")
    Date created;

    @Column(name = "active")
    boolean active;

    @Column(name = "real_name")
    String realName;

    @Column(name = "password")
    String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "`user_role`", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toList());
    }

    public User(User user) {
        this.id = user.id;
        this.version = user.version;
        this.username = user.username;
        this.password = user.password;
        this.realName = user.realName;
        this.active = user.active;
        this.roles = user.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        return persistableEquals(this, o);
    }

    @Override
    public int hashCode() {
        return persistableHashCode(this);
    }
}
