package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import com.google.common.base.Function;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import webdriver.helper.MobileElement;
import webdriver.helper.PageElement;


/**
 * @author sabarinath.s
 *
 * @param <T>
 */
public class AndroidFindByAnnotationConverter<T> implements IFindByAnnotationConverter<AndroidFindBy> {

	private static Map<Function<AndroidFindBy, String>, Function<String, By>> androidFindByMap = new HashMap<>();

	static{
		androidFindByMap.put(AndroidFindBy::id, By::id);
		androidFindByMap.put(AndroidFindBy::tagName, MobileBy::tagName);
		androidFindByMap.put(AndroidFindBy::xpath, By::xpath);
		androidFindByMap.put(AndroidFindBy::uiAutomator, MobileBy::AndroidUIAutomator);
		androidFindByMap.put(AndroidFindBy::accessibility, MobileBy::AccessibilityId);
		androidFindByMap.put(AndroidFindBy::className, By::className);
	}

	@Override
	public By getByfromFindBy(AndroidFindBy findBy) {


		By by = null;
		for (Function<AndroidFindBy, String> func : androidFindByMap.keySet()) {
			String value = func.apply(findBy);
			if (StringUtils.isNotEmpty(value)) {
				by = androidFindByMap.get(func).apply(value);
			}
		}
		return by;
	}

	@Override
	public By getByforField(Field f) {

		By by = null;
		AndroidFindBy findBy = f.getAnnotation(AndroidFindBy.class);
		if(findBy != null){

			for (Function<AndroidFindBy, String> func : androidFindByMap.keySet()) {
				String value = func.apply(findBy);
				if (StringUtils.isNotEmpty(value)) {
					by = androidFindByMap.get(func).apply(value);
				}
			}
		}
		return by;
	}

	@Override
	public MobileElement createWebElement(String name, By by) {
		return new MobileElement(name , by);
	}
}
