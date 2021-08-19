package com.brandanswers.dashboard.orchestrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.apache.commons.validator.GenericValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ValidationProcessor {

	private static final Map<String, ValidatorWrapper> validationMap = new HashMap<String, ValidatorWrapper>();

	@PostConstruct
	private void init() {
		validationMap.put("Email", new ValidatorWrapper((value, params) -> {
			return GenericValidator.isEmail(String.valueOf(value));
		},"Invalid EmailId - %s"));
		validationMap.put("Required", new ValidatorWrapper((value, params) -> {
			return ValidationProcessor.isRequired(value);
		},"%s is required"));

		validationMap.put("CreditCard", new ValidatorWrapper((value, params) -> {
			return GenericValidator.isCreditCard(String.valueOf(value));
		},"Invalid Credit card - %s"));

		validationMap.put("Url", new ValidatorWrapper((value, params) -> {
			return GenericValidator.isUrl(String.valueOf(value));
		},"Invalid Url - %s"));

		validationMap.put("Int", new ValidatorWrapper((value, params) -> {
			return GenericValidator.isInt(String.valueOf(value));
		},"Value is not integer - %s"));

		validationMap.put("Float", new ValidatorWrapper((value, params) -> {
			return GenericValidator.isFloat(String.valueOf(value));
		},"Value is not float - %s"));

		validationMap.put("Max", new ValidatorWrapper((value, params) -> {
			if (GenericValidator.isInt(String.valueOf(value))) {
				return GenericValidator.maxValue((int) value, Integer.valueOf(params[1].trim()));
				
			} else {
				return GenericValidator.maxValue((float) value, Float.valueOf(params[1].trim()));
			}

		},"Current value exceeds the maximum value"));

		validationMap.put("Min", new ValidatorWrapper((value, params) -> {
			if (GenericValidator.isInt(String.valueOf(value))) {
				return GenericValidator.minValue((int) value, Integer.valueOf(params[1].trim()));
			} else {
				return GenericValidator.minValue((float) value, Float.valueOf(params[1].trim()));
			}

		},"Current value is less than min value"));

		validationMap.put("MaxLength", new ValidatorWrapper((value, params) -> {
			return GenericValidator.maxLength(String.valueOf(value), Integer.valueOf(params[1].trim()));
		},"Current length exceeds tha maximum length"));

		validationMap.put("MinLength", new ValidatorWrapper((value, params) -> {
			return GenericValidator.minLength(String.valueOf(value), Integer.valueOf(params[1].trim()));
		},"Current length is less than minimum length."));

		validationMap.put("Regex", new ValidatorWrapper((value, params) -> {
			return GenericValidator.matchRegexp(String.valueOf(value), params[1].trim());
		},"Invalid pattern matching"));

	}

	public List<String> validate(String param, Object value, List<String> validations)
			throws ReflectiveOperationException {
		List<String> errorMessage = new ArrayList<String>();
		for (String validation : validations) {
			ValidatorWrapper valDataWrapper = this.getValidationWrapper(validation);
			if (valDataWrapper != null) {
				String[] params = validation.split("\\|");
				if (!valDataWrapper.validate(value, params)) {
					errorMessage.add(String.format(valDataWrapper.getMessage(), String.valueOf(value)));
				}

			}

		}
		return errorMessage;
	}

	private ValidatorWrapper getValidationWrapper(String validation) {
		String[] methodParts = validation.split("\\|");
		return validationMap.get(methodParts[0].trim());
	}

	public static boolean isRequired(Object value) {
		String data = value == null ? "" : String.valueOf(value);
		return !GenericValidator.isBlankOrNull(data);
	}
}

class ValidatorWrapper {
	String message;
	private BiFunction<Object, String[], Boolean> validateFunc;

	public String getMessage() {
		return message;
	}
	public boolean validate(Object value, String[] params) {
		return this.validateFunc.apply(value, params);
	}

	public ValidatorWrapper(BiFunction<Object, String[], Boolean> validate,String message) {
		super();
		this.validateFunc = validate;
		this.message = message;
	}
}
