import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class TestMain {

	@Test
	void MainNullArgs() {
		Main.main(null);
	}

	@Test
	void MainSleep() {
		long beforeTimeMillis = System.currentTimeMillis();
		Main.main(new String[] { "sleep(1234)" });
		long afterTimeMillis = System.currentTimeMillis();
		assertTrue(beforeTimeMillis + 1200 < afterTimeMillis);
	}

}
