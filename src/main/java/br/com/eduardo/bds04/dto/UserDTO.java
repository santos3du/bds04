package br.com.eduardo.bds04.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.com.eduardo.bds04.entities.User;

public class UserDTO implements Serializable {
    public final static long serialVersionUID = 1L;
    private Long id;

    @NotBlank(message = "Campo requerido")
    @Email(message = "Entrar com e-mail válido")
    private String email;

    Set<RoleDTO> roles = new HashSet<RoleDTO>();

    public UserDTO() {}

    public UserDTO (Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.email = email;
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
