/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client.basic_types;

import org.C2C.client.TaskObject;

/**
 * Class C2CInteger
 * @author C2C Software, LLC
 */
public class C2CInteger extends TaskObject
{
    private C2CInteger()
    {
        m_newInstanceRequired = false;
        fireCompleted(true);
        setCompleted(true);
        setExecuted(true);
    }

    public C2CInteger(Integer i)
    {
        super(i.toString());
        m_newInstanceRequired = true;
        setCompleted(false);
    }

    /**
     * The default constructor for class CC2CString
     */
    public C2CInteger(TaskObject t)
    {
        this();
        m_parentTask = t;
    }
}
