package cn.wuxian.techrd.vertx.invoice.support.utils;

import static cn.wuxian.techrd.vertx.invoice.support.contants.Constants.PARAMS;
import static cn.wuxian.techrd.vertx.invoice.support.contants.Constants.SQL;

import org.springframework.jdbc.core.namedparam.NamedParameterUtils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonUtils {
	private JsonUtils() {
	}

	public static JsonObject of(String sql) {
		JsonObject jsonObj = new JsonObject();
		jsonObj.put(SQL, sql);
		return jsonObj;
	}

	public static JsonObject of(JsonObject fromObj, String sql) {
		JsonObject jsonObj = new JsonObject(fromObj.getMap());
		jsonObj.put(SQL, sql);
		return jsonObj;
	}

	public static JsonObject mapParams(JsonObject fromObj) {
		JsonObject retObject = new JsonObject();
		String sql = fromObj.getString(SQL);
		Object[] valueArr = NamedParameterUtils.buildValueArray(sql, fromObj.getMap());
		if (valueArr.length == 0) {
			retObject.put(SQL, sql);
		} else {
			JsonArray jsonArr = new JsonArray();
			for (Object param : valueArr) {
				jsonArr.add(param);
			}
			retObject.put(PARAMS, jsonArr);
			retObject.put(SQL, NamedParameterUtils.parseSqlStatementIntoString(sql));
		}
		return retObject;
	}
}
