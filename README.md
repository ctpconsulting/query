CDI Query
=========

CDI Query is a CDI extension which allows creating JPA queries based on method names or method annotations. 
For a more detailed introduction into the extension, just have a look at the project website at http://ctpconsulting.github.com/query

### Code Sample

public interface PersonDao extends EntityDao<Person, Long> {

    List<Person> findByAgeBetweenAndGender(int minAge, int maxAge, Gender gender);

    @Query("select p from Person p where p.ssn = ?1")
    Person findBySSN(String ssn);

    @Query(named=Person.BY_FULL_NAME)
    Person findByFullName(String firstName, String lastName);

}