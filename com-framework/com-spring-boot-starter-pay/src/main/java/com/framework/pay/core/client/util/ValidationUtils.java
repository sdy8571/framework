package com.framework.pay.core.client.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author shen_dy@halcyonz.com
 * @date 2024/4/9
 */
public class ValidationUtils {

	public static void validate(Object object, Class<?>... groups) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Assert.notNull(validator);
		validate(validator, object, groups);
	}

	public static void validate(Validator validator, Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		if (CollUtil.isNotEmpty(constraintViolations)) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

}
