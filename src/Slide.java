import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Slide {

	private static final char B = '.';
	
	public static final int U = 0;
	public static final int R = 1;
	public static final int L = 2;
	public static final int D = 3;
	
	public char[] C = new char[]{'U','R','L','D'};
	public int[] F;
	public int[] M;
	public int[][] DD;
	
	private final char[] field;
	private final char[] answer;
	
	private final int width;
	private final int height;
	private final char[] map;

	private final int timeout;

	private final Integer[] EP;
	private final int[][] RANDOM = new int[][] {
			{},			// 0000
			{U},		// 0001
			{R},		// 0010
			{U, R},		// 0011
			{L},		// 0100
			{L, U}, 	// 0101
			{L, R},	 	// 0110
			{L, R, U},	// 0111
			{D},		// 1000
			{D, U},		// 1001
			{D, R},		// 1010
			{D, U, R},	// 1011
			{D, L},		// 1100
			{D, L, U}, 	// 1101
			{D, L, R},	// 1110
			{D, L, R, U}// 1111
	};

	public Slide(int width, int height, String map) {
		this(width, height, map, 0);
	}

	public Slide(int width, int height, String map, int timeout) {
		this.width = width;
		this.height = height;
		this.timeout = timeout;
		this.map = new char[map.length()];
		for(int i = 0; i < map.length(); i++){
			this.map[i] = map.charAt(i);
		}
		
		this.field = initField(width, height, map);
		this.answer = initAnswer(width, height, map);
		
		// 移動距離(Field用)
		F = new int[] { (width + 2) * -1, 1, -1, (width + 2) };
		// 移動距離(map用)
		M = new int[] { width * -1, 1, -1, width };
		
		DD = new int[][]{
				{U, R, L},
				{U, R, D},
				{U, L, D},
				{R, L, D},
		};
		
		// イコールの位置
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < map.length(); i++) {
			if (map.charAt(i) == '=') {
				list.add(i);
			}
		}
		EP = list.toArray(new Integer[list.size()]);
		
	}

	char[] initAnswer(int width, int height, String map) {
		char[] answer = new char[(width + 2) * (height + 2)];
		Arrays.fill(answer, B);
		
		// BODY
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int index = i * width + j + 1;
				char c;
				if (index == width * height) {
					c = '0';
				} else if (map.charAt(i*width + j) == '=') {
					c = '=';
				} else if (index > 9) {
					c = (char)('A' + (index - 10));
				} else {
					c = (char) (index + '0');
				}
				answer[(width + 2) * (i + 1) + (j + 1)] = c;
			}
		}

		return answer;
	}

	char[] initField(int width, int height, String map) {
		char[] field = new char[(width + 2) * (height + 2)];
		Arrays.fill(field, B);
		
		// BODY
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				field[(width + 2) * (i + 1) + (j + 1)] = map.charAt(i*width + j);
			}
		}

		return field;
	}
	

