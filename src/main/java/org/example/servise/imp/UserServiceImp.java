package org.example.servise.imp;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entitys.LoginUser;
import org.example.entitys.User;
import org.example.mapper.UserMapper;
import org.example.servise.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@Transactional
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {


    private final UserMapper userMapper;

    public UserServiceImp(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //登录接口
    public SaResult login(LoginUser loginUser) {
        //查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone,loginUser.getPhone());
        User user = userMapper.selectOne(queryWrapper);
        if(user==null) return SaResult.error("没有此用户");
        if(user.getStatus()!=0){
            return SaResult.error("账户处于异常状态");
        }else if(user.getPassword().equals(loginUser.getPassword())) {
            //登录成功
            StpUtil.login(loginUser.getPhone());
            return  SaResult.data(StpUtil.getTokenInfo());
        }else {
            return SaResult.error("密码错误");
        }
    }

    /**
     * 退出接口     发现一个问题，就是请求这个接口必须要携带token，如果没有携带token会导致redis中的数据还存在
     * @return
     */
    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }
}