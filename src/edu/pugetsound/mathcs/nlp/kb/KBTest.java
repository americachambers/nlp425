import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;

public class KBTest{
	@Test
	public void isThere(){
		KBController kb = new KBController();
		PrologStructure p = new PrologStructure(2);
		p.setName("isA");
		p.addArgument("fluffy", 0);
		p.addArgument("cat",1);
		assertTrue(kb.yesNo(p));
	}

	@Test
	public void notThere(){
		KBController kb = new KBController();
		PrologStructure p = new PrologStructure(2);
		p.setName("isA");
		p.addArgument("spot", 0);
		p.addArgument("cat",1);
		assertFalse(kb.yesNo(p));
	}

	@Test
	public void prologException(){
		KBController kb = new KBController("faultyProlog.pl");
		PrologStructure p = new PrologStructure(2);
		assertTrue(kb.yesNo(p));
		fail("An exception should have been thrown");
	}

}
