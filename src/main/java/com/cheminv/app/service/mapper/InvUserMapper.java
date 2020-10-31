package com.cheminv.app.service.mapper;


import com.cheminv.app.domain.*;
import com.cheminv.app.service.dto.InvUserDTO;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link InvUser} and its DTO {@link InvUserDTO}.
 */
@Service
public class InvUserMapper  {

    public List<InvUserDTO> usersToUserDTOs(List<InvUser> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public InvUserDTO userToUserDTO(InvUser user) {
        return new InvUserDTO(user);
    }

    public List<InvUser> userDTOsToUsers(List<InvUserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }


    public InvUser userDTOToUser(InvUserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            InvUser user = new InvUser();
            user.setId(userDTO.getId());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public InvUser userFromId(Long id) {
        if (id == null) {
            return null;
        }
        InvUser user = new InvUser();
        user.setId(id);
        return user;
    }
}
