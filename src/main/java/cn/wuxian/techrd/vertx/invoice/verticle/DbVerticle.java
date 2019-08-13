package cn.wuxian.techrd.vertx.invoice.verticle;

import static cn.wuxian.techrd.vertx.invoice.support.contants.Constants.PARAMS;
import static cn.wuxian.techrd.vertx.invoice.support.contants.Constants.ROWS;
import static cn.wuxian.techrd.vertx.invoice.support.contants.Constants.SQL;
import static cn.wuxian.techrd.vertx.invoice.support.contants.DbBus.COMMAND;
import static cn.wuxian.techrd.vertx.invoice.support.contants.DbBus.QUERY;
import static cn.wuxian.techrd.vertx.invoice.support.contants.DbBus.QUERY_ONE;

import cn.wuxian.techrd.vertx.invoice.repository.MysqlClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbVerticle extends AbstractVerticle {
	private MysqlClient mysqlClient = new MysqlClient();

	@Override
	public void start() throws Exception {
		super.start();
		mysqlClient.init(vertx);

		vertx.eventBus().<JsonObject>consumer(QUERY.queue()).handler(this::handleQuery);
		vertx.eventBus().<JsonObject>consumer(QUERY_ONE.queue()).handler(this::handleQueryOne);
		vertx.eventBus().<JsonObject>consumer(COMMAND.queue()).handler(this::handleUpdate);
		log.info("deployId: {}, thread: {}", context.deploymentID(), Thread.currentThread());
	}

	private void handleQuery(Message<JsonObject> message) {
		handleQuery(message, false);
	}

	private void handleQueryOne(Message<JsonObject> message) {
		handleQuery(message, true);
	}

	private void handleQuery(Message<JsonObject> message, boolean isOne) {
		JsonObject query = message.body();

		mysqlClient.query(query.getString(SQL), query.getJsonArray(PARAMS)).subscribe(ar -> message
				.reply(isOne ? ar.toJson().getJsonArray(ROWS).getJsonObject(0) : ar.toJson().getJsonArray(ROWS)));
	}

	private void handleUpdate(Message<JsonObject> message) {
		JsonObject update = message.body();

		mysqlClient.update(update.getString(SQL), update.getJsonArray(PARAMS))
				.subscribe(ar -> message.reply(ar.toJson()));
	}
}
