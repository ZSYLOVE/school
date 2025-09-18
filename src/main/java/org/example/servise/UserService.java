package org.example.servise;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entitys.LoginUser;
import org.example.entitys.User;

import javax.validation.Valid;

public interface UserService extends IService<User> {
    SaResult login(@Valid LoginUser loginUser);
    SaResult logout();
    SaResult selectUser(Long id);
    SaResult updateUser(User user);
    SaResult saveUser(User user);
    SaResult deletebyid(Long id);
}