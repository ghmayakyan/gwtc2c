/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import org.C2C.client.basic_types.C2CBoolean;
import org.C2C.client.basic_types.C2CDouble;
import org.C2C.client.basic_types.C2CInteger;
import org.C2C.client.basic_types.C2CString;


/**
 * Class ClassRegistry
 * @author C2C Software, LLC
 */
public class ClassRegistry
{

    public static void INIT_CLASSES()
    {

        //Basic Type registration
        new TaskRegisterClass(String.class.getName(), C2CString.class);
        new TaskRegisterClass(Integer.class.getName(), C2CInteger.class);
        new TaskRegisterClass(Boolean.class.getName(), C2CBoolean.class);
        new TaskRegisterClass(Double.class.getName(), C2CDouble.class);

        
    }
}
