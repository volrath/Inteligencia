package neuralj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Implementation of object cloning
 */
public abstract class ObjectCloner
{
	/**
	 * Makes true copy of an object (with a different pointer)
	 * 
	 * @param o
	 *            A serializable object instance
	 * @return The object clone
	 */
	public static Object clone(Serializable o)
	{
		try
		{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream out;
			out = new ObjectOutputStream(b);
			out.writeObject(o);
			out.close();
			ByteArrayInputStream bi = new ByteArrayInputStream(b.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bi);
			Object no = in.readObject();
			return no;
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Input/Output exception whilst cloning an object.");
			e.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("ERROR: Unknown class found whilst cloning an object.");
			e.printStackTrace();
			return null;
		}
	}
}
