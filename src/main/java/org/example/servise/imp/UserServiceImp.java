package org.example.servise.imp;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.config.SaTokenProperties;
import org.example.entitys.LoginUser;

import org.example.entitys.User;
import org.example.mapper.UserMapper;
import org.example.servise.UserService;
import org.example.util.PhoneValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {


    private final UserMapper userMapper;
    private final SaTokenProperties saTokenProperties;

    public UserServiceImp(UserMapper userMapper, SaTokenProperties saTokenProperties) {
        this.userMapper = userMapper;
        this.saTokenProperties = saTokenProperties;
    }

    //登录接口
    public SaResult login(@Valid LoginUser loginUser) {
        // 先去除手机号和密码的空格
        String trimmedPhone = loginUser.getPhone().trim();
        String trimmedPassword = loginUser.getPassword().trim();

        // 校验手机号格式
        if (!PhoneValidator.isValidPhone(trimmedPhone)) {
            return SaResult.error("手机号格式不正确");
        }

        //查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, trimmedPhone);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null)
            return SaResult.error("没有此用户");
        if(user.getStatus()!=0){
            return SaResult.error("账户处于异常状态");
        }else {
            String decryptedPassword = SaSecureUtil.aesDecrypt(saTokenProperties.getVerifyKey(), user.getPassword());
            if(decryptedPassword.equals(trimmedPassword)) {
                //登录成功
                StpUtil.login(trimmedPhone);
                return  SaResult.data(StpUtil.getTokenInfo());
            }else {
                return SaResult.error("密码错误");
            }
        }
    }

    @Override
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @Override
    public SaResult selectUser(Long id) {
        //1.查询信息;
        User user=userMapper.selectById(id);
        //判断用户是否存在
        if (user==null)
            return SaResult.error("用户不存在");
        //隐匿密码
        user.setPassword(null);
        return SaResult.data(user);
    }
    
    @Override
    public SaResult updateUser(User user) {
        //根据id查询用户
        User user1=userMapper.selectById(user.getId());
        if (user1==null)
            return SaResult.error("用户不存在");
        //2.有没有传递密码
        if (user.getPassword()!=null&&!"".equals(user.getPassword().trim())) {
            String s= SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(),user.getPassword().trim());
            user.setPassword(s);
        }
        userMapper.updateById(user);
        return SaResult.ok();
    }
    @Override
    public SaResult saveUser(User user) {
        //1.判断手机号格式
        if (!PhoneValidator.isValidPhone(user.getPhone()))
            return SaResult.error("手机号格式错误");
        //2.查询手机号是否被注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone, user.getPhone());
        User user1 = userMapper.selectOne(queryWrapper);
        if (user1!=null)
            return SaResult.error("手机号已被注册");
        user.setPassword(SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(),user.getPassword()));
        return userMapper.insert(user)>0?SaResult.ok():SaResult.error();
    }

    @Override
    public SaResult deletebyid(Long id) {
        //1.先查询是否有该用户
        User user=userMapper.selectById(id);
        if (user==null)
            return SaResult.error("用户不存在");
        userMapper.deleteById(id);
        return SaResult.ok();
    }
}