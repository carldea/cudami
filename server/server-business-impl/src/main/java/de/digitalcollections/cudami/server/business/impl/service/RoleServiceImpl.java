package de.digitalcollections.cudami.server.business.impl.service;

import de.digitalcollections.cudami.model.api.security.Role;
import de.digitalcollections.cudami.model.api.security.enums.Roles;
import de.digitalcollections.cudami.server.backend.api.repository.RoleRepository;
import de.digitalcollections.cudami.server.business.api.service.RoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
//@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService<Role, Long> {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role create() {
    return roleRepository.create();
  }

  @Override
  public Role getAdminRole() {
    return roleRepository.findByName(Role.PREFIX + Roles.ADMIN);
  }

  @Override
  public Role get(Long id) {
    return (Role) roleRepository.findOne(id);
  }

  @Override
  public List<Role> getAll() {
    return roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
  }

  @Override
  public Role findByName(String name) {
    return roleRepository.findByName(name);
  }

}
