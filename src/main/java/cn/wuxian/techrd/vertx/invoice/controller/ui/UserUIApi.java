package cn.wuxian.techrd.vertx.invoice.controller.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wuxian.techrd.vertx.invoice.controller.RestApi;
import cn.wuxian.techrd.vertx.invoice.service.UserService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserUIApi implements RestApi {
	@Autowired
	UserService userService;

	@GetMapping("/")
	public void fetch(RoutingContext context) {
		userService.fetch(ar -> {
			if (ar.succeeded()) {
				context.response().end(ar.result().body().toString());
			} else {
				log.error("findAll failed: {}", ar.cause());
			}
		});
	}

	@GetMapping("/fetchOne")
	public void fetchOne(RoutingContext context) {
		String username = context.request().getParam("username");
		if (username == null) {
			context.response().setStatusCode(400).setStatusMessage("username must not empty").end();
			return;
		}
		JsonObject jsonObj = new JsonObject();
		jsonObj.put("username", username);
		userService.fetchOne(jsonObj, ar -> {
			if (ar.succeeded()) {
				context.response().end(ar.result().body().toString());
			} else {
				log.error("findOne failed: {}", ar.cause());
			}
		});
	}

	@PostMapping(value = "/save")
	public void save(RoutingContext context) {
		userService.save(context.getBodyAsJson(), ar -> {
			if (ar.succeeded()) {
				context.response().end(ar.result().body().toString());
			} else {
				log.error("save failed: {}", ar.cause());
			}
		});
	}

	@PostMapping(value = "/update")
	public void update(RoutingContext context) {
		userService.update(context.getBodyAsJson(), ar -> {
			if (ar.succeeded()) {
				context.response().end(ar.result().body().toString());
			} else {
				log.error("update failed: {}", ar.cause());
			}
		});
	}
}
