package cn.wuxian.techrd.vertx.invoice.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface UserService {
	void fetchOne(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler);

	void fetch(Handler<AsyncResult<Message<Object>>> resultHandler);

	void save(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler);

	void update(JsonObject json, Handler<AsyncResult<Message<Object>>> resultHandler);
}
