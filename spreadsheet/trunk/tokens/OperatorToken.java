package tokens;

/**
 * Operator token for the spreadsheet.
 * 
 * @author Son Pham
 * @version 1.0
 */
public class OperatorToken extends Token {
	/**
	 * Plus character.
	 */
	public static final char PLUS = '+';

	/**
	 * Minus character.
	 */
	public static final char MINUS = '-';

	/**
	 * Multiply character.
	 */
	public static final char MULT = '*';

	/**
	 * Divide character.
	 */
	public static final char DIV = '/';

	/**
	 * Exponent character.
	 */
	public static final char EXP = '^';
	/**
	 * Left parenthesis character.
	 */
	public static final char LEFT_PAREN = '(';

	/**
	 * Unary Minus character.
	 */
	public static final char U_MINUS = '~';
	/**
	 * The character of this operator token.
	 */
	private char my_operator_token;

	// Constructor

	/**
	 * Construct an operator token base on the input token.
	 * 
	 * @param token
	 *            The token.
	 */
	public OperatorToken(final char token) {
		if (isOperator(token)) {
			switch (token) {
			case PLUS:
				my_operator_token = PLUS;
				break;
			case MINUS:
				my_operator_token = MINUS;
				break;
			case MULT:
				my_operator_token = MULT;
				break;
			case DIV:
				my_operator_token = DIV;
				break;
			case EXP:
				my_operator_token = EXP;
				break;
			case LEFT_PAREN:
				my_operator_token = LEFT_PAREN;
				break;
			case U_MINUS:
				my_operator_token = U_MINUS;
				break;
			}
		}
	}

	/**
	 * Return true if the char ch is an operator of a formula. Current operators
	 * are: +, -, *, /, (.
	 * 
	 * @param ch
	 *            a char
	 * @return whether ch is an operator
	 */
	public static boolean isOperator(char ch) {
		return ((ch == PLUS) || (ch == MINUS) || (ch == MULT) || (ch == DIV)
				|| (ch == EXP) || (ch == LEFT_PAREN) || (ch == U_MINUS));
	}

	/**
	 * Return the priority of this OperatorToken.
	 * <p>
	 * priorities: <br>
	 * +, - : 0 <br>
	 * , / : 1 <br>
	 * ( : 2
	 * 
	 * @return the priority of operatorToken
	 */
	public int priority() {
		int priority = 0;
		switch (my_operator_token) {
		case PLUS:
			priority = 0;
			break;
		case MINUS:
			priority = 0;
			break;
		case MULT:
			priority = 1;
			break;
		case DIV:
			priority = 1;
			break;
		case EXP:
			priority = 2;
		case LEFT_PAREN:
			priority = 4;
			break;
		case U_MINUS:
			priority = 3;
			break;

		default:
			// This case should NEVER happen
			System.out.println("Error in priority.");
			System.exit(0);
			break;
		}
		return priority;
	}

	/**
	 * @return The character of this operator.
	 */
	public char getOperatorToken() {
		return my_operator_token;
	}
}
