package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_cat")
public class ItemCat extends BasePojo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6708521607661154233L;
	@TableId(type = IdType.AUTO)
	private Long id;			// id号        
	private Long parentId;		// 父级id号
	private String name;		// 节点名称
	private Integer status;		// 状态
	private Integer sortOrder;	// 排序号
	private Boolean isParent;	// 是否为父级

}
