/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client.basic_types;

import org.C2C.client.TaskObject;


/**
 * Class C2CString
 * @author C2C Software, LLC
 */
public class C2CString extends TaskObject
{
    public C2CString()
    {
        m_newInstanceRequired = false;
        fireCompleted(true);
        setCompleted(true);
        setExecuted(true);
    }

    public C2CString(String s)
    {
        super(s);
        m_newInstanceRequired = true;
        setCompleted(false);
    }

    /**
     * The default constructor for class CC2CString
     */
    public C2CString(TaskObject t)
    {
        this();
        m_parentTask = t;
    }
}
