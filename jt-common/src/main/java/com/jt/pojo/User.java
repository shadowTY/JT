package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_user")
@Data
@Accessors(chain = true)
public class User extends BasePojo{
	@TableId(type = IdType.AUTO)   // 主键自增
	private Long id;
	private String username;
	private String password;	// 加密保存 md5
	private String phone;
	private String email;

}
