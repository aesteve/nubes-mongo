package unit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.github.aesteve.nubes.orm.mongo.services.MongoService;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import mock.models.Dog;

@RunWith(VertxUnitRunner.class)
public abstract class TestBase {
	
	public final static int MONGO_PORT = 8889;
	
	protected MongodExecutable mongod;
	protected Vertx vertx;
	protected MongoService mongo;
	
	@Before
	public void setUp(TestContext context) {
		Async async = context.async();
		Vertx vertx = Vertx.vertx();
		vertx.executeBlocking(future -> {
			try {
				MongodStarter starter = MongodStarter.getDefaultInstance();
				MongodConfigBuilder builder = new MongodConfigBuilder();
				builder.version(Version.Main.PRODUCTION);
				builder.net(new Net(MONGO_PORT, Network.localhostIsIPv6()));
				mongod = starter.prepare(builder.build());
				mongod.start();
				future.complete();
			} catch(Exception e) {
				future.fail(e);
			}
		}, res -> {
			if (res.failed()) {
				context.fail(res.cause());
			} else {
				mongo = new MongoService();
				mongo.init(vertx, config());
				Future<Void> fut = Future.future();
				fut.setHandler(mongoRes -> {
					if (mongoRes.failed()) {
						context.fail(mongoRes.cause());
					} else {
						mongo.create(new Dog("Snoopy", "Beagle"), createRes -> {
							if (createRes.failed()) {
								context.fail(createRes.cause());
							} else {
								async.complete();
							}
						});
					}
				});
				mongo.start(fut);
			}
		});
	}
	
	@After
	public void tearDown(TestContext context) {
		mongod.stop();
	}
	
	protected JsonObject config() {
		JsonObject json = new JsonObject();
		json.put("host", "localhost");
		json.put("port", MONGO_PORT);
		return json;
	}
}
