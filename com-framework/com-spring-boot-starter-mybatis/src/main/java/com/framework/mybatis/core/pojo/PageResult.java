package com.framework.mybatis.core.pojo;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/3/30
 */
@Data
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> list;
	private Long total;

	@Getter
	private Long currentPage;
	@Getter
	private Long totalPage;

	public PageResult() {
	}

	public PageResult(List<T> list, Long total) {
		this.list = list;
		this.total = total;
	}

	public PageResult(Long total) {
		this.list = new ArrayList<>();
		this.total = total;
	}

	public PageResult(List<T> list, Long total, Long currentPage, Long totalPage) {
		this.list = list;
		this.total = total;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
	}

	public boolean hasContent() {
		return list != null && list.size() > 0;
	}

	public boolean isLast() {
		return currentPage == null || totalPage == null || currentPage >= totalPage;
	}

	public static <T> PageResult<T> empty() {
		return new PageResult<>(0L);
	}

	public static <T> PageResult<T> empty(Long total) {
		return new PageResult<>(total);
	}

}
