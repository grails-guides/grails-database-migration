package grails.database.migration

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Address {
    String name
    int age
    String streetName
    String city
    String zipCode

    String toString() {
        name
    }

    static constraints = {
        age(nullable: true)
        streetName(nullable: true)
        city(nullable: true)
        zipCode(nullable: true)
    }
}
