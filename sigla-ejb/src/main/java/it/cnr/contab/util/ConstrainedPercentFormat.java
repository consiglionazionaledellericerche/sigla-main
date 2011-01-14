package it.cnr.contab.util;

/**
 * Insert the type's description here.
 * Creation date: (10/10/2002 16:42:35)
 * @author: CNRADM
 */
public class ConstrainedPercentFormat extends PositivePercentFormat {
	private final static java.math.BigDecimal MAX = new java.math.BigDecimal(100);
/**
 * ConstrainedPercentFormat constructor comment.
 */
public ConstrainedPercentFormat() {
	super();
}
public Object parseObject(String source) throws java.text.ParseException {
	java.math.BigDecimal percent = (java.math.BigDecimal)super.parseObject(source);
	if (percent == null) return null;
	if (percent.compareTo(MAX) > 0)
		throw new it.cnr.jada.bulk.ValidationParseException("Sono ammesse solo percentuali minori di 100",0);
	return percent;
}
}
