/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client.basic_types;

import org.C2C.client.TaskObject;

/**
 * Class C2CDouble
 * @author C2C Software, LLC
 */
public class C2CDouble extends TaskObject
{
    private C2CDouble()
    {
        m_newInstanceRequired = false;
        fireCompleted(true);
        setCompleted(true);
    }

    public C2CDouble(Double d)
    {
        super(d.toString());
        m_newInstanceRequired = true;
        setCompleted(false);
    }

    /**
     * The default constructor for class CC2CString
     */
    public C2CDouble(TaskObject t)
    {
        this();
        m_parentTask = t;
    }
}
