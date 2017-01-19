package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Function;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import webdriver.helper.MobileElement;

/**
 * @author sabarinath.s
 *
 * @param <T>
 */
public class IOSFindByAnnotationConverter<T> implements IFindByAnnotationConverter<iOSFindBy> {

	private static Map<Function<iOSFindBy, String>, Function<String, By>> iosFindByMap = new HashMap<>();

	static{

		//TODO
		// populate ios findby Map
	}

	@Override
	public By getByfromFindBy(iOSFindBy findBy) {


		By by = null;
		for (Function<iOSFindBy, String> func : iosFindByMap.keySet()) {
			String value = func.apply(findBy);
			if (StringUtils.isNotEmpty(value)) {
				by = iosFindByMap.get(func).apply(value);
			}
		}
		return by;
	}

	@Override
	public By getByforField(Field f) {

		By by = null;
		iOSFindBy findBy = f.getAnnotation(iOSFindBy.class);
		if(findBy != null){

			for (Function<iOSFindBy, String> func : iosFindByMap.keySet()) {
				String value = func.apply(findBy);
				if (StringUtils.isNotEmpty(value)) {
					by = iosFindByMap.get(func).apply(value);
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
