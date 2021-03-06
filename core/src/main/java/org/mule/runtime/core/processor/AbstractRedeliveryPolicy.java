/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.processor;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.construct.FlowConstruct;
import org.mule.runtime.core.api.construct.FlowConstructAware;
import org.mule.runtime.core.api.context.MuleContextAware;
import org.mule.runtime.core.api.exception.MessagingExceptionHandler;
import org.mule.runtime.core.api.exception.MessagingExceptionHandlerAware;
import org.mule.runtime.core.api.processor.Processor;
import org.mule.runtime.core.config.i18n.CoreMessages;

/**
 * Implement a redelivery policy for Mule. This is similar to JMS retry policies that will redeliver a message a maximum number of
 * times. If this maximum is exceeded, the message is sent to a dead letter queue, Here, if the processing of the messages fails
 * too often, the message is sent to the failedMessageProcessor MP, whence success is force to be returned, to allow the message
 * to be considered "consumed".
 */
public abstract class AbstractRedeliveryPolicy extends AbstractInterceptingMessageProcessor
    implements Processor, Lifecycle, MuleContextAware, FlowConstructAware, MessagingExceptionHandlerAware {

  protected FlowConstruct flowConstruct;
  protected int maxRedeliveryCount;
  public static final int REDELIVERY_FAIL_ON_FIRST = 0;
  private MessagingExceptionHandler messagingExceptionHandler;

  @Override
  public void setFlowConstruct(FlowConstruct flowConstruct) {
    this.flowConstruct = flowConstruct;
  }

  @Override
  public void initialise() throws InitialisationException {
    if (maxRedeliveryCount < 0) {
      throw new InitialisationException(CoreMessages.initialisationFailure("maxRedeliveryCount must be positive"), this);
    }
  }

  @Override
  public void start() throws MuleException {}

  @Override
  public void stop() throws MuleException {}

  @Override
  public void dispose() {}

  public int getMaxRedeliveryCount() {
    return maxRedeliveryCount;
  }

  public void setMaxRedeliveryCount(int maxRedeliveryCount) {
    this.maxRedeliveryCount = maxRedeliveryCount;
  }

  public void setMessagingExceptionHandler(MessagingExceptionHandler messagingExceptionHandler) {
    this.messagingExceptionHandler = messagingExceptionHandler;
  }

  @Override
  public void setMuleContext(MuleContext context) {
    super.setMuleContext(context);
  }
}
