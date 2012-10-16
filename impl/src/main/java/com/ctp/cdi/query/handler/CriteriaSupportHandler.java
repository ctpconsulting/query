package com.ctp.cdi.query.handler;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.enterprise.context.Dependent;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.Criteria;
import com.ctp.cdi.query.criteria.CriteriaSupport;
import com.ctp.cdi.query.criteria.QueryCriteria;
import com.ctp.cdi.query.criteria.QuerySelection;
import com.ctp.cdi.query.criteria.selection.AttributeQuerySelection;
import com.ctp.cdi.query.criteria.selection.numeric.Abs;
import com.ctp.cdi.query.criteria.selection.numeric.Avg;
import com.ctp.cdi.query.criteria.selection.numeric.Count;
import com.ctp.cdi.query.criteria.selection.numeric.Max;
import com.ctp.cdi.query.criteria.selection.numeric.Min;
import com.ctp.cdi.query.criteria.selection.numeric.Modulo;
import com.ctp.cdi.query.criteria.selection.numeric.Neg;
import com.ctp.cdi.query.criteria.selection.numeric.Sum;
import com.ctp.cdi.query.criteria.selection.strings.Lower;
import com.ctp.cdi.query.criteria.selection.strings.SubstringFrom;
import com.ctp.cdi.query.criteria.selection.strings.SubstringFromTo;
import com.ctp.cdi.query.criteria.selection.strings.Upper;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentDate;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentTime;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentTimestamp;

@Dependent
public class CriteriaSupportHandler<E> extends AbstractDelegateQueryHandler<E> implements CriteriaSupport<E> {

    @Override
    public Criteria<E, E> criteria() {
        return new QueryCriteria<E, E>(getEntityClass(), getEntityClass(), getEntityManager());
    }
    
    @Override
    public <T> Criteria<T, T> where(Class<T> clazz) {
        return new QueryCriteria<T, T>(clazz, clazz, getEntityManager());
    }
    
    @Override
    public <T> Criteria<T, T> where(Class<T> clazz, JoinType joinType) {
        return new QueryCriteria<T, T>(clazz, clazz, getEntityManager(), joinType);
    }
    
    @Override
    public <X> QuerySelection<E, X> attribute(SingularAttribute<E, X> attribute) {
        return new AttributeQuerySelection<E, X>(attribute);
    }
    
    // ----------------------------------------------------------------------------
    // NUMERIC QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public <N extends Number> QuerySelection<E, N> abs(SingularAttribute<E, N> attribute) {
        return new Abs<E, N>(attribute);
    }
    
    @Override
    public <N extends Number> QuerySelection<E, N> avg(SingularAttribute<E, N> attribute) {
        return new Avg<E, N>(attribute);
    }
    
    @Override
    public <N extends Number> QuerySelection<E, N> count(SingularAttribute<E, N> attribute) {
        return new Count<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> max(SingularAttribute<E, N> attribute) {
        return new Max<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> min(SingularAttribute<E, N> attribute) {
        return new Min<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> neg(SingularAttribute<E, N> attribute) {
        return new Neg<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> sum(SingularAttribute<E, N> attribute) {
        return new Sum<E, N>(attribute);
    }
    
    @Override
    public QuerySelection<E, Integer> modulo(SingularAttribute<E, Integer> attribute, Integer modulo) {
        return new Modulo<E>(attribute, modulo);
    }
    
    // ----------------------------------------------------------------------------
    // STRING QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public QuerySelection<E, String> upper(SingularAttribute<E, String> attribute) {
        return new Upper<E>(attribute);
    }

    @Override
    public QuerySelection<E, String> lower(SingularAttribute<E, String> attribute) {
        return new Lower<E>(attribute);
    }
    
    @Override
    public QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from) {
        return new SubstringFrom<E>(attribute, from);
    }

    @Override
    public QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from, int length) {
        return new SubstringFromTo<E>(attribute, from, length);
    }
    
    // ----------------------------------------------------------------------------
    // TEMPORAL QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public QuerySelection<E, Date> currDate() {
        return new CurrentDate<E>();
    }
    
    @Override
    public QuerySelection<E, Time> currTime() {
        return new CurrentTime<E>();
    }
    
    @Override
    public QuerySelection<E, Timestamp> currTStamp() {
        return new CurrentTimestamp<E>();
    }
}
