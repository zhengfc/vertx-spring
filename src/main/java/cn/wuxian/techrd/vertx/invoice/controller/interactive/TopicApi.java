package cn.wuxian.techrd.vertx.invoice.controller.interactive;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wuxian.techrd.vertx.invoice.controller.RestApi;
import io.vertx.ext.web.RoutingContext;

@Controller
@RequestMapping("/api")
public class TopicApi implements RestApi {
	@GetMapping("/fetchOne")
	public void fetchOne(RoutingContext context) {
		context.response().end("hello topic getOne!");
	}
}
