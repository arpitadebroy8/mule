/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.deployer.api.descriptor;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public interface DescriptorParser
{
    ApplicationDescriptor parse(File descriptor, String applicationName) throws IOException;

    String getSupportedFormat();
}