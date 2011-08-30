import java.awt.Window;
import java.util.ArrayList;


public class Slide {

	private static final char B = '.';
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;
	
	public char[] C = new char[]{'U','R','L','D'};
	public int[] F;
	public int[] M;
	
	private final String field;
	private final String answer;
	
	private final int width;
	private final int height;
	private final String map;

	public Slide(int width, int height, String map) {
		this.width = width;
		this.height = height;
		this.map = map;
		this.field = initField(width, height, map);
		this.answer = initAnswer(width, height, map);
		
		// ˆÚ“®‹——£(Field—p)
		F = new int[] { (width + 2) * -1, 1, -1, (width + 2) };
		// ˆÚ“®‹——£(map—p)
		M = new int[] { width * -1, 1, -1, width };
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
	

	public String search() {
		Stack stack = new Stack();
		stack.push(new StringBuffer("44"));

		int limit = valid(width, height, map) + 2 + startIndex() % 2;
		int count = 0;

		while (true) {
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
				
				// ³‰ðH
				if (answer.equals(movedFiled.toString())) {
//					return count + "," + format(step);
					return format(step);
				}
				
				// Œ»Ý‚Ì[‚³{‰ºŒÀ’l(LowerBound)„¡‰ñ‚Ì[‚³§ŒÀ
				if (step.length() + valid(width, height, movedFiled.toString()) < limit) {
					stack.push(step);
					count++;
				}
			}
			System.out.println("---------------" + stack.size());
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
		// –ß‚Á‚½‚çfalse
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
			System.out.println(sb.toString());
			stack.add(sb);
		}

		public int size() {
			return stack.size();
		}
	}

	public static int valid(int width, int height, String map) {
		int limit = 0;
		for (int i = 0; i < map.length(); i++) {
			char c = map.charAt(i);
			int index = -1;
			if (c == '0') {
				continue;
			}
			if ('A' <= c) {
				index += c - 'A' + 10;
			} else {
				index += c - '0';
			}
			int w = Math.abs(i % width - index % width);
			int h = Math.abs(i / height - index / height);
			limit += w + h;
		}
		return limit;
	}
}
