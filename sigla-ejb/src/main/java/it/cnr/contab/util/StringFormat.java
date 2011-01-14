package it.cnr.contab.util;

/**
 * Insert the type's description here.
 * Creation date: (13/11/2002 16.24.26)
 * @author: CNRADM
 */
public class StringFormat extends java.text.Format {
	private char fillingChar;
	private int length;
	private int direction;
	public final static int RIGHT = 0;
	public final static int LEFT = 1;
/**
 * CapFormat constructor comment.
 *
 * @param character		carattere di riempimento
 * @param length		lunghezza totale della stringa da completare
 * @param direction		direzione di completamento (LEFT -> completo a sx, RIGHT -> compelto a dx)
 */
public StringFormat(char fillingChar, int length) {
	super();
	this.fillingChar = fillingChar;
	this.length = length;
	this.direction = LEFT;
	
	
}
/**
 * CapFormat constructor comment.
 *
 * @param character		carattere di riempimento
 * @param length		lunghezza totale della stringa da completare
 * @param direction		direzione di completamento (LEFT -> completo a sx, RIGHT -> compelto a dx)
 */
public StringFormat(char fillingChar, int length, int direction) {
	super();
	this.fillingChar = fillingChar;
	this.length = length;
	this.direction = direction;
	
	
}
/**
 * format method comment.
 */
public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {

	if (obj == null) return toAppendTo;
	toAppendTo.append(obj.toString());

	return toAppendTo;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.39.33)
 * @return int
 */
public int getDirection() {
	return direction;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.38.07)
 * @return char
 */
public char getFillingChar() {
	return fillingChar;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.39.14)
 * @return int
 */
public int getLength() {
	return length;
}
/**
 * parseObject method comment.
 */
public Object parseObject(String source, java.text.ParsePosition status) {

	if (source==null) return null;
	source = source.trim();
	if (source.length() == 0) return null;

	status.setIndex(source == null ? 1 : source.length());

	StringBuffer toAppendTo = new StringBuffer();
	if (direction == RIGHT)
		toAppendTo.append(source);
		
	for (int i = source.length();i < length;i++)
		toAppendTo.append(fillingChar);

	if (direction != RIGHT)
		toAppendTo.append(source);

	return toAppendTo;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.39.33)
 * @param newDirection int
 */
public void setDirection(int newDirection) {
	direction = newDirection;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.38.07)
 * @param newFillingChar char
 */
public void setFillingChar(char newFillingChar) {
	fillingChar = newFillingChar;
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 16.39.14)
 * @param newLength int
 */
public void setLength(int newLength) {
	length = newLength;
}
}
