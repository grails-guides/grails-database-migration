package grails.database.migration

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

        for ( Map<String, Object> s : persons ) {
            new Person(name: s.name, age: s.age).save(flush: true)
        }
    }

    def destroy = {
    }
}
