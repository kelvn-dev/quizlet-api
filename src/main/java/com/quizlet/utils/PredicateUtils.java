package com.quizlet.utils;

import com.querydsl.core.types.dsl.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PredicateUtils {

  public static BooleanExpression getBooleanExpression(
      List<SearchCriteria> criteria, Class<?> classType) {
    BooleanExpression exp = Expressions.asBoolean(true).isTrue();
    for (SearchCriteria cr : criteria) {
      exp =
          exp.and(
              PredicateUtils.getPredicate(cr.getKey(), cr.getOperator(), cr.getValue(), classType));
    }
    return exp;
  }

  public static BooleanExpression getPredicate(
      String key, String operator, String value, Class<?> classType) {
    PathBuilder<?> entityPath =
        new PathBuilder<>(classType, HelperUtils.getEntityVariable(classType.getSimpleName()));
    Class<?> propertyType = HelperUtils.getPropertyType(classType, key);
    switch (propertyType.getSimpleName().toLowerCase()) {
      case "uuid":
        return getUUIDPredicate(key, operator, value, entityPath);
      case "boolean":
        return getBooleanPredicate(key, value, entityPath);
      case "string":
        return getStringPredicate(key, operator, value, entityPath);
      case "integer":
        return getIntegerPredicate(key, operator, value, entityPath);
      case "double":
        return getDoublePredicate(key, operator, value, entityPath);
      case "localdate":
        return getDatePredicate(key, operator, value, entityPath);
      case "localdatetime":
        return getDateTimePredicate(key, operator, value, entityPath);
      default:
        return null;
    }
  }

  public static BooleanExpression getStringPredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    StringPath path = entityPath.getString(key);
    switch (operator) {
      case "=":
        return path.equalsIgnoreCase(value);
      case "-":
        return path.containsIgnoreCase(value);
      case "%":
        return path.startsWithIgnoreCase(value);
      case ":":
        return path.in(Stream.of(value.split(";")).collect(Collectors.toList()));
      default:
        return null;
    }
  }

  public static BooleanExpression getUUIDPredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    ComparablePath<UUID> path = entityPath.getComparable(key, UUID.class);
    if (operator.equals("=")) {
      return path.eq(UUID.fromString(value));
    }
    return null;
  }

  public static BooleanExpression getBooleanPredicate(
      String key, String value, PathBuilder<?> entityPath) {
    BooleanPath path = entityPath.getBoolean(key);
    return path.stringValue().equalsIgnoreCase(value);
  }

  public static BooleanExpression getIntegerPredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    NumberPath<Integer> path = entityPath.getNumber(key, Integer.class);
    switch (operator) {
      case "=":
        return path.eq(Integer.parseInt(value));
      case "!=":
        return path.ne(Integer.parseInt(value));
      case ">":
        return path.gt(Integer.parseInt(value));
      case "<":
        return path.lt(Integer.parseInt(value));
      case ">=":
        return path.goe(Integer.parseInt(value));
      case "<=":
        return path.loe(Integer.parseInt(value));
      case ":":
        return path.in(Stream.of(value.split(";")).map(Integer::parseInt).toArray(Integer[]::new));
      case "()":
        String[] valueRange = HelperUtils.getValueRange(value);
        return path.between(
            valueRange[0].equals(" ") ? null : Integer.parseInt(valueRange[0]),
            valueRange[1].equals(" ") ? null : Integer.parseInt(valueRange[1]));
      default:
        return null;
    }
  }

  public static BooleanExpression getDoublePredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    NumberPath<Double> path = entityPath.getNumber(key, Double.class);
    switch (operator) {
      case "=":
        return path.eq(Double.parseDouble(value));
      case "!=":
        return path.ne(Double.parseDouble(value));
      case ">":
        return path.gt(Double.parseDouble(value));
      case "<":
        return path.lt(Double.parseDouble(value));
      case ">=":
        return path.goe(Double.parseDouble(value));
      case "<=":
        return path.loe(Double.parseDouble(value));
      case ":":
        return path.in(Stream.of(value.split(";")).map(Double::parseDouble).toArray(Double[]::new));
      case "()":
        String[] valueRange = HelperUtils.getValueRange(value);
        return path.between(
            valueRange[0].equals(" ") ? null : Double.parseDouble(valueRange[0]),
            valueRange[1].equals(" ") ? null : Double.parseDouble(valueRange[1]));
      default:
        return null;
    }
  }

  public static BooleanExpression getDatePredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    DatePath<LocalDate> path = entityPath.getDate(key, LocalDate.class);
    switch (operator) {
      case "=":
        return path.eq(LocalDate.parse(value));
      case ">":
        return path.gt(LocalDate.parse(value));
      case "<":
        return path.lt(LocalDate.parse(value));
      case ">=":
        return path.goe(LocalDate.parse(value));
      case "<=":
        return path.loe(LocalDate.parse(value));
      case "()":
        String[] valueRange = HelperUtils.getValueRange(value);
        return path.between(
            valueRange[0].equals(" ") ? null : LocalDate.parse(valueRange[0]),
            valueRange[1].equals(" ") ? null : LocalDate.parse(valueRange[1]));
      default:
        return null;
    }
  }

  public static BooleanExpression getDateTimePredicate(
      String key, String operator, String value, PathBuilder<?> entityPath) {
    DatePath<LocalDateTime> path = entityPath.getDate(key, LocalDateTime.class);
    switch (operator) {
      case "=":
        return path.eq(LocalDateTime.parse(value));
      case ">":
        return path.gt(LocalDateTime.parse(value));
      case "<":
        return path.lt(LocalDateTime.parse(value));
      case ">=":
        return path.goe(LocalDateTime.parse(value));
      case "<=":
        return path.loe(LocalDateTime.parse(value));
      case "()":
        String[] valueRange = HelperUtils.getValueRange(value);
        return path.between(
            valueRange[0].equals(" ") ? null : LocalDateTime.parse(valueRange[0]),
            valueRange[1].equals(" ") ? null : LocalDateTime.parse(valueRange[1]));
      default:
        return null;
    }
  }
}
