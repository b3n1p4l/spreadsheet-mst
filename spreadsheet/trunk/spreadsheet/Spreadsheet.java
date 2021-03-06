package spreadsheet;

import ADTs.LinkedList;
import ADTs.Overflow;
import ADTs.QueueAr;
import tokens.CellToken;

/**
 * Spreadsheet class.
 * 
 * @author Teddy Doll.
 * @version 1.0.
 */
public class Spreadsheet
{
	// Instance fields

	/**
	 * 2-dimensional cell array.
	 */
	private Cell[][] cellData;

	/**
	 * Number of rows.
	 */
	private int rows;

	/**
	 * Number of columns.
	 */
	private int columns;

	// Constructors

	/**
	 * Construct a spreadsheet with rows and columns is equal to size.
	 * 
	 * @param size
	 *          The size.
	 */
	public Spreadsheet(int size)
	{
		this.rows = size;
		this.columns = size;
		cellData = new Cell[rows][columns];
		populateCells();
	}

	/**
	 * Construct a spreadsheet with rows and columns as the inputs.
	 * 
	 * @param rows
	 *          The number of rows.
	 * @param columns
	 *          The number of columns.
	 */
	public Spreadsheet(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		cellData = new Cell[rows][columns];
		populateCells();
	}

	// Instance methods

	/**
	 * Filling 2-dimensional cell array with empty cells.
	 */
	private void populateCells()
	{
		for (int row = 0; row < this.rows; row++)
		{
			for (int col = 0; col < this.columns; col++)
			{
				cellData[row][col] = new Cell();
			}
		}
	}

	/**
	 * Returns the cell at the row and column specified, of this spreadsheet.
	 * 
	 * @param row
	 *          The row of the cell.
	 * @param column
	 *          The column of the cell.
	 * @return A cell at row and column.
	 */
	public Cell getCell(int row, int column)
	{
		try
		{
			return cellData[row][column];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw e;
		}
	}

	/**
	 * Update a cell formula and recalculate Spreadsheet.
	 * 
	 * @param cellToken
	 *          The cell to update.
	 * @param inputFormula
	 *          The new formula for cell.
	 * @throws CycleFound
	 * @throws UnpairedParenthesesException
	 */
	public void changeCellFormulaAndRecalculate(CellToken cellToken,
			String inputFormula) throws CycleFound, UnpairedParenthesesException
	{
		cellData[cellToken.getRow()][cellToken.getColumn()].setFormula(
				inputFormula, this);

		topSort();

	}

	/**
	 * Revert cells formula and recalculate. Should only be used when a cycle is
	 * found.
	 * 
	 * @param cellToken
	 *          the cell to revert.
	 */
	public void revert(CellToken cellToken)
	{
		try
		{
			cellData[cellToken.getRow()][cellToken.getColumn()].revert(this);
		}
		catch (UnpairedParenthesesException e1)
		{

			System.err
					.println("Parentheses error in reverting (Should never Happen!");
		}
		try
		{
			topSort();
		}
		catch (CycleFound e)
		{
			System.err.println("Cycle found in reverting (Should never Happen!");
		}
	}

	/**
	 * Does a topological sort of all the cells and evaluates them.
	 * 
	 * @throws CycleFound
	 */
	private void topSort() throws CycleFound
	{
		// Some useful note for TCSS390C
		/*
		//Here's how you can get the indegree number of a cell.
		Cell aCell = new Cell();
		int numIndegree = aCell.indegree;
		
		//Here's how you evaluate a cell in this spreadsheet
		aCell.evaluate(this);
		
		//Here's how you can get a list of cells that depend on aCell
		LinkedList dependents = aCell.getDependents();
		
		//Here's how you can get an iterator that points to the front of the list (the first
		//real node)
		LinkedList.Iterator iterator = dependents.iterator();
		
		//Here's how you can get an iterator that points to the header node of a linked list
		LinkedList.Iterator zeroth = dependents.zeroth();*/
		
		int numVertices = this.rows * this.columns;
		QueueAr q = new QueueAr(numVertices);
		int counter = 0;
		Cell cellV, cellW;
		try
		{
			for (int row = 0; row < this.rows; row++)
			{
				for (int col = 0; col < this.columns; col++)
				{
					cellV = cellData[row][col];
					// reset temporary indegree to original indegree.
					cellV.indegreeTemp = cellV.indegree;
					if (cellV.indegreeTemp == 0)
					{
						q.enqueue(cellV);
					}
				}
			}

			while (!q.isEmpty())
			{
				cellV = (Cell) q.dequeue();
				cellV.evaluate(this);
				counter++;
				LinkedList.Iterator iter = cellV.dependents.iterator();
				while (iter.hasNext())
				{
					cellW = (Cell) iter.next();
					if (--cellW.indegreeTemp == 0)
					{
						q.enqueue(cellW);
					}
				}
			}
			if (counter != numVertices)
			{
				throw new CycleFound();
			}
		}
		catch (Overflow e)
		{
			System.out.println("Unexpected overflow");
		}
	}

	/**
	 * Returns the value of the requested cell.
	 * 
	 * @param cellToken
	 *          The cell for which value is requested.
	 * @return The cell value.
	 */
	public int getCellValue(CellToken cellToken)
	{
		return cellData[cellToken.getRow()][cellToken.getColumn()].getValue();
	}

	/**
	 * Prints all the cells of the spreadsheet to the console.
	 */
	public void printValues()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Spreadsheet values: \n");

		for (int row = 0; row < this.rows; row++)
		{
			for (int col = 0; col < this.columns; col++)
			{
				String nextValue = cellData[row][col].printValue();
				sb.append(nextValue);
				// for proper spacing
				for (int spaces = nextValue.length(); spaces < 10; spaces++)
					sb.append(" ");

			}
			sb.append("\n");
		}
		System.out.println(sb.toString());

	}

	/**
	 * Print all the formulas in this spreadsheet.
	 */
	public void printAllFormulas()
	{
		for (int row = 0; row < this.rows; row++)
		{
			for (int col = 0; col < this.columns; col++)
			{
				System.out.println(cellData[row][col].printFormula());
			}
		}
	}

	/**
	 * Print the cell formula.
	 * 
	 * @param cellToken
	 *          The cell token.
	 */
	public void printCellFormula(CellToken cellToken)
	{
		System.out.println(cellData[cellToken.getRow()][cellToken.getColumn()]
				.printFormula());
	}

	/**
	 * 
	 * @param cellToken
	 *          The cell token.
	 * @return The cell formula.
	 */
	public String cellFormulaToString(CellToken cellToken)
	{
		return cellData[cellToken.getRow()][cellToken.getColumn()].printFormula();
	}

	/**
	 * 
	 * @param cellToken
	 *          The cell token.
	 * @return The string representation of the cell's value.
	 */
	public String cellValueToString(CellToken cellToken)
	{
		return cellData[cellToken.getRow()][cellToken.getColumn()].printValue();
	}

	/**
	 * 
	 * @return The number of rows.
	 */
	public int getNumRows()
	{
		return this.rows;
	}

	/**
	 * 
	 * @return The number of columns.
	 */
	public int getNumColumns()
	{
		return this.columns;
	}

}
