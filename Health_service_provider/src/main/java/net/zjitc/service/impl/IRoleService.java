package net.zjitc.service.impl;

import net.zjitc.service.RoleService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = RoleService.class)
@Transactional
public class IRoleService implements RoleService {
}
