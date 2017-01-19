package webdriver.helper.pageFactoryUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.support.FindBy;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

/**
 * @author sabarinath.s
 * Date: 29-Apr-2016	
 * Time: 2:08:23 pm 
 * 
 * This factory class returns respective findByConverter based on the findby annotation on the page element
 */

@SuppressWarnings("rawtypes")
public class FindByAnnotationConverterFactory{

	
	
	/**
	 * returns the respective findByConverter based on the annotation class type
	 * @param clzz FindBy Annotation class instance
	 * @return FindByAnnotationConverter
	 */
	public static IFindByAnnotationConverter getFindByAnnotationConverter(Class clzz){

		switch (clzz.getSimpleName()){

		case "FindBy": return new BrowserFindByAnnotationConverter<FindBy>();
		case "AndroidFindBy": return new AndroidFindByAnnotationConverter<AndroidFindBy>();
		case "iOSFindBy": return new IOSFindByAnnotationConverter<iOSFindBy>();
		default : return null;
		}
	}

	/**
	 * returns the respective findByConverter by reading the annotation type of the feild passed to this method
	 * @param field FindBy Annotation class instance
	 * @return FindByAnnotationConverter
	 */
	public static IFindByAnnotationConverter<?> getFindByAnnotationConverter(Field field){

		if(field.getAnnotation(FindBy.class)!=null){
			return new BrowserFindByAnnotationConverter<FindBy>();
		}
		else if (field.getAnnotation(AndroidFindBy.class)!=null){
			return new AndroidFindByAnnotationConverter<AndroidFindBy>();
		}
		else if(field.getAnnotation(iOSFindBy.class)!=null){
			return new IOSFindByAnnotationConverter<iOSFindBy>();
		}
		else return null;
	}

}
