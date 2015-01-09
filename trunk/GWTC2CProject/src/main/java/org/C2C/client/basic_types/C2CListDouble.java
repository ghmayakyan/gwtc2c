/**
 * Copyright 2007-2008 C2C, Inc. All rights reserved.
 * C2C PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.client.basic_types;

import org.C2C.client.TaskObject;


/**
 * Class CC2CDoubleList.java
 * @author C2C Inc.
 */
public class C2CListDouble extends TaskObject
{
    /**
     * The default constructor for class CC2CDoubleList
     */
    public C2CListDouble()
    {
        m_newInstanceRequired = false;
        fireCompleted(false);
        setCompleted(true);
        setExecuted(true);
    }

    /**
     * The cast constructor for class CC2CString
     */
    public C2CListDouble(TaskObject t)
    {
        this();
        m_parentTask = t;
    }


}