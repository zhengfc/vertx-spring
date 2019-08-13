package cn.wuxian.techrd.vertx.invoice.repository;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MysqlClient {

	AsyncSQLClient client;

	public void init(Vertx vertx) {
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file").setOptional(true)
				.setConfig(new JsonObject().put("path", "mysql.json"));
		ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
		
		Future<JsonObject> future = ConfigRetriever.getConfigAsFuture(retriever);
		future.setHandler(ar -> {
			if (ar.failed()) {
				log.error("init mysqlClient fail", ar.cause());
			} else {
				this.client = MySQLClient.createShared(vertx, ar.result());
			}
		});
	}

	public Mono<ResultSet> query(String sql, JsonArray params) {
		Promise<ResultSet> promise = Promise.promise();
		client.queryWithParams(sql, (params == null) ? new JsonArray() : params, promise.future());

		return Mono.create(sink -> promise.future().setHandler(ar -> {
			if (ar.succeeded()) {
				sink.success(ar.result());
			} else {
				sink.error(ar.cause());
			}
		}));
	}

	public Mono<UpdateResult> update(String sql, JsonArray params) {
		Promise<UpdateResult> promise = Promise.promise();
		client.updateWithParams(sql, (params == null) ? new JsonArray() : params, promise.future());

		return Mono.create(sink -> promise.future().setHandler(ar -> {
			if (ar.succeeded()) {
				sink.success(ar.result());
			} else {
				sink.error(ar.cause());
			}
		}));
	}
}