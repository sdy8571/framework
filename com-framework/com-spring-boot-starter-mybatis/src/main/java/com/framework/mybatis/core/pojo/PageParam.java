package com.framework.mybatis.core.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
@Data
public class PageParam implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Integer PAGE_NO = 1;
  private static final Integer PAGE_SIZE = 10;

  private Integer pageNo;
  private Integer pageSize;

}
