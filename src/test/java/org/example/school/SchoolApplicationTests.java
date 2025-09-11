package org.example.school;

import org.example.entitys.User;
import org.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SchoolApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
    public void test(){
        User build=User.builder().name("xiaogu").password("123456").phone("17723299233").remark("dsgahdsah").build();
        userMapper.insert(build);
    }
    @Test
    public void test2(){

    }
}