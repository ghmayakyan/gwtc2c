/**
 * Copyright © 2008-2009 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.io.File;

/**
 * Class Environment
 * @author C2C Software, LLC
 */
public class Environment
{
    /**
     * Private default construcor to prevent instantiation of objects of this class.
     */
    private Environment()
    {}
    
    /**
     * Retrieves the list of all environment variables.
     * @return The list of all environment variables.
     */
    public static List<String> allVariables()
    {
        List<String> l = new ArrayList<String>();
        Map env = System.getenv();
        Set keys = env.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()){
          l.add((String) iterator.next());
        }
        return l;
    }

    /**
     * Gets the value of specified environment variable.
     * @param var The environment variable name.
     * @return The value of specified environment variable. If the variable is
     * not deficed then null is returned.
     */
    public static String valueOf(String var) {
        return System.getenv(var);
    }
    
    /**
     * Gets the name of the operating system.
     * @return The osName name in lowercase.
     */
    public static String osName()
    {
        String p = System.getProperty("os.name");
        return (null != p ? p.toLowerCase() : "unknown");
    }
    
    /**
     * Gets the file delimiter symbol on this osName.
     * @return The file delimiter symbol, For Windows osName will return '\' and for
     * unix/linux will return '/'.
     */
    public static final String fileSeparator()
    {
        return File.separator;
    }

    /**
     * Gets the path delimiter symbol on this osName.
     * @return The file delimiter symbol, For Windows osName will return ';' and for
     * unix/linux will return ':'.
     */
    public static final String pathSeparator()
    {
        return File.pathSeparator;
    }
    
    /**
     * Gets the home directory.
     * @return The home directory of the currently logged user.
     */
    public static String userHomeDir()
    {
        String p = System.getProperty("user.home");
        assert(null != p);
        return p;
    }

    /**
     * Gets the working directory.
     * @return The current working directory of the user.
     */
    public static String userWorkDir()
    {
        String p = System.getProperty("user.dir");
        assert(null != p);
        return p;
    }
    
    /**
     * Gets the temp directory.
     * @return The home directory of the currently logged user.
     */
    public static String tmpDir()
    {
        String p = System.getProperty("java.io.tmpdir");
        assert(null != p);
        String fs = fileSeparator();
        if(p.endsWith(fs)){
            p = p.substring(0, p.length()-1);
        }
        return p;
    }
    
}
