package com.framework.mybatis.core.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/29
 */
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 创建者 */
	private String createBy;

	/** 创建时间 */
	private Date createTime;

	/** 更新者 */
	private String updateBy;

	/** 更新时间 */
	private Date updateTime;

	private boolean deleted;

}
