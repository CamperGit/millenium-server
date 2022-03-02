package com.millenium.milleniumserver.specifiaction;

import com.millenium.milleniumserver.util.TypeUtils;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contains common jpa specification implementations
 */
public final class Specifications {

    private interface FromSpecification {

        Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

        /**
         * Returns responsible column name from columns chain
         * <p>
         * Example:
         * - input parameters: myEntry.name
         * - returns: name
         *
         * @param column - column chain, separated by '.'
         * @return
         */
        default String column(String column) {
            List<String> joins = Arrays.asList(column.split("\\."));
            return joins.get(joins.size() - 1);
        }

        /**
         * Returns responsible join for specification
         *
         * @param root
         * @param column - responsible column name
         * @return
         */
        default From<?, ?> from(Root root, String column) {
            List<String> joins = Arrays.asList(column.split("\\."));

            if (joins.size() <= 1) {
                return root;
            }

            Join _root = root.join(joins.get(0), JoinType.LEFT);
            for (String value : joins.subList(1, joins.size() - 1)) {
                _root = _root.join(value, JoinType.LEFT);
            }
            return _root;
        }

    }

    private Specifications() {
        throw new IllegalStateException(
                "Forbidden to create an instance of ru.ds.rsb.escm.content.dao.specification.Specifications class"
        );
    }

    /**
     * Common 'equal' specification implementation
     */
    @Builder
    public static class Equal<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            Expression exp = from.get(column(column));

