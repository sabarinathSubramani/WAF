package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Function;

import webdriver.helper.PageElement;

/**
 * @author sabarinath.s
 *
 * @param <T>
 */
public class BrowserFindByAnnotationConverter<T> implements IFindByAnnotationConverter<FindBy> {

	private static Map<Function<FindBy, String>, Function<String, By>> findByMap = new HashMap<>();


	static{
		findByMap.put(FindBy::id, By::id);
		findByMap.put(FindBy::css, By::cssSelector);
		findByMap.put(FindBy::name, By::name);
		findByMap.put(FindBy::tagName, By::tagName);
		findByMap.put(FindBy::xpath, By::xpath);
		findByMap.put(FindBy::linkText, By::linkText);
		findByMap.put(FindBy::partialLinkText, By::partialLinkText);
		findByMap.put(FindBy::className, By::className);
	}

	@Override
	public By getByfromFindBy(FindBy findBy) {

		By by = null;
		for (Function<FindBy, String> func : findByMap.keySet()) {
			String value = func.apply(findBy);
			if (StringUtils.isNotEmpty(value)) {
				by = findByMap.get(func).apply(value);
			}
		}
		return by;
	}

	@Override
	public By getByforField(Field f) {

		By by = null;
		FindBy findBy = f.getAnnotation(FindBy.class);
		if(findBy != null){

			for (Function<FindBy, String> func : findByMap.keySet()) {
				String value = func.apply(findBy);
				if (StringUtils.isNotEmpty(value)) {
					by = findByMap.get(func).apply(value);
				}
			}
		}
		return by;
	}

	@Override
	public PageElement createWebElement(String name, By by) {
		return new PageElement(name , by);
	}

}
