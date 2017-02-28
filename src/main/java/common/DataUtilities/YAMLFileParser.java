package common.DataUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static <T> T readYAMLAsJavaPojo(File file, Class<T> c) throws FileNotFoundException{
		
		FileInputStream fis = new FileInputStream(file);
		Yaml yaml = new Yaml();
		return yaml.loadAs(fis, c);
	}
	
	
	public static void writeToYaml(Object o, File file) throws IOException{
		Yaml yaml = new Yaml();
		FileWriter writer = new FileWriter(file);
		yaml.dump(o, writer);
	}
		
	
}
