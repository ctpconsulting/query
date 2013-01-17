package com.ctp.cdi.query.spi;



/**
 * A marker interface. Used for writing custom query methods:
 *
 * public interface DaoExtension<E> {
 *     E saveAndFlushAndRefresh(E entity);
 * }
 *
 * public class DelegateDaoExtension<E> implements DaoExtension<E>, DelegateQueryHandler {
 *    @Inject
 *    private QueryInvocationContext context;
 *
 *    @Override
 *    public E saveAndFlushAndRefresh(E entity) {
 *        ...
 *    }
 * }
 *
 * The extension is now usable with:
 *
 * @Dao
 * public interface MySimpleDao extends DaoExtension<Simple>, EntityDao<Simple, Long> {
 * }
 *
 */
public interface DelegateQueryHandler {
}
