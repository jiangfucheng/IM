package com.jiangfucheng.im.auth.controller;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.resp.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 16:37
 *
 * @author jiangfucheng
 */
@RestControllerAdvice
public class GlobalExceptionResolver {

	@ExceptionHandler(ConstraintViolationException.class)
	public Response resolveException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		StringBuilder builder = new StringBuilder();
		constraintViolations.forEach(constraintViolation -> builder.append(constraintViolation.getMessage()).append(","));
		String errorMessage = builder.toString();
		if (errorMessage.length() > 1) {
			errorMessage = errorMessage.substring(0, builder.length() - 1);
		}
		return Response.error(ErrorCode.PARAM_ERROR, errorMessage);
	}

}
