package com.academicbrain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户数据传输对象
 * @author AcademicBrain
 */
@Data
@ApiModel("用户DTO")
public class UserDTO implements Serializable {

    @ApiModelProperty("用户ID")
    private String id;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("确认密码")
    private String confirmPassword;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("验证码")
    private String code;
} 