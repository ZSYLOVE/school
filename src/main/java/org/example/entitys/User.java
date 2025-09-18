package org.example.entitys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.example.comment.Valida;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    //id
    @NotNull(message = "id不能为空", groups = {Valida.Update.class})
    private Long id;
    //名字
    @NotNull(message = "名字不能为空", groups = {Valida.Create.class})
    private String name;
    //电话
    @NotNull(message = "电话不能为空", groups = {Valida.Create.class})
    private String phone;
    //密码
    @NotNull(message = "密码不能为空", groups = {Valida.Create.class})
    private String password;
    //简介
    private String remark;
    //状态
    private Integer status;
    //提交时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
}