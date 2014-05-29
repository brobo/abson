package tech.magnitude.abson;

public class JsonPrintSettings {
	public static final JsonPrintSettings DEFAULT = new JsonPrintSettings().setMultiline(false).setWhitespace(true).setRoundFractions(false);
	public static final JsonPrintSettings PRETTY_DEFAULT = new JsonPrintSettings().setMultiline(true).setIndent(3);
	
	private int startIndent;
	private int indent;
	private boolean multiline;
	
	private boolean whitespace;
	
	private boolean roundFractions;
	private int roundAmount;
	
	public JsonPrintSettings() {
		indent = 3;
		startIndent = 0;
		multiline = roundFractions = false;
		whitespace = true;
	}
	
	public JsonPrintSettings(boolean multiline, int indent, boolean whitespace, boolean roundFractions, int roundAmount) {
		this.multiline = multiline;
		this.indent = indent;
		this.whitespace = whitespace;
		this.roundFractions = roundFractions;
		this.roundAmount = roundAmount;
	}
	
	public JsonPrintSettings(JsonPrintSettings old, int newStartIndent) {
		startIndent = newStartIndent;
		
		indent = old.indent;
		multiline = old.multiline;
		
		whitespace = old.whitespace;
		
		roundFractions = old.roundFractions;
		roundAmount = old.roundAmount;
	}
	
	public int getStartIndent() {
		return startIndent;
	}
	
	public JsonPrintSettings setStartIndent(int startIndent) {
		this.startIndent = startIndent;
		return this;
	}

	public int getIndent() {
		return indent;
	}

	public JsonPrintSettings setIndent(int indent) {
		this.indent = indent;
		return this;
	}

	public boolean isMultiline() {
		return multiline;
	}

	public JsonPrintSettings setMultiline(boolean multiline) {
		this.multiline = multiline;
		return this;
	}

	public boolean hasWhitespace() {
		return whitespace;
	}

	public JsonPrintSettings setWhitespace(boolean whitespace) {
		this.whitespace = whitespace;
		return this;
	}

	public boolean shouldRoundFractions() {
		return roundFractions;
	}

	public JsonPrintSettings setRoundFractions(boolean roundFractions) {
		this.roundFractions = roundFractions;
		return this;
	}

	public int getRoundAmount() {
		return roundAmount;
	}

	public JsonPrintSettings setRoundAmount(int roundAmount) {
		this.roundAmount = roundAmount;
		return this;
	}
}
