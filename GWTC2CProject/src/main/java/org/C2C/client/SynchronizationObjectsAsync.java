/**
 * Copyright © 2008-2009 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Map;

/**
 * Interface SynchronizationObjectsAsync
 * @author C2C Software, LLC
 */
public interface SynchronizationObjectsAsync
{
    public void callService(List<String> cmds, AsyncCallback<Map<String, String>> asyncCallback);
}
