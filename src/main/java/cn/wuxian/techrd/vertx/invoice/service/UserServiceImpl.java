package cn.wuxian.techrd.vertx.invoice.service;

import static cn.wuxian.techrd.vertx.invoice.support.contants.DbBus.*;

import org.springframework.stereotype.Service;

import cn.wuxian.techrd.vertx.invoice.support.utils.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends AbstractVerticle implements UserService {

	@Override
	public void fetchOne(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler) {
		String sql = "select * from user where username=:username";
		JsonObject queryObj = JsonUtils.mapParams(JsonUtils.of(json, sql));
		vertx.eventBus().request(QUERY_ONE.queue(), queryObj, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(ar);
			} else {
				log.error("query failed: {}", ar.cause());
			}
		});
	}

	@Override
	public void fetch(Handler<AsyncResult<Message<Object>>> resultHandler) {
		JsonObject queryObj = JsonUtils.of("select * from user");
		vertx.eventBus().request(QUERY.queue(), queryObj, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(ar);
			} else {
				log.error("query failed: {}", ar.cause());
			}
		});
	}

	@Override
	public void save(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler) {
		String sql = "insert into user(username, email, mobile, dep_id, create_time) values (:username, :email, :mobile, :dep_id, now())";
		JsonObject saveObj = JsonUtils.mapParams(JsonUtils.of(json, sql));
		vertx.eventBus().request(COMMAND.queue(), saveObj, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(ar);
			} else {
				log.error("save failed: {}", ar.cause());
			}
		});
	}

	@Override
	public void update(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler) {
		String sql = "update user set email=:email, mobile=:mobile, dep_id=:dep_id where username=:username";
		JsonObject updateObj = JsonUtils.mapParams(JsonUtils.of(json, sql));
		vertx.eventBus().request(COMMAND.queue(), updateObj, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(ar);
			} else {
				log.error("update failed: {}", ar.cause());
			}
		});
	}

}
