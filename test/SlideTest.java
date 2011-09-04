import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class SlideTest {
	
	@Test
	public void Stack1() {
		Slide.Step step = new Slide.Step();
		assertEquals(2,  step.index);
		assertEquals(4,  step.steps[0]);
		assertEquals(4,  step.steps[1]);
		step.add(2);
		
		assertEquals(3,  step.index);
		assertEquals(4,  step.steps[0]);
		assertEquals(4,  step.steps[1]);
		assertEquals(2,  step.steps[2]);
	}
	
	@Test
	public void Stack2() {
		Slide.Step step = new Slide.Step(new int[]{4,4,2});
		assertEquals(3,  step.index);
		assertEquals(4,  step.steps[0]);
		assertEquals(4,  step.steps[1]);
		step.add(1);
		
		assertEquals(4,  step.index);
		assertEquals(4,  step.steps[0]);
		assertEquals(4,  step.steps[1]);
		assertEquals(2,  step.steps[2]);
		assertEquals(1,  step.steps[3]);
	}

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
	
	@Ignore
	@Test
	public void random236105478() {
		Slide slide = new Slide(3, 3, "236105478");
//		assertEquals("RR", slide.random());
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
		assertEquals("LULDRDLUURRDLLURRDLDR", slide.search());
	}
	
	@Test
	public void search32465871FAC0_9BE() {
		Slide slide = new Slide(4,4,"32465871FAC0=9BE", 120000);
		assertEquals("24,43,7251596,LULDDRRULURULLLDRDLURRULDRRDLLDRULLUURDRRDD", slide.search());
	}
	
	@Test
	public void search21597084ACBF__36() {
		Slide slide = new Slide(4, 4, "21597084ACBF==36", 120000);
		// ê[Ç≥óDêÊÇÃèÍçá
		assertEquals("29,46,9016015,LDRUURRDDLDRUULDLLURURDDDRUULDLUULDRDLURRRDLDR", slide.search());
	}
	
	@Test
	public void valid() {
		Slide slide = new Slide(3,3,"123456708");
		assertEquals(1, slide.valid(new char[]{'1','2','3','4','5','6','7','0','8'}));
		assertEquals(2, slide.valid(new char[]{'1','2','3','4','5','6','0','7','8'}));

		slide = new Slide(3,3,"12045=783");
		assertEquals(4, slide.valid(new char[]{'1','2','0','4','5','=','7','8','3'}));
	}
	
	@Test
	public void valid2() {
		Slide slide = new Slide(3,3,"123456708");
		assertEquals(1, slide.valid(new char[]{'.','.','.','.','1','2','3','.','.','4','5','6','.','.','7','0','8','.','.','.','.'}));
		assertEquals(2, slide.valid(new char[]{'.','.','.','.','1','2','3','.','.','4','5','6','.','.','0','7','8','.','.','.','.'}));
		
		slide = new Slide(3,3,"12045=783");
		assertEquals(4, slide.valid(new char[]{'.','.','.','.','1','2','0','.','.','4','5','=','.','.','7','8','3','.','.','.'}));
	}
	
	@Test
	public void valid840251_63() {
		Slide slide = new Slide(3,3,"840251=63");
		// ïùóDêÊÇÃèÍçá
		assertEquals(14, slide.valid(new char[]{'8','4','0','2','5','1','=','6','3'}));
	}
	
	@Test
	public void swap() {
		char[] sb = new char[]{1,2,3};
		Slide.swap(sb, 0, 2);
	}
	
	@Test
	public void check33() {
		Slide slide = new Slide(3, 3, "123456078");
		
		assertEquals("......123..456..780......",  slide.check(Slide.createStep(new int[]{4,4,1,1})).toString());
		assertEquals(null, slide.check(Slide.createStep(new int[]{4,4,1,3})));
		assertEquals(null, slide.check(Slide.createStep(new int[]{4,4,2})));
		assertEquals(null,  slide.check(Slide.createStep(new int[]{4,4,3})));
		assertEquals(null,  slide.check(Slide.createStep(new int[]{4,4,0,3})));
		assertEquals(null,  slide.check(Slide.createStep(new int[]{4,4,1,2})));
	}
}
