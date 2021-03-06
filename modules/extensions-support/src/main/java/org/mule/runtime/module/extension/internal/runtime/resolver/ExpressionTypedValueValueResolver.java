/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.internal.runtime.resolver;

import static org.mule.runtime.core.util.ClassUtils.isInstance;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.Event;
import org.mule.runtime.core.api.TransformationService;

import javax.inject.Inject;

/**
 * A {@link ValueResolver} implementation and extension of {@link TypeSafeExpressionValueResolver } which evaluates expressions
 * and tries to ensure that the output is always of a certain type.
 * <p>
 * This {@link ValueResolver} will return the {@link TypedValue} of the MEL evaluation result.
 *
 * @param <T>
 * @since 4.0
 */
public class ExpressionTypedValueValueResolver<T> extends ExpressionValueResolver<TypedValue<T>> implements Initialisable {

  private final Class<T> expectedClass;
  private TypeSafeTransformer typeSafeTransformer;

  @Inject
  private TransformationService transformationService;

  public ExpressionTypedValueValueResolver(String expression, Class<T> expectedClass) {
    super(expression);
    this.expectedClass = expectedClass;
  }

  @Override
  public TypedValue<T> resolve(Event event) throws MuleException {
    initEvaluator();

    TypedValue typedValue = evaluator.resolveTypedValue(event, Event.builder(event));
    if (isInstance(expectedClass, typedValue.getValue())) {
      return typedValue;
    } else {
      DataType expectedDataType =
          DataType.builder().type(expectedClass).mediaType(typedValue.getDataType().getMediaType()).build();
      return new TypedValue<>((T) typeSafeTransformer.transform(typedValue.getValue(), typedValue.getDataType(), expectedDataType,
                                                                event),
                              expectedDataType);
    }
  }

  public void setTransformationService(TransformationService transformationService) {
    this.transformationService = transformationService;
  }

  @Override
  public void initialise() throws InitialisationException {
    this.typeSafeTransformer = new TypeSafeTransformer(transformationService);
  }
}
