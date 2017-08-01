package demo

import demo.Person
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if ( Environment.current == Environment.DEVELOPMENT ) {
            initPersons()
        }
    }

    static void initPersons() {
        List<Map<String, Object>> persons = [
                [name: 'Nirav', age: 39],
                [name: 'Jeff', age: 45],
                [name: 'Sergio', age: 36]
        ]

        for ( Map<String, Object> p : persons ) {
            // only create person if it doesn't exist
            Person.findByName(p.name) ?: new Person(name: p.name, age: p.age).save(flush: true)
        }
    }

    def destroy = {
    }
}
