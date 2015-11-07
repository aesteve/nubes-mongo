package unit.crud;

import org.junit.Test;

import com.github.aesteve.nubes.orm.queries.FindBy;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import mock.models.Dog;
import unit.TestBase;

public class CrudSpec extends TestBase {

	@Test
	public void insertAndFind(TestContext context) {
		Async async = context.async();
		Dog snowy = new Dog("Snowy", "Fox");
		mongo.create(snowy, res -> {
			context.assertFalse(res.failed());
			Dog result = res.result();
			context.assertEquals(snowy, result);
			FindBy<Dog> findBy = new FindBy<>(Dog.class);
			findBy.addRestriction("name", "Snowy");
			mongo.findBy(findBy, findRes -> {
				context.assertFalse(findRes.failed());
				Dog findResult = findRes.result();
				context.assertEquals(snowy, findResult);
				async.complete();
			});
		});
	}
	
	@Test
	public void update(TestContext context) {
		Async async = context.async();
		Dog snoop = new Dog("Snoopy", "NoLongerABeagle");
		FindBy<Dog> crit = new FindBy<>(Dog.class);
		crit.addRestriction("name", "Snoopy");
		mongo.update(snoop, crit, res -> {
			context.assertFalse(res.failed());
			Dog result = res.result();
			context.assertEquals(snoop, result);
			mongo.findBy(crit, findRes -> {
				context.assertFalse(findRes.failed());
				Dog findResult = findRes.result();
				context.assertEquals(snoop, findResult);
				async.complete();
			});
		});
	}
}
