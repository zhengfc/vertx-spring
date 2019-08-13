package cn.wuxian.techrd.vertx.invoice;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import cn.wuxian.techrd.vertx.invoice.verticle.DbVerticle;
import cn.wuxian.techrd.vertx.invoice.verticle.HttpVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {

	@Autowired 
	ApplicationContext context;
	@Autowired
	private List<Verticle> verticles;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void deployVerticle() {
		Vertx vertx = Vertx.vertx();
		
		log.info("deply vertx start...... ");
		//deploy services
		verticles.forEach(vertx::deployVerticle);
		
		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(4);
		
		//deploy multiple httpServer
		HttpVerticle.setApplicationContext(context);
		vertx.deployVerticle(HttpVerticle.class.getName(), options);
		
		//deploy multiple database event consumer
		vertx.deployVerticle(DbVerticle.class.getName(), options);
		log.info("deply vertx end...... ");
	}
}
