package com.sumit.userdirectory.feature.users.data.mapper

import com.sumit.userdirectory.feature.users.data.remote.dto.AddressDto
import com.sumit.userdirectory.feature.users.data.remote.dto.CompanyDto
import com.sumit.userdirectory.feature.users.data.remote.dto.UserDto
import com.sumit.userdirectory.feature.users.domain.model.Address
import com.sumit.userdirectory.feature.users.domain.model.Company
import com.sumit.userdirectory.feature.users.domain.model.User

object UserDtoMapper {
    fun map(dto: UserDto): User {
        return User(
            id = dto.id ?: 0,
            name = dto.name.orEmpty(),
            username = dto.username.orEmpty(),
            email = dto.email.orEmpty(),
            phone = dto.phone.orEmpty(),
            website = dto.website.orEmpty(),
            address = dto.address.toDomain(),
            company = dto.company.toDomain(),
        )
    }

    private fun AddressDto?.toDomain(): Address {
        val street = this?.street.orEmpty()
        val suite = this?.suite.orEmpty()
        val city = this?.city.orEmpty()
        val zipcode = this?.zipcode.orEmpty()

        return Address(
            street = street,
            suite = suite,
            city = city,
            zipcode = zipcode,
            fullAddress = buildFullAddress(street, suite, city, zipcode),
        )
    }

    private fun CompanyDto?.toDomain(): Company {
        return Company(
            name = this?.name.orEmpty(),
            catchPhrase = this?.catchPhrase.orEmpty(),
        )
    }

    private fun buildFullAddress(
        street: String,
        suite: String,
        city: String,
        zipcode: String,
    ): String {
        val cityLine = listOf(city, zipcode)
            .filter(String::isNotBlank)
            .joinToString(separator = " ")

        return listOf(street, suite, cityLine)
            .filter(String::isNotBlank)
            .joinToString(separator = ", ")
    }
}