            return criteriaBuilder.equal(
                    exp.getJavaType().isEnum() ? exp.as(String.class) : exp,
                    exp.getJavaType().isEnum() ? value.toString() : value
            );
        }
    }

    /**
     * Common 'is null' specification implementation
     */
    @Builder
    public static class IsNull<T> implements Specification<T>, FromSpecification {

        private String column;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return criteriaBuilder.isNull(from.get(column(column)));
        }
    }

    /**
     * Common 'is bot null' specification implementation
     */
    @Builder
    public static class IsNotNull<T> implements Specification<T>, FromSpecification {

        private String column;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return criteriaBuilder.isNotNull(from.get(column(column)));
        }
    }

    /**
     * Common 'is true' specification implementation
     */
    @Builder
    public static class IsTrue<T> implements Specification<T>, FromSpecification {

        private String column;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return criteriaBuilder.isTrue(from.get(column(column)));
        }
    }

    /**
     * Common 'is false' specification implementation
     */
    @Builder
    public static class IsFalse<T> implements Specification<T>, FromSpecification {

        private String column;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return criteriaBuilder.isFalse(from.get(column(column)));
        }
    }

    /**
     * Common 'like' specification implementation
     */
    @Builder
    public static class Like<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            Expression<String> likeExpression = from.get(column(column));
            return criteriaBuilder.like(
                    criteriaBuilder.upper(likeExpression),
                    "%" + ((String) value).toUpperCase() + "%"
            );
        }
    }

    /**
     * Common 'like' specification implementation
     */
    @Builder
    public static class LikeIn<T> implements Specification<T>, FromSpecification {

        private String column;
        private List<String> value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            Expression<String> likeExpression = from.get(column(column));
            return criteriaBuilder.or(value.stream()
                    .map((str) -> criteriaBuilder.like(
                            criteriaBuilder.upper(likeExpression),
                            "%" + str.toUpperCase() + "%"
                    )).toArray(Predicate[]::new));
        }
    }

    @Builder
    public static class And<T> implements Specification<T> {

        private List<Specification<T>> specifications;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            criteriaQuery.distinct(true);

            List<Specification<T>> specs = specifications
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Predicate[] predicates = new Predicate[specs.size()];

            specs.stream()
                    .map(s -> s.toPredicate(root, criteriaQuery, criteriaBuilder))
                    .collect(Collectors.toList()).toArray(predicates);

            return criteriaBuilder.and(predicates);
        }
    }

    @Builder
    public static class Or<T> implements Specification<T> {

        private List<Specification<T>> specifications;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            criteriaQuery.distinct(true);

            List<Specification<T>> specs = specifications
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Predicate[] predicates = new Predicate[specs.size()];

            specs.stream()
                    .map(s -> s.toPredicate(root, criteriaQuery, criteriaBuilder))
                    .collect(Collectors.toList()).toArray(predicates);

            return criteriaBuilder.or(predicates);
        }
    }

    /**
     * Common 'in' specification implementation
     */
    @Builder
    public static class In<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return from.get(column(column)).in(value);
        }
    }

    /**
     * Common 'inList' specification implementation
     */
    @Builder
    public static class InList<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            return criteriaBuilder.isMember(value, from.get(column(column)));
        }
    }

    /**
     * Common 'greater than or equal to' specification implementation
     */
    @Builder
    public static class GreaterThanOrEqualTo<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            String _column = column(column);
            if (from.get(_column).getJavaType() == LocalDate.class) {
                Expression<LocalDate> ltExpression = from.get(_column);
                return criteriaBuilder.greaterThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, LocalDate.class));
            } else if (from.get(_column).getJavaType() == LocalDateTime.class) {
                Expression<LocalDateTime> ltExpression = from.get(_column);
                return criteriaBuilder.greaterThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, LocalDateTime.class));
            } else if (from.get(_column).getJavaType() == Long.class) {
                Expression<Long> ltExpression = from.get(_column);
                return criteriaBuilder.greaterThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, Long.class));
            } else if (from.get(_column).getJavaType() == Integer.class) {
                Expression<Integer> ltExpression = from.get(_column);
                return criteriaBuilder.greaterThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, Integer.class));
            } else if (from.get(_column).getJavaType() == BigDecimal.class) {
                Expression<BigDecimal> ltExpression = from.get(_column);
                return criteriaBuilder.greaterThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, BigDecimal.class));
            }
            throw new RuntimeException("Couldn't determinate greater then property type");
        }
    }

    /**
     * Common 'less than or equal to' specification implementation
     */
    @Builder
    public static class LessThanOrEqualTo<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object value;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            String _column = column(column);
            if (from.get(_column).getJavaType() == LocalDate.class) {
                Expression<LocalDate> ltExpression = from.get(_column);
                return criteriaBuilder.lessThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, LocalDate.class));
            } else if (from.get(_column).getJavaType() == LocalDateTime.class) {
                Expression<LocalDateTime> ltExpression = from.get(_column);
                return criteriaBuilder.lessThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, LocalDateTime.class));
            } else if (from.get(_column).getJavaType() == Long.class) {
                Expression<Long> ltExpression = from.get(_column);
                return criteriaBuilder.lessThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, Long.class));
            } else if (from.get(_column).getJavaType() == Integer.class) {
                Expression<Integer> ltExpression = from.get(_column);
                return criteriaBuilder.lessThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, Integer.class));
            } else if (from.get(_column).getJavaType() == BigDecimal.class) {
                Expression<BigDecimal> ltExpression = from.get(_column);
                return criteriaBuilder.lessThanOrEqualTo(ltExpression, TypeUtils.valueOf(value, BigDecimal.class));
            }
            throw new RuntimeException("Couldn't determinate less then property type");
        }
    }

    /**
     * Common 'between' specification implementation
     */
    @Builder
    public static class Between<T> implements Specification<T>, FromSpecification {

        private String column;
        private Object valueFrom;
        private Object valueTo;

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return toFromPredicate(from(root, column), query, criteriaBuilder);
        }

        @Override
        public Predicate toFromPredicate(From<?, ?> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            query.distinct(true);
            String _column = column(column);
            if (from.get(_column).getJavaType() == LocalDate.class) {
                Expression<LocalDate> ltExpression = from.get(_column);
                return criteriaBuilder.between(
                        ltExpression,
                        TypeUtils.valueOf(valueFrom, LocalDate.class),
                        TypeUtils.valueOf(valueTo, LocalDate.class)
                );
            } else if (from.get(_column).getJavaType() == LocalDateTime.class) {
                Expression<LocalDateTime> ltExpression = from.get(_column);
                return criteriaBuilder.between(
                        ltExpression,
                        TypeUtils.valueOf(valueFrom, LocalDateTime.class),
                        TypeUtils.valueOf(valueTo, LocalDateTime.class)
                );
            } else if (from.get(_column).getJavaType() == Long.class) {
                Expression<Long> ltExpression = from.get(_column);
                return criteriaBuilder.between(
                        ltExpression,
                        TypeUtils.valueOf(valueFrom, Long.class),
                        TypeUtils.valueOf(valueTo, Long.class)
                );
            } else if (from.get(_column).getJavaType() == Integer.class) {
                Expression<Integer> ltExpression = from.get(_column);
                return criteriaBuilder.between(
                        ltExpression,
                        TypeUtils.valueOf(valueFrom, Integer.class),
                        TypeUtils.valueOf(valueTo, Integer.class)
                );
            } else if (from.get(_column).getJavaType() == BigDecimal.class) {
                Expression<BigDecimal> ltExpression = from.get(_column);
                return criteriaBuilder.between(
                        ltExpression,
                        TypeUtils.valueOf(valueFrom, BigDecimal.class),
                        TypeUtils.valueOf(valueTo, BigDecimal.class)
                );
            }
            throw new RuntimeException("Couldn't determinate less then property type");
        }
    }

    /**
     * Возвращает спецификацию "LessThanOrEqual" или null, если не заполнено значение
     *
     * @param column - наименование поля для сравнения
     * @param value  - значение поля
     * @param type
     * @return
     */
    public static <T> Specification<T> lessThanEqualToOrNull(String column, Object value, Class<T> type) {
        if (value == null || (value instanceof String && StringUtils.isEmpty(value))) {
            return null;
        }

        return LessThanOrEqualTo.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    /**
     * Возвращает спецификацию "GreaterThanOrEqualTo" или null, если не заполнено значение
     *
     * @param column - наименование поля для сравнения
     * @param value  - значение поля
     * @param type
     * @return
     */
    public static <T> Specification<T> greaterThanEqualToOrNull(String column, Object value, Class<T> type) {
        if (value == null || (value instanceof String && StringUtils.isEmpty(value))) {
            return null;
        }

        return GreaterThanOrEqualTo.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    /**
     * Возвращает спецификацию "LIKE" или null, если не заполнено значение
     *
     * @param column - наименование поля для сравнения
     * @param value  - значение поля
     * @param type
     * @return
     */
    public static <T> Specification<T> likeOrReturnNull(String column, Object value, Class<T> type) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Specifications.Like.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    /**
     * Возвращает спецификацию "EQUAL" или null, если не заполнено значение
     *
     * @param column - наименование поля для сравнения
     * @param value  - значение поля
     * @param type
     * @return
     */
    public static <T> Specification<T> equalOrReturnNull(String column, Object value, Class<T> type) {
        if (value == null || (value instanceof String && StringUtils.isEmpty(value))) {
            return null;
        }

        return Specifications.Equal.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    /**
     * Возвращает спецификацию "true/false" или null, если не заполнено значение
     *
     * @param column - наименование поля для сравнения
     * @param value  - значение
     * @return
     */
    public static <T> Specification<T> trueFalseOrReturnNull(String column, Boolean value, Class<T> type) {
        if (value == null) {
            return null;
        }

        return value ? Specifications.IsTrue.<T>builder().column(column).build()
                : Specifications.IsFalse.<T>builder().column(column).build();

    }

    public static <T> Specification<T> inOrReturnNull(String column, Object value, Class<T> type) {
        if (value == null) {
            return null;
        }

        return Specifications.In.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    public static <T> Specification<T> likeInOrReturnNull(String column, List<String> value, Class<T> type) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        return Specifications.LikeIn.<T>builder()
                .column(column)
                .value(value)
                .build();
    }

    public static <T> Specification<T> trueFalseIsNullOrReturnNull(String column, Boolean value, Class<T> type) {
        if (value == null) {
            return null;
        }

        return value ? Specifications.IsNull.<T>builder()
                .column(column)
                .build() : Specifications.IsNotNull.<T>builder()
                .column(column)
                .build();
    }
}
