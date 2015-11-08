package unit.crud;

import java.util.Arrays;

import org.junit.Test;

import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.nubes.orm.queries.ListAndCount;

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
			findBy.eq("name", "Snowy");
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
		crit.eq("name", "Snoopy");
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
	
	@Test
	public void insertAndFindMany(TestContext context) {
		Async async = context.async();
		Dog snowy = new Dog("Snowy", "Fox");
		mongo.create(snowy, res -> {
			context.assertFalse(res.failed());
			Dog result = res.result();
			context.assertEquals(snowy, result);
			FindBy<Dog> findBy = new FindBy<>(Dog.class);
			findBy.in("breed", Arrays.asList("Fox", "Beagle"));
			mongo.listAndCount(findBy, 0, 100, findRes -> {
				context.assertFalse(findRes.failed());
				ListAndCount<Dog> findResult = findRes.result();
				context.assertEquals(2l, findResult.count);
				async.complete();
			});
		});
	}
	
	@Test
	public void insertAndFindOne(TestContext context) {
		Async async = context.async();
		Dog snowy = new Dog("Snowy", "Fox");
		mongo.create(snowy, res -> {
			context.assertFalse(res.failed());
			Dog result = res.result();
			context.assertEquals(snowy, result);
			FindBy<Dog> findBy = new FindBy<>(Dog.class);
			findBy.in("breed", Arrays.asList("Beagle"));
			mongo.listAndCount(findBy, 0, 100, findRes -> {
				context.assertFalse(findRes.failed());
				ListAndCount<Dog> findResult = findRes.result();
				context.assertEquals(1l, findResult.count);
				async.complete();
			});
		});
	}
}