//	public String random() {
//		Random random = new Random(map.hashCode());
//		
//		ArrayList<StringBuffer> list = new ArrayList<StringBuffer>();
//
//		int length = valid(map) / 2;
//		System.out.println(length);
//		while(true){
//			StringBuffer sb = new StringBuffer("44");
//			int currentIndex = startField(field);
//			int temp = -1;
//			for(int i = 0; i < length; i++){
//				int n = 0;
//				for (int j = 0; j < 4; j++) {
//					if (temp == j) {
//						continue;
//					}
//					char c = field.charAt(currentIndex + F[j]);
//					if (c != B) {
//						n += (1 << j);
//					}
//				}
//				int[] r = RANDOM[n];
//				int m = r[random.nextInt(r.length)];
//				temp = 3-m;
//				currentIndex += F[m];
//				sb.append(m);
//			}
//			
//			StringBuffer check = check(sb);
//			
//			System.out.println(sb.toString() + "," + valid(check.toString().replace(".", "")));
//			
//			list.add(sb);
//			if (list.size() > 10){
//				break;
//			}
//		}
//		
//		for (StringBuffer sb : list) {
//			System.out.println(sb.toString());
//		}
//		
//		return "";
//	}
	
	public String search() {
		Stack stack = new Stack();
		stack.push(new Step());

		int valid = valid(map);
		int limit = valid + 2 + startIndex() % 2;
		int backupLimit = limit;
		int count = 0;

		long start = System.currentTimeMillis();
		while (true) {
			if (count % 1000 == 0) {
				long end = System.currentTimeMillis();
				if (timeout != 0 && start + timeout < end) {
					return backupLimit + "," + stack.pop().index + ","
							+ count + ",";
				}
			}
			Step node = stack.pop();
			if (node == null) {
				node = new Step();
				limit += 2;
			}
			for (int i = 0; i < 4; i++) {
				Step step = node.copyInstance();
				step.add(i);
				
				char[] movedFiled = check(step);
				if (movedFiled == null) {
					continue;
				}
				
				// 正解？
				if (Arrays.equals(answer, movedFiled)) {
					String format = format(step);
					return backupLimit + "," + format.length() + "," + count
							+ "," + format;
//					return format(step);
				}
				
				// 現在の深さ＋下限値(LowerBound)＞今回の深さ制限
				int valid2 = valid(movedFiled);
				if (step.index + valid2 < limit) {
					stack.push(step);
					count++;
				}
			}
//			System.out.println("---------------" + stack.size());
		}
	}

	String format(Step step) {
		StringBuffer sb = new StringBuffer();
		for (int i = 2; i < step.index; i++) {
			sb.append(C[step.steps[i]]);
		}
		return sb.toString();
	}

	char[] check(Step step) {
		// 戻ったらfalse
		int a = step.steps[step.index - 1];
		int b = step.steps[step.index - 2];
		if (a + b == 3) {
			return null;
		}
		
		char[] tempField = Arrays.copyOf(field, field.length);
		
		int currentField = startField(getField());
		int temp = currentField;
		for (int i = 2; i < step.index; i++) {
			currentField += F[step.steps[i]];

			if (field[currentField] == B) {
				return null;
			}
			if (field[currentField] == '=') {
				return null;
			}
			swap(tempField, temp, currentField);
			temp = currentField;
		}

		return tempField;
	}

	static void swap(char[] tempField, int a, int b) {
		char temp = tempField[a];
		tempField[a] = tempField[b];
		tempField[b] = temp;
	}

	private int startIndex() {
		return startField(map);
	}

	private int startField(char[] es) {
		for(int i = 0; i < es.length; i++){
			if (es[i] == '0'){
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String[] ss = args[0].split(",");
		if (ss.length < 3) {
			return;
		}
		Slide slide;
		if (args.length == 2) {
			slide = new Slide(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
					ss[2], Integer.parseInt(args[1]));

		} else {
			slide = new Slide(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
					ss[2]);

		}
		try {
			System.out.println(slide.search());
		} catch (Exception e) {
			System.out.println("x");
		}
	}

	public char[] getField() {
		return field;
	}

	public char[] getAnswer() {
		return answer;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public char[] getMap() {
		return map;
	}

	public static Step createStep(int[] init) {
		return new Step(init);
	}
	
	public static class Step {
		int[] steps;
		int index = -1;
		
		public Step() {
			steps = new int[256];
			Arrays.fill(steps, -1);
			steps[0] = 4;
			steps[1] = 4;
			index  = 2;
		}
		
		Step(int steps[], int index){
			this.steps = steps;
			this.index = index;
		}
		
		public Step(int[] init) {
			steps = new int[256];
			Arrays.fill(steps, -1);
			
			System.arraycopy(init, 0, steps, 0, init.length);
			
			index = init.length;
		}
		
		public void add(int i) {
			steps[index] = i;
			index++;
		}
		
//		public int getIndex() {
//			return index;
//		}
//		public void setIndex(int index) {
//			this.index = index;
//		}
		
		public Step copyInstance() {
			int[] s = new int[steps.length];
			System.arraycopy(steps, 0, s, 0, steps.length);
			return new Step(s, index);
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for(int i = 0; steps[i] != -1;i++){
				sb.append(steps[i]);
			}
			return sb.toString();
		}
	}
	
	class Stack {
		ArrayList<Step> stack = new ArrayList<Step>();
		
		public Step pop() {
			if (stack.size() == 0) {
				return null;
			}
			return stack.remove(stack.size() - 1);
		}

		public void push(Step sb) {
//			System.out.println(sb);
			stack.add(sb);
		}

		public int size() {
			return stack.size();
		}
	}

	public int valid(char[] movedFiled) {
		int limit = 0;
		int b = 0;
		for (int i = 0; i < movedFiled.length; i++) {
			char c = movedFiled[i];
			if (c == B) {
				b++;
				continue;
			}
			int i2 = i - b;
			
			int index = -1;
			if (c == '0') {
				continue;
			}
			if (c == '=') {
				continue;
			}
			if ('A' <= c) {
				index += c - 'A' + 10;
			} else {
				index += c - '0';
			}
			int w = Math.abs(i2 % width - index % width);
			int h = Math.abs(i2 / height - index / height);
			limit += w + h;
			
			// イコールの判定
			for (int j = 0; j < EP.length; j++) {
				if (i2 % width == EP[j] % width
						&& index % width == EP[j] % width) {
					if (Math.min(i2, index) < EP[j] && EP[j] < Math.max(i2, index)) {
						limit += 2;
					}
				} else if (i2 / width == EP[j] % width
						&& index / width == EP[j] % width) {
					if (Math.min(i2, index) < EP[j] && EP[j] < Math.max(i2, index)) {
						limit += 2;
					}
				}
			}
		}
		return limit;
	}
}
