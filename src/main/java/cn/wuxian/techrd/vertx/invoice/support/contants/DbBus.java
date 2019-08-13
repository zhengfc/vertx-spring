package cn.wuxian.techrd.vertx.invoice.support.contants;

public enum DbBus {
	COMMAND("db.update"), QUERY("db.query"), QUERY_ONE("db.query.one");

	private String queue;

	private DbBus(String queue) {
		this.queue = queue;
	}

	public String queue() {
		return queue;
	}
}
