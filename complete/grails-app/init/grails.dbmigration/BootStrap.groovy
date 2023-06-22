package grails.dbmigration

import grails.compiler.GrailsCompileStatic
import grails.util.Environment

@GrailsCompileStatic
class BootStrap {

    def init = { servletContext ->

        if (Environment.current == Environment.DEVELOPMENT) {
            initPersons()
        }
    }

    def destroy = {
    }

    static void initPersons() {
        List<Map<String, Object>> personList = [
                [name: "Nirav", age: 39],
                [name: "Jeff", age: 45],
                [name: "Sergio", age: 34]
        ] as List<Map<String, Object>>
        personList
                .each(personInfo -> { Person.countByName((String) personInfo.name) ?: new Person(personInfo).save() })
    }
}
