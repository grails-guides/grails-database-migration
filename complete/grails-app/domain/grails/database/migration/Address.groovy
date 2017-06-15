package grails.database.migration

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Address {
    Person person
    String streetName
    String city
    String zipCode

    static belongsTo = [person: Person]

    String toString() {
        "${streetName}, ${city}, ${zipCode}"
    }

    static constraints = {
        streetName(nullable: true)
        city(nullable: true)
        zipCode(nullable: true)
    }
}
