package com.framework.mybatis.core.mapper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
@Data
public class SortingField implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ORDER_ASC = "asc";
	public static final String ORDER_DESC = "desc";
	private String field;
	private String order;

}
