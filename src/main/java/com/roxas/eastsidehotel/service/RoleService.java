package com.roxas.eastsidehotel.service;

import com.roxas.eastsidehotel.exception.RoleAlreadyExistException;
import com.roxas.eastsidehotel.exception.UserAlreadyExistsException;
import com.roxas.eastsidehotel.model.Role;
import com.roxas.eastsidehotel.model.User;
import com.roxas.eastsidehotel.repository.RoleRepository;
import com.roxas.eastsidehotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role role) {
        String roleName = "ROLE_"+role.getName().toUpperCase();
        Role role1 = new Role(roleName);
        if (roleRepository.existsByName(roleName))
            throw new RoleAlreadyExistException(role.getName()+" role already exists");
        return roleRepository.save(role1);
    }

    @Override
    public void deleteRole(Long id) {
        this.removeAllUsersFromRole(id);
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())) {
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())) {
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+ " is already assigned to the" + role.get().getName()+ " role");
        }
        if (role.isPresent()) {
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role::removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
