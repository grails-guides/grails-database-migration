package example

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PersonSpec extends Specification implements DomainUnitTest<Person> {

    void 'name is required'() {
        when:
        def person = new Person(age: 30)

        then:
        !person.validate()
        person.errors['name'].code == 'nullable'
    }

    void 'age is required'() {
        when:
        def person = new Person(name: 'Ada')

        then:
        !person.validate()
        person.errors['age'].code == 'nullable'
    }

    void 'valid person passes constraints'() {
        expect:
        new Person(name: 'Ada', age: 36).validate()
    }
}
