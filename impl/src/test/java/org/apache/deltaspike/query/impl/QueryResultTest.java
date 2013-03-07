package org.apache.deltaspike.query.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.api.QueryResult;
import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.domain.SimpleBuilder;
import org.apache.deltaspike.query.test.domain.Simple_;
import org.apache.deltaspike.query.test.service.SimpleDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;

public class QueryResultTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                .addClasses(SimpleDao.class)
                .addPackage(Simple.class.getPackage());
    }

    @Inject
    private SimpleDao dao;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    private SimpleBuilder builder;

    @Test
    public void should_sort_result() {
        // given
        final String name = "testSortResult";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(123));

        // when
        List<Simple> result = dao.findByName(name)
                .orderDesc(Simple_.counter)
                .orderAsc(Simple_.id)
                .getResultList();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        int lastCounter = Integer.MAX_VALUE;
        long lastId = Long.MIN_VALUE;
        for (Simple simple : result) {
            int currentCounter = simple.getCounter().intValue();
            long currentId = simple.getId().longValue();
            if (currentCounter == lastCounter) {
                assertTrue(currentId > lastId);
            } else {
                assertTrue(currentCounter < lastCounter);
            }
            lastCounter = currentCounter;
            lastId = currentId;
        }
    }

    @Test
    public void should_change_sort_order() {
        // given
        final String name = "testChangeSortOrder";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(229));

        // when
        QueryResult<Simple> query = dao.findByName(name);
        List<Simple> result1 = query
                .changeOrder(Simple_.counter)
                .getResultList();
        List<Simple> result2 = query
                .changeOrder(Simple_.counter)
                .getResultList();

        // then
        assertEquals(22, result1.get(0).getCounter().intValue());
        assertEquals(229, result2.get(0).getCounter().intValue());
    }

    @Test
    public void should_clear_sort_order() {
        // given
        final String name = "testClearSortOrder";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(229));

        // when
        QueryResult<Simple> query = dao.findByName(name);
        List<Simple> result1 = query
                .changeOrder(Simple_.counter)
                .getResultList();
        List<Simple> result2 = query
                .clearOrder()
                .getResultList();

        // then
        assertEquals(result1.size(), result2.size());
        for (int i = 0; i < result1.size(); i++) {
            int count1 = result1.get(i).getCounter().intValue();
            int count2 = result2.get(i).getCounter().intValue();
            if (count1 != count2) {
                return;
            }
        }
        fail("Both collections sorted: " + result1 + "," + result2);
    }

    @Test
    public void should_page_result() {
        // given
        final String name = "testPageResult";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(123));

        // when
        List<Simple> result = dao.findByName(name)
                .hint("javax.persistence.query.timeout", 10000)
                .lockMode(LockModeType.NONE)
                .flushMode(FlushModeType.COMMIT)
                .orderDesc(Simple_.counter)
                .firstResult(2)
                .maxResults(2)
                .getResultList();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void should_page_with_page_api() {
        // given
        final String name = "testPageAPI";
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(123));
        builder.createSimple(name, Integer.valueOf(229));
        builder.createSimple(name, Integer.valueOf(299));
        builder.createSimple(name, Integer.valueOf(389));

        // when
        QueryResult<Simple> pagedQuery = dao
                .findByName(name)
                .withPageSize(2);
        List<Simple> result1 = pagedQuery.getResultList();
        List<Simple> result2 = pagedQuery.nextPage().nextPage().getResultList();
        int current = pagedQuery.currentPage();
        List<Simple> result3 = pagedQuery.toPage(1).getResultList();
        int total = pagedQuery.countPages();
        int pageSize = pagedQuery.pageSize();

        // then
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
        assertEquals(2, result3.size());
        assertEquals(2, current);
        assertEquals(4, total);
        assertEquals(2, pageSize);

        assertEquals(22, result1.get(0).getCounter().intValue());
        assertEquals(229, result2.get(0).getCounter().intValue());
        assertEquals(99, result3.get(0).getCounter().intValue());

    }

    @Test
    public void should_modify_named_query() {
        // given
        final String name = "testModifyNamedQuery";
        builder.createSimple(name + 0);
        builder.createSimple(name + 1);
        builder.createSimple(name + 2);
        builder.createSimple(name + 3);

        // when
        List<Simple> result = dao.queryResultWithNamed(name + "%")
                 .orderDesc(Simple_.name)
                 .getResultList();

        // then
        assertEquals(4, result.size());
        assertEquals(name + 3, result.get(0).getName());
        assertEquals(name + 2, result.get(1).getName());
    }

    @Test
    public void should_count_with_method_query() {
        // given
        final String name = "testCountWithMethodQuery";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        long result = dao.findByName(name).count();

        // then
        assertEquals(2L, result);
    }

    @Test
    public void should_count_with_named_query() {
        // given
        final String name = "testCountWithNamedQuery";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        long result = dao.queryResultWithNamed(name).count();

        // then
        assertEquals(2L, result);
    }

    @Before
    public void setup() {
        builder = new SimpleBuilder(entityManager);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
