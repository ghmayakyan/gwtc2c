/**
 * Copyright 2007-2008 C2C, Inc. All rights reserved.
 * C2C PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.client;

/**
 * Class TaskNull.java
 * @author C2C Inc.
 */
public class TaskNull extends TaskObject
{
    private static TaskNull NULL = null;
    /**
     * The default constructor for class TaskNull
     */
    private TaskNull()
    {
        m_newInstanceRequired = false;
    }

    public static TaskNull GET_NULL() {
        if(null == NULL) {
            NULL = new TaskNull();
        }
        return NULL;
    }
    @Override
    public String getId() {
        return CallManager.OBJECT_TYPE_NULL;
    }

}