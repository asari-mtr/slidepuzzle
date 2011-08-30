import org.junit.Test;
import static org.junit.Assert.*;



public class SlideTest {

	@Test
	public void initField33() {
		Slide slide = new Slide(3, 3, "213456870");

		assertEquals("......213..456..870......", slide.getField());
		assertEquals("......123..456..780......", slide.getAnswer());
	}
	
	@Test
	public void initField46() {
		Slide slide = new Slide(4, 6, "214356789ABCDEFGHIJKLMN0");
		
		assertEquals(".......2143..5678..9ABC..DEFG..HIJK..LMN0.......", slide.getField());
		assertEquals(".......1234..5678..9ABC..DEFG..HIJK..LMN0.......", slide.getAnswer());
	}
	
	@Test
	public void initField65() {
		Slide slide = new Slide(6, 5, "214356789ABCDEFGHIJKLMNOPQRST0");
		
		assertEquals(".........214356..789ABC..DEFGHI..JKLMNO..PQRST0.........", slide.getField());
		assertEquals(".........123456..789ABC..DEFGHI..JKLMNO..PQRST0.........", slide.getAnswer());
	}
	
	@Test
	public void initField65_() {
		Slide slide = new Slide(6, 5, "214=56789ABCDE=GHIJKLMNO=QRST0");
		
		assertEquals(".........214=56..789ABC..DE=GHI..JKLMNO..=QRST0.........", slide.getField());
		assertEquals(".........123=56..789ABC..DE=GHI..JKLMNO..=QRST0.........", slide.getAnswer());
	}
	
	@Test
	public void random236105478() {
		Slide slide = new Slide(3, 3, "236105478");
		
		assertEquals("RR", slide.random());
	}
	
	@Test
	public void search() {
		Slide slide = new Slide(3, 3, "123456078");

		assertEquals("RR", slide.search());
	}
	
	@Test
	public void search236105478() {
		Slide slide = new Slide(3, 3, "236105478");
		
		assertEquals("RULLDDRR", slide.search());
	}
	
	@Test
	public void search840251_63() {
		Slide slide = new Slide(3, 3, "840251=63");
		
		// ê[Ç≥óDêÊÇÃèÍçá
		assertEquals("DLLURRDLLURRDDLUURDD", slide.search());
	}
	
	@Test
	public void searcg471620538() {
		Slide slide = new Slide(3,3,"471620538");
		assertEquals("LLURRDLDRULLURRDDLLURDR", slide.search());
	}
	
	@Test
	public void analyze32465871FAC0_9BE() {
		Slide slide = new Slide(4,4,"32465871FAC0=9BE");
		assertEquals("DDLLURDR", slide.search());
	}
	
	@Test
	public void search21597084ACBF__36() {
		Slide slide = new Slide(4, 4, "21597084ACBF==36");
		fail();
		// ê[Ç≥óDêÊÇÃèÍçá
		assertEquals("DLLURRDLLURRDDLUURDD", slide.search());
	}
	
	@Test
	public void valid() {
		assertEquals(1, Slide.valid(3,3,"123456708"));
		assertEquals(2, Slide.valid(3,3,"123456078"));
	}
	
	@Test
	public void vlid840251_63() {
		// ïùóDêÊÇÃèÍçá
		assertEquals(16, Slide.valid(3,3,"840251=63"));
	}
	
	@Test
	public void swap() {
		StringBuffer sb = new StringBuffer("123");
		Slide.swap(sb, 0, 2);
	}
	
	@Test
	public void check33() {
		Slide slide = new Slide(3, 3, "123456078");
		
		assertEquals("......123..456..780......",  slide.check(new StringBuffer("4411")).toString());
		assertEquals(null, slide.check(new StringBuffer("4413")));
		assertEquals(null, slide.check(new StringBuffer("442")));
		assertEquals(null,  slide.check(new StringBuffer("443")));
		assertEquals(null,  slide.check(new StringBuffer("4403")));
		assertEquals(null,  slide.check(new StringBuffer("4412")));
	}

}
