package common.DataUtilities;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;


/**
 * @author sabarinath.s
 * Date: 13-Jul-2015	
 * Time: 7:24:01 pm 
 */

public class YAMLFileParser {

	public static String getJsonString(String fileName){
		
		InputStream resourceAsStream = YAMLFileParser.class.getResourceAsStream(fileName);
		Yaml yaml = new Yaml();
		return yaml.load(resourceAsStream).toString();
	}
	
	public static <T> T readYAMLAsJavaPojo(String fileName, Class<T> c){
		
		InputStream resourceAsStream = YAMLFileParser.class.getClassLoader().getResourceAsStream(fileName);
		Yaml yaml = new Yaml();
		return yaml.loadAs(resourceAsStream, c);
	}
		
	
}
