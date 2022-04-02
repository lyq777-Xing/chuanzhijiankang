package net.zjitc.service;

import net.zjitc.entity.User;

public interface UserService {
    User findByUsername(String username);

    String getAuthorityInfo(int userid);
}
