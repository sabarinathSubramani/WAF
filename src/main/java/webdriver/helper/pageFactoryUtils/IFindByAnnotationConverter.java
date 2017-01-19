package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

import webdriver.helper.PageElement;

/**
 * @author sabarinath.s
 *
 *Any new findBy Type has to implement this class
 * @param <T>
 */
public interface IFindByAnnotationConverter<T> {

	public By getByfromFindBy(T t);
	
	public By getByforField(Field f);
	
	public PageElement createWebElement(String name, By by);
	
}
