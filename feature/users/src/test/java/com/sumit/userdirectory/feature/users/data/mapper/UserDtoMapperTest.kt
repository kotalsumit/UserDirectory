package com.sumit.userdirectory.feature.users.data.mapper

import com.sumit.userdirectory.feature.users.data.remote.dto.AddressDto
import com.sumit.userdirectory.feature.users.data.remote.dto.CompanyDto
import com.sumit.userdirectory.feature.users.data.remote.dto.UserDto
import kotlin.test.Test
import kotlin.test.assertEquals

class UserDtoMapperTest {
    @Test
    fun `maps complete UserDto to User domain model`() {
        val dto = UserDto(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "leanne@example.com",
            phone = "1-770-736-8031",
            website = "hildegard.org",
            address = AddressDto(
                street = "Kulas Light",
                suite = "Apt. 556",
                city = "Gwenborough",
                zipcode = "92998-3874",
            ),
            company = CompanyDto(
                name = "Romaguera-Crona",
                catchPhrase = "Multi-layered client-server neural-net",
                bs = "harness real-time e-markets",
            ),
        )

        val user = UserDtoMapper.map(dto)

        assertEquals(1, user.id)
        assertEquals("Leanne Graham", user.name)
        assertEquals("Bret", user.username)
        assertEquals("leanne@example.com", user.email)
        assertEquals("1-770-736-8031", user.phone)
        assertEquals("hildegard.org", user.website)
        assertEquals("Kulas Light", user.address.street)
        assertEquals("Apt. 556", user.address.suite)
        assertEquals("Gwenborough", user.address.city)
        assertEquals("92998-3874", user.address.zipcode)
        assertEquals("Kulas Light, Apt. 556, Gwenborough 92998-3874", user.address.fullAddress)
        assertEquals("Romaguera-Crona", user.company.name)
        assertEquals("Multi-layered client-server neural-net", user.company.catchPhrase)
    }

    @Test
    fun `handles null fields safely`() {
        val user = UserDtoMapper.map(UserDto())

        assertEquals(0, user.id)
        assertEquals("", user.name)
        assertEquals("", user.username)
        assertEquals("", user.email)
        assertEquals("", user.phone)
        assertEquals("", user.website)
        assertEquals("", user.address.street)
        assertEquals("", user.address.suite)
        assertEquals("", user.address.city)
        assertEquals("", user.address.zipcode)
        assertEquals("", user.address.fullAddress)
        assertEquals("", user.company.name)
        assertEquals("", user.company.catchPhrase)
    }

    @Test
    fun `builds readable address text from address parts`() {
        val dto = UserDto(
            address = AddressDto(
                street = "Victor Plains",
                suite = "Suite 879",
                city = "Wisokyburgh",
                zipcode = "90566-7771",
            ),
        )

        val user = UserDtoMapper.map(dto)

        assertEquals("Victor Plains, Suite 879, Wisokyburgh 90566-7771", user.address.fullAddress)
    }

    @Test
    fun `maps company safely when missing`() {
        val user = UserDtoMapper.map(UserDto(company = null))

        assertEquals("", user.company.name)
        assertEquals("", user.company.catchPhrase)
    }
}
