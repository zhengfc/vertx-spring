package cn.wuxian.techrd.vertx.invoice.support.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.wuxian.techrd.vertx.invoice.controller.RestApi;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouterUtils {
	RouterUtils() {
	}

	public static void injectRouter(RestApi api, Router router) {
		Map<Method, RequestMapping> annotatedMethods = MethodIntrospector.selectMethods(api.getClass(),
				(MetadataLookup<RequestMapping>) method -> AnnotatedElementUtils.findMergedAnnotation(method,
						RequestMapping.class));
		RequestMapping annotatedClass = api.getClass().getDeclaredAnnotation(RequestMapping.class);
		annotatedMethods.forEach((method, request) -> {
			Class<?>[] params = method.getParameterTypes();
			Assert.isAssignable(RoutingContext.class, params[0]);
			router.route().handler(BodyHandler.create());
			String path = annotatedClass.value()[0] + request.value()[0];
			router.route(convert(request.method()), path).produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.handler(context -> {
						try {
							context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
							method.invoke(api, context);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							log.error("e :", e.getCause());
						}
					});
		});
	}

	private static HttpMethod convert(RequestMethod[] methods) {
		if (methods.length == 0)
			return HttpMethod.GET;
		return HttpMethod.valueOf(methods[0].name());
	}

}
