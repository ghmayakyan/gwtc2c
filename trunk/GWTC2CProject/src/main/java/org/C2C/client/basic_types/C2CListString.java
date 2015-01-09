/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.client.basic_types;

import org.C2C.client.TaskObject;


/**
 * Class C2CListString
 * @author C2C Software, LLC
 */
public class C2CListString extends TaskObject
{
    public C2CListString()
    {
        m_newInstanceRequired = false;
        fireCompleted(false);
        setCompleted(true);
        setExecuted(true);
    }

    /**
     * The cast constructor for class CC2CString
     */
    public C2CListString(TaskObject t)
    {
        this();
        m_parentTask = t;
    }
}