package tech.magnitude.abson;

public class JsonTokenizer {
	String text;
	
	public JsonTokenizer(String text) {
		this.text = text;
	}
	
	private void normalize() {
		text = text.replaceAll("\n", "");
	}
	
	private void disposeWhitespace() {
		//System.out.print(text);
		while (hasNext() && Character.isWhitespace(text.charAt(0))) {
			text = text.substring(1);
		}
		//System.out.println(" -> " + text);
	}
	
	public char peek() {
		disposeWhitespace();
		return text.charAt(0);
	}
	
	public char pop() {
		return popWithoutDisposing();
	}
	
	public char popWithoutDisposing() {
		char res = text.charAt(0);
		text = text.substring(1);
		return res;
	}
	
	public String nextWord() {
		disposeWhitespace();
		String res = "";
		while (isValidIdentifier(peek())) {
			res += pop();
		}
		return res;
	}
	
	public String findMatching() {
		disposeWhitespace();
		char matcher = peek();
		char matching;
		if (matcher == '{') matching = '}';
		else if (matcher == '[') matching = ']';
		else return "";
		int level = 0;
		String res = "";
		do {
			char c = pop();
			if (c == matcher) level++;
			else if (c == matching) level --;
			res += c;
		} while (level > 0);
		return res;
	}
	
	public String getStringLiteral() {
		disposeWhitespace();
		if (peek() != '"') return "";
		String res = "" + pop();
		char c;
		do {
			res += (c = pop());
			if (c == '\\') {
				res += pop();
			}
		} while (c != '"');
		return res;
	}
	
	public String getNextToken() {
		disposeWhitespace();
		switch (peek()) {
		case '"':
			return getStringLiteral();
		case '[':
		case '{':
			return findMatching();
		default:
			return nextWord();
		}
	}
	
	public boolean hasNext() {
		return text.length() > 0;
	}
	
	private boolean isValidIdentifier(char c) {
		return Character.isLetterOrDigit(c) || c == '_' || c == '$';
	}
}
