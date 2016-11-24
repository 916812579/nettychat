package chat.utils;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
	
   private static Properties config;
   private static final Logger logger = Logger.getLogger("Configuration");
   
   private static final String PORT_NAME = "port";
   private static int PORT;
   
   static {
		config = new Properties();
		try {
			config.load(Configuration.class.getClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			logger.warning("init configuration failed, please check file config.properties is exists");
		}
   }
   
   /**
    * 端口号
    * @return  默认 50000
    */
   public static int getPort() {
	   if (PORT != 50000 && PORT > 0) {
		   return PORT;
	   } else {
		   PORT = getInt(PORT_NAME, PORT);
	   }
	   return PORT;
   }
   
   /**
    * 字符编码
    * @return  UTF-8
    */
   public static Charset getCharset() {
	   return CharsetUtil.UTF_8;
   }
   
   
   private static int getInt(String name, int defaultValue) {
	   String value = config.getProperty(name);
	   if (value != null) {
		   try {
			   int v = Integer.parseInt(value);
			   return v;
		   } catch (Exception e) {
			   
		   }   
	   } 
	   return defaultValue;
   }

}
