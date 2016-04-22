/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.processor;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.runtime.core.api.transaction.Transaction;
import org.mule.tck.testmodels.mule.TestTransaction;
import org.mule.runtime.core.transaction.TransactionCoordination;

import org.junit.Ignore;
import org.junit.Test;

public class LaxSedaStageInterceptingMessageProcessorTestCase extends
    SedaStageInterceptingMessageProcessorTestCase
{

    @Test
    public void testProcessRequestResponse() throws Exception
    {
        MuleEvent event = getTestEvent(TEST_MESSAGE);

        assertSync(messageProcessor, event);
    }

    @Test
    public void testProcessOneWayWithTx() throws Exception
    {
        MuleEvent event = getTestEvent(TEST_MESSAGE);
        Transaction transaction = new TestTransaction(muleContext);
        TransactionCoordination.getInstance().bindTransaction(transaction);

        try
        {
            assertSync(messageProcessor, event);
        }
        finally
        {
            TransactionCoordination.getInstance().unbindTransaction(transaction);
        }
    }

    @Test
    public void testProcessRequestResponseWithTx() throws Exception
    {
        MuleEvent event = getTestEvent(TEST_MESSAGE);
        Transaction transaction = new TestTransaction(muleContext);
        TransactionCoordination.getInstance().bindTransaction(transaction);

        try
        {
            assertSync(messageProcessor, event);
        }
        finally
        {
            TransactionCoordination.getInstance().unbindTransaction(transaction);
        }
    }

    @Override
    protected LaxSedaStageInterceptingMessageProcessor createAsyncInterceptingMessageProcessor(MessageProcessor listener)
        throws Exception
    {
        LaxSedaStageInterceptingMessageProcessor mp = new LaxSedaStageInterceptingMessageProcessor("name",
            "name", queueProfile, queueTimeout, muleContext.getDefaultThreadingProfile(), queueStatistics,
            muleContext);
        mp.setListener(listener);
        return mp;
    }

}