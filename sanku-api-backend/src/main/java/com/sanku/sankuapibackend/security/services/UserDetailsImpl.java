package com.sanku.sankuapibackend.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanku.sankuapibackend.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

  public static UserDetailsImpl build(Usuario user) {
        // --- CORRECCIÓN: Forma simplificada y correcta de obtener el rol ---
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRol().getNombrerol().name()));

        return new UserDetailsImpl(
                user.getIdusuario(),
                user.getEmail(),
                user.getContrasenia(),
                authorities);
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Métodos de UserDetails (isAccountNonExpired, etc.)
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}