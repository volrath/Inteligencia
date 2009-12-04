package neuralj;

import java.util.Vector;

/**
 * Represents a matrix of objects
 */
public class Matrix
{
	// Lists that represent the matrix
	private Vector<Vector<Object>>	lists;

	/**
	 * Initializes the matrix
	 * 
	 * @param x
	 *            The matrix's width
	 * @param y
	 *            The matrix's height
	 */
	public Matrix(int x, int y)
	{
		this.lists = new Vector<Vector<Object>>();
		for (int step = 0; step < x; step++)
			this.lists.add(new Vector<Object>(y));
		for (int step1 = 0; step1 < x; step1++)
			for (int step2 = 0; step2 < y; step2++)
				this.lists.get(step1).add(step2, null);
	}

	/**
	 * Checks if a specified position of the matrix exists
	 * 
	 * @param x
	 *            The matrix's x position
	 * @param y
	 *            The matrix's y position
	 * @return Boolean that specifies if the position exists
	 */
	private boolean exists(int x, int y)
	{
		if (x >= this.lists.size())
			return false;
		else if (y >= this.lists.get(0).size())
			return false;
		else
			return true;
	}

	/**
	 * Gets an object in from the specified position
	 * 
	 * @param x
	 *            The horizontal position in the matrix
	 * @param y
	 *            The vertical position in the matrix
	 * @return Returns an object from the specified position in the matrix
	 */
	public Object get(int x, int y)
	{
		if (exists(x, y))
			return this.lists.get(x).get(y);
		return null;
	}

	/**
	 * Returns the matrix's height
	 * 
	 * @return Returns the matrix's height
	 */
	public int height()
	{
		return this.lists.get(0).size();
	}

	/**
	 * Prints the matrix's contents to the console
	 */
	public void printMatrix()
	{
		for (int y = 0; y < this.lists.get(0).size(); y++)
		{
			for (int x = 0; x < this.lists.size(); x++)
			{
				System.out.print(this.lists.get(x).get(y) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Sets an object in the specified position
	 * 
	 * @param x
	 *            The horizontal position in the matrix
	 * @param y
	 *            The vertical position in the matrix
	 * @param object
	 *            The object that's going to be inserted in the matrix
	 */
	public void set(int x, int y, Object object)
	{
		if (exists(x, y))
			this.lists.get(x).set(y, object);
	}

	/**
	 * Returns the matrix's width
	 * 
	 * @return Returns the matrix's width
	 */
	public int width()
	{
		return this.lists.size();
	}
}
