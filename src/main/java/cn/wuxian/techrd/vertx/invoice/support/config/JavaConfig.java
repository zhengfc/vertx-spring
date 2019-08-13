package cn.wuxian.techrd.vertx.invoice.support.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JavaConfig {
	@Autowired
	Environment environment;

	public int httpPort() {
		return environment.getProperty("server.port", Integer.class, 8080);
	}
	
	public String contextPath() {
		return environment.getProperty("server.context-path", String.class, "");
	}
}
