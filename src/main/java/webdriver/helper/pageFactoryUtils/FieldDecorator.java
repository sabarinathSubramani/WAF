package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.FindBy;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import webdriver.helper.MobileElement;
import webdriver.helper.PageElement;

/**
 * @author sabarinath.s
 *
 * Customized field decorator to initialize page elements using pageFacorty.initElements method
 */
public class FieldDecorator extends AppiumFieldDecorator {

	public FieldDecorator(SearchContext context) {
		super(context);
	}

	public FieldDecorator(SearchContext context, long implicitlyWaitTimeOut, TimeUnit timeUnit){
		super(context, implicitlyWaitTimeOut,timeUnit);

	}

	@SuppressWarnings("rawtypes")
	private static Class[] decoratableElementList = {PageElement.class, MobileElement.class};

	public boolean isDecoratableElement(Class<?> fieldType){

		for(Class<?> clz :decoratableElementList){
			return clz.isAssignableFrom(fieldType); 
		}
		return false;
	}

	@Override
	public Object decorate(ClassLoader ignored, Field field) {

		Object webElement = super.decorate(ignored, field);

		if(webElement == null){
			if(isDecoratableElement(field.getType())){

				IFindByAnnotationConverter<?> findByAnnotationConverter = FindByAnnotationConverterFactory.getFindByAnnotationConverter(field);
				By byforField = findByAnnotationConverter.getByforField(field);
				if(byforField != null)
					webElement = findByAnnotationConverter.createWebElement(field.getName(), byforField);
			}
		}
		return webElement;
	}






}
