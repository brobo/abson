package tech.magnitude.abson;

public class JsonPrintSettings {
	/**
	 * Represents the default JsonPrintSettings used, in which whitespace is enabled but multiline mode and all other flags are disabled.
	 */
	public static final JsonPrintSettings DEFAULT = new JsonPrintSettings().setMultiline(false).setWhitespace(true).setRoundDecimals(false);
	
	/**
	 * Represents a "pretty" default which may be used, which enables whitespace and multiline using an indent of 3 spaces between levels.
	 */
	public static final JsonPrintSettings PRETTY_DEFAULT = new JsonPrintSettings().setMultiline(true).setIndent(3);
	
	private int startIndent;
	private int indent;
	private boolean multiline;
	
	private boolean whitespace;
	
	private boolean roundFractions;
	private int roundAmount;
	
	/**
	 * Constructs a new JSON print settings object which is functionally identical to JsonPrintSettings.DEFAULT;
	 * whitespace is the only enabled flag.
	 */
	public JsonPrintSettings() {
		indent = 3;
		startIndent = 0;
		multiline = roundFractions = false;
		whitespace = true;
	}
	
	/**
	 * Constructs a new JSON print settings object with the specified parameters.
	 * @param multiline Whether or not the JSON should be printed across multiple lines.
	 * @param indent The amount to indent (when using multiline mode) between levels.
	 * @param whitespace Whether or not the JSON should contain whitespace.
	 * @param roundFractions Whether or not fractions should be rounded, to guard against round-off error.
	 * @param roundAmount The amount of decimals to which fractions should be rounded.
	 */
	public JsonPrintSettings(boolean multiline, int indent, boolean whitespace, boolean roundFractions, int roundAmount) {
		this.multiline = multiline;
		this.indent = indent;
		this.whitespace = whitespace;
		this.roundFractions = roundFractions;
		this.roundAmount = roundAmount;
	}
	
	/**
	 * Constructs a new JsonPrintSettings which is a shallow copy of the specified JsonPrintSettings.
	 * @param old The old JsonPrintSettings from which to copy flags.
	 */
	public JsonPrintSettings(JsonPrintSettings old) {
		startIndent = old.getStartIndent();
		
		indent = old.getIndent();
		multiline = old.isMultiline();
		
		whitespace = old.hasWhitespace();
		
		roundFractions = old.shouldRoundDecimals();
		roundAmount = old.getRoundAmount();
	}
	
	/**
	 * Used in multiline formatting; returns a new JsonPrintSettins which has had its start indent
	 * shifted to the right by <code>getIndent()</code> spaces.
	 * @return
	 */
	public JsonPrintSettings getNextLevel() {
		return new JsonPrintSettings(this).setStartIndent(getStartIndent() + getIndent());
	}
	
	/**
	 * Obtains the starting indent.
	 * @return The starting indent.
	 */
	public int getStartIndent() {
		return startIndent;
	}
	
	/**
	 * Sets the starting indent.
	 * @param startIndent The new starting indent.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setStartIndent(int startIndent) {
		this.startIndent = startIndent;
		return this;
	}

	/**
	 * Obtains the indent between levels.
	 * @return The indent between levels.
	 */
	public int getIndent() {
		return indent;
	}

	/**
	 * Sets the indent between levels.
	 * @param indent The new indent between levels.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setIndent(int indent) {
		this.indent = indent;
		return this;
	}
	
	/**
	 * Obtains whether or not the JSON should be printed on multiple lines.
	 * @return Whether or not the JSON should be printed on multiple lines.
	 */
	public boolean isMultiline() {
		return multiline;
	}

	/**
	 * Sets whether or not the JSON should be printed on multiple lines.
	 * @param multiline Whether or not the JSON should be printed on multiple lines.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setMultiline(boolean multiline) {
		this.multiline = multiline;
		return this;
	}

	/**
	 * Obtains whether or not the JSON should contain whitespace.
	 * @return Whether or not the JSON contains whitespace.
	 */
	public boolean hasWhitespace() {
		return whitespace;
	}

	/**
	 * Sets whether or not the JSON should contain whitespace.
	 * @param whitespace Whether or not the JSON should contain whitespace.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setWhitespace(boolean whitespace) {
		this.whitespace = whitespace;
		return this;
	}

	/**
	 * Obtains whether or not decimals should be rounded.
	 * @return Whether or not decimals should be rounded.
	 */
	public boolean shouldRoundDecimals() {
		return roundFractions;
	}

	/**
	 * Set whether or not decimals should be rounded.
	 * @param roundDecimals Whether or not decimals should be rounded.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setRoundDecimals(boolean roundDecimals) {
		this.roundFractions = roundDecimals;
		return this;
	}

	/**
	 * Obtains the number of decimal places to round to, if decimal rounding is enabled.
	 * @return The number of decimal places to round to, if decimal rounding is enabled.
	 */
	public int getRoundAmount() {
		return roundAmount;
	}

	/**
	 * Sets the number of decimal places to round to, if decimal rounding is enabled.
	 * @param roundAmount The number of decimal places to round to.
	 * @return A reference to this object.
	 */
	public JsonPrintSettings setRoundAmount(int roundAmount) {
		this.roundAmount = roundAmount;
		return this;
	}
}
