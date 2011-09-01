import java.util.ArrayList;
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
	
	private final String field;
	private final String answer;
	
	private final int width;
	private final int height;
	private final String map;

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
		this.map = map;
		this.timeout = timeout;
		
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

	String initAnswer(int width, int height, String map) {
		StringBuffer sb = new StringBuffer();
		// HEAD
		for (int i = 0; i < (width + 2); i++)
			sb.append(B);
		// BODY
		for (int i = 0; i < height; i++) {
			sb.append(B);
			for (int j = 0; j < width; j++) {
				int index = i * width + j + 1;
				if (index == width * height) {
					sb.append(0);
				} else if (map.charAt(i*width + j) == '=') {
					sb.append('=');
				} else if (index > 9) {
					sb.append((char)('A' + (index - 10)));
				} else {
					sb.append(index);
				}
			}
			sb.append(B);
		}
		// FOOD
		for (int i = 0; i < (width + 2); i++)
			sb.append(B);

		return sb.toString();
	}

	String initField(int width, int height, String map) {
		StringBuffer sb = new StringBuffer();
		// HEAD
		for (int i = 0; i < (width + 2); i++)
			sb.append(B);
		// BODY
		for (int i = 0; i < height; i++) {
			sb.append(B);
			for (int j = 0; j < width; j++) {
				sb.append(map.charAt(i*width + j));
			}
			sb.append(B);
		}
		// FOOD
		for (int i = 0; i < (width + 2); i++)
			sb.append(B);

		return sb.toString();
	}
	

	public String random() {
		Random random = new Random(map.hashCode());
		
		ArrayList<StringBuffer> list = new ArrayList<StringBuffer>();

		int length = valid(map) / 2;
		System.out.println(length);
		while(true){
			StringBuffer sb = new StringBuffer("44");
			int currentIndex = startField(field);
			int temp = -1;
			for(int i = 0; i < length; i++){
				int n = 0;
				for (int j = 0; j < 4; j++) {
					if (temp == j) {
						continue;
					}
					char c = field.charAt(currentIndex + F[j]);
					if (c != B) {
						n += (1 << j);
					}
				}
				int[] r = RANDOM[n];
				int m = r[random.nextInt(r.length)];
				temp = 3-m;
				currentIndex += F[m];
				sb.append(m);
			}
			
			StringBuffer check = check(sb);
			
			System.out.println(sb.toString() + "," + valid(check.toString().replace(".", "")));
			
			list.add(sb);
			if (list.size() > 10){
				break;
			}
		}
		
		for (StringBuffer sb : list) {
			System.out.println(sb.toString());
		}
		
		return "";
	}
	
	public String search() {
		Stack stack = new Stack();
		stack.push(new StringBuffer("44"));

		int limit = valid(map) + 2 + startIndex() % 2;
		int backupLimit = limit;
		int count = 0;

		long start = System.currentTimeMillis();
		while (true) {
			if (count % 1000 == 0) {
				long end = System.currentTimeMillis();
				if (timeout != 0 && start + timeout < end) {
					return backupLimit + "," + stack.pop().length() + ","
							+ count + ",";
				}
			}
			StringBuffer node = stack.pop();
			if (node == null) {
				node = new StringBuffer("44");
				limit += 2;
			}
			for (int i = 0; i < 4; i++) {
				StringBuffer step = new StringBuffer(node);
				step.append(i);
				
				StringBuffer movedFiled = check(step);
				if (movedFiled == null) {
					continue;
				}
				
				// 正解？
				if (answer.equals(movedFiled.toString())) {
					String format = format(step);
					return backupLimit + "," + format.length() + "," + count
							+ "," + format;
//					return format(step);
				}
				
				// 現在の深さ＋下限値(LowerBound)＞今回の深さ制限
				if (step.length() + valid(movedFiled.toString()) < limit) {
					stack.push(step);
					count++;
				}
			}
//			System.out.println("---------------" + stack.size());
		}
	}

	String format(StringBuffer step) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < step.length(); i++) {
			int c = step.charAt(i) - '0';
			if (c == 4) {
				continue;
			}
			sb.append(C[c]);
		}
		return sb.toString();
	}

	StringBuffer check(StringBuffer step) {
		// 戻ったらfalse
		int a = step.charAt(step.length() - 1) - '0';
		int b = step.charAt(step.length() - 2) - '0';
		if (a + b == 3) {
			return null;
		}
		
		StringBuffer tempStep = new StringBuffer(step);
		tempStep.delete(0, 2);
		
		StringBuffer tempField = new StringBuffer(field);
		
		int currentField = startField(getField());
		int temp = currentField;
		for (int i = 0; i < tempStep.length(); i++) {
			currentField += F[tempStep.charAt(i) - '0'];

			if (field.charAt(currentField) == B) {
				return null;
			}
			if (field.charAt(currentField) == '=') {
				return null;
			}
			swap(tempField, temp, currentField);
			temp = currentField;
		}

		return tempField;
	}

	static void swap(StringBuffer tempField, int a, int b) {
		char temp = tempField.charAt(a);
		tempField.setCharAt(a, tempField.charAt(b));
		tempField.setCharAt(b, temp);
	}

	private int startIndex() {
		return map.indexOf('0');
	}

	private int startField(String field) {
		return field.indexOf('0');
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

	public String getField() {
		return field;
	}

	public String getAnswer() {
		return answer;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getMap() {
		return map;
	}

	class Stack {
		ArrayList<StringBuffer> stack = new ArrayList<StringBuffer>();

		public StringBuffer pop() {
			if (stack.size() == 0) {
				return null;
			}
			return stack.remove(stack.size() - 1);
		}

		public void push(StringBuffer sb) {
//			System.out.println(sb.toString());
			stack.add(sb);
		}

		public int size() {
			return stack.size();
		}
	}

	public int valid(String map) {
		int limit = 0;
		int b = 0;
		for (int i = 0; i < map.length(); i++) {
			char c = map.charAt(i);
			if (c == B) {
				b++;
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
