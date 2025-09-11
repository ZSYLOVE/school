package org.example.servise;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entitys.LoginUser;
import org.example.entitys.User;

public interface UserService extends IService<User> {
    SaResult login(LoginUser loginUser);
}