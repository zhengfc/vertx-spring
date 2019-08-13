package cn.wuxian.techrd.vertx.invoice.verticle;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import cn.wuxian.techrd.vertx.invoice.controller.RestApi;
import cn.wuxian.techrd.vertx.invoice.support.config.JavaConfig;
import cn.wuxian.techrd.vertx.invoice.support.utils.RouterUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpVerticle extends AbstractVerticle {

	protected static ApplicationContext applicationContext;

	@Override
	public void start() throws Exception {
		// 获取配置文件
		Map<String, RestApi> apis = applicationContext.getBeansOfType(RestApi.class);
		JavaConfig javaConfig = applicationContext.getBean(JavaConfig.class);

		// 注册路由
		Router restRouter = Router.router(vertx);
		apis.forEach((k, v) -> RouterUtils.injectRouter(v, restRouter));
		Router mainRouter = Router.router(vertx);
		mainRouter.mountSubRouter(javaConfig.contextPath(), restRouter);

		// 创建Server
		vertx.createHttpServer().requestHandler(mainRouter).listen(javaConfig.httpPort());
		log.info("deployId: {}, thread: {}", context.deploymentID(), Thread.currentThread());
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		HttpVerticle.applicationContext = applicationContext;
	}

}