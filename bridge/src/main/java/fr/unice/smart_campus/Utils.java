
package fr.unice.smart_campus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class who contains utilities methods.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class Utils
{

/**
 * Delete a file.
 * 
 * @param file         File to delete.
 * @throws IOException IO error.
 */
public static void deleteFile(File file)
throws IOException
{
   // Check if file is a directory or a file.
   if (file.isDirectory())
   {
      // Delete the files in the directory.
      String[] files = file.list();
      if (files != null)
      {
         for (String temp : files)
         {
            File fileDelete = new File(file, temp);
            deleteFile(fileDelete);
         }
      }
   }

   // Delete the file.
   file.delete();
}

    /**
     * Load the configuration file in a string.
     *
     * @param f File to load.
     *
     * @return The file in a string.
     *
     * @throws java.io.FileNotFoundException
     */
    public static String loadConfigFile(File f)
            throws FileNotFoundException
    {
        StringBuilder sb = new StringBuilder();

        // Build the return string from the file.
        Scanner scan = new Scanner(f);
        while (scan.hasNext())
        {
            String line = scan.nextLine().trim();
            if (line.startsWith("//"))
                continue;
            sb.append(line).append("\n");
        }
        scan.close();

        return sb.toString();
    }

/**
 * Compare two objects possibly null.
 * 
 * @param obj1 Object to compare with the second object.
 * @param obj2 Object to compare with the first object.
 * 
 * @return true if the two objects are equals, false if not.
 */
public static boolean equals(Object obj1, Object obj2)
{      
   return (obj1 == null ? obj2 == null : obj1.equals(obj2));
}
}
