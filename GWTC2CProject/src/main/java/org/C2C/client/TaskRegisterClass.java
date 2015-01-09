/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

/**
 * Class RegisterClass
 * @author C2C Software, LLC
 */
public class TaskRegisterClass extends Task
{
    Object m_serverSideClasName = null;
    Class m_localClass = null;

    /**
     * The default constructor for class RegisterClass
     */
    public TaskRegisterClass(Object serverSideClasName, Class localClass)
    {
        super();
        m_serverSideClasName = serverSideClasName;
        m_localClass = localClass;
        setCompleted(false);
        TaskManager.ADD_TASK(this);
    }

    @Override
    public String run()
    {
        String ret = CallManager.REGISTER_CLASS(this, (String) m_serverSideClasName, m_localClass);
        setCompleted(true);
        return ret;
    }
}
