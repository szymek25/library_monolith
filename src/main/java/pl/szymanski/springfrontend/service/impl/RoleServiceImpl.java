package pl.szymanski.springfrontend.service.impl;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szymanski.springfrontend.dao.RoleDao;
import pl.szymanski.springfrontend.model.Role;
import pl.szymanski.springfrontend.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleDao roleDao;

  @Override
  public List<Role> getAll() {
    return Lists.newArrayList(roleDao.findAll());
  }

  @Override
  public Role getById(long id) {
    return roleDao.findById(id);
  }

  @Override
  public Role getByName(String name) {
    return roleDao.findByName(name);
  }
}
