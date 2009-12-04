package neuralj;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles the conversion of objects to file and back to object
 */
public abstract class Serializer
{
	/**
	 * Loads a serialized object from a file
	 * 
	 * @param path
	 *            File where the serialized object is saved
	 * @return Returns the object's instance
	 */
	public static Object loadObject(String path)
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			return ois.readObject();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("ERROR: Problem loading object from file in " + path);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("ERROR: Problem loading object from file in " + path);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("ERROR: Problem loading object from file in " + path);
		}
		return null;
	}

	/**
	 * Saves a serialized version of the object
	 * 
	 * @param object
	 *            Object to be serialized and saved
	 * @param path
	 *            File where the serialized version of the object will be saved
	 */
	public static void saveObject(Object object, String path)
	{
		ObjectOutputStream oos;
		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(object);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("ERROR: Problem saving object to file in " + path);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Problem saving object to file in " + path);
			e.printStackTrace();
		}
	}
}
