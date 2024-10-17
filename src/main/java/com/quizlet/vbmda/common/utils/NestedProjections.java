/*
 * Copyright (c) 2020 by VinBrain LLC. All Rights Reserved.
 */

package net.vinbrain.vbmda.common.utils;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.core.types.Path;
import net.vinbrain.vbmda.common.exception.VbmdaBaseRuntimeException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nguyen Hoang Nam
 */
public class NestedProjections<T> extends MappingProjection<T> {

    private static final String DOT_PROPERTY = ".";
    private static final String UNDERLINE_PROPERTY = "_";

    private final List<Path<?>> other = new ArrayList<>();
    private final List<Path<?>> root = new ArrayList<>();

    protected NestedProjections(final Class<T> clazz, final Path<?>... args) {
        super(clazz, args);
        for (final Path<?> path : args) {
            if (path.getRoot().getType().equals(clazz)) {
                root.add(path);
            } else {
                other.add(path);
            }
        }
    }

    private NestedProjections(final Class<T> clazz, final String rootAlias, final Path<?>... args) {
        super(clazz, args);
        for (final Path<?> path : args) {
            if (path.getRoot().toString().equals(rootAlias)) {
                root.add(path);
            } else {
                other.add(path);
            }
        }
    }

    public static <T> NestedProjections<T> of(final Class<T> clazz, final Path<?>... args) {
        return new NestedProjections<>(clazz, args);
    }

    public static <T> NestedProjections<T> of(final Class<T> clazz, final String rootAlias, final Path<?>... args) {
        return new NestedProjections<>(clazz, rootAlias, args);
    }

    @Override
    protected T map(final Tuple row) {
        try {
            final T result = getType().newInstance();
            String propertyPath;
            for (final Path<?> arg : root) {
                if (doCustomMapping(result, arg, row)) {
                    continue;
                }

                final String string = arg.toString();
                if (string.contains(UNDERLINE_PROPERTY)) {
                    propertyPath = StringUtils.join(ArrayUtils.remove(StringUtils.split(string, UNDERLINE_PROPERTY), 0), DOT_PROPERTY);
                } else {
                    propertyPath = StringUtils.join(ArrayUtils.remove(StringUtils.split(string, DOT_PROPERTY), 0), DOT_PROPERTY);
                }
                PropertyUtils.setNestedProperty(result, propertyPath, row.get(arg));
            }

            for (final Path<?> arg : other) {
                final String string = arg.toString();
                if (string.contains(UNDERLINE_PROPERTY)) {
                    propertyPath = StringUtils.join(StringUtils.split(string, UNDERLINE_PROPERTY), DOT_PROPERTY);
                } else {
                    propertyPath = StringUtils.join(StringUtils.split(string, DOT_PROPERTY), DOT_PROPERTY);
                }
                instantiateNestedProperties(result, propertyPath);
                PropertyUtils.setNestedProperty(result, propertyPath, row.get(arg));
            }
            return result;
        } catch (final Exception exception) {
            throw new VbmdaBaseRuntimeException("Unexpected error while initializing nested projections.", exception);
        }
    }

    @SuppressWarnings("unused")
    protected boolean doCustomMapping(final T result, final Path<?> arg, final Tuple row) {
        return false;
    }

    private void instantiateNestedProperties(final Object obj, final String fieldName) throws Exception {
        final String[] fieldNames = StringUtils.split(fieldName, DOT_PROPERTY);
        if (fieldNames.length <= 1) {
            return;
        }

        final StringBuilder nestedProperty = new StringBuilder();
        for (int i = 0; i < fieldNames.length - 1; i++) {
            final String fn = fieldNames[i];
            if (i != 0) {
                nestedProperty.append(".");
            }
            nestedProperty.append(fn);
            final Object value;

            value = PropertyUtils.getProperty(obj, nestedProperty.toString());

            if (value == null) {
                final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(
                        obj,
                        nestedProperty.toString());
                final Class<?> propertyType = propertyDescriptor.getPropertyType();
                final Object newInstance = propertyType.newInstance();
                PropertyUtils.setProperty(obj, nestedProperty.toString(), newInstance);
            }
        }
    }

}
