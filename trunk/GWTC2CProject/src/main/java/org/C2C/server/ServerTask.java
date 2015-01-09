/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.server;

/**
 * Class ServerTask
 * @author C2C Software, LLC
 */
public class ServerTask
{
    private Object m_result = null;

    public ServerTask()
    {
    }

    public Object getResult()
    {
        return m_result;
    }

    public void setResult(Object result)
    {
        m_result = result;
    }
}