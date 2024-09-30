package com.mospolytech.data.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.impl.JWTParser
import com.auth0.jwt.interfaces.Payload
import com.mospolytech.domain.auth.AccountModel
import com.mospolytech.domain.auth.AccountsModel
import com.mospolytech.domain.auth.GetJwtRefreshTokenUseCase
import com.mospolytech.domain.auth.GetJwtTokenUseCase
import com.mospolytech.domain.auth.TokenModel
import io.ktor.util.*
import java.math.BigInteger
import java.security.MessageDigest

class AccountsDtoMapper(
    private val getJwtTokenUseCase: GetJwtTokenUseCase,
    private val getJwtRefreshTokenUseCase: GetJwtRefreshTokenUseCase,
) {

    fun AccountsResponse.toModel(guid: String?): AccountsModel {
        val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
        val isStudent = user.isStudent()
        val description =
            if (isStudent) {
                "${user.group} • ${user.degreeLevel?.lowercase() ?: "студент"}"
            } else {
                if (user.workPlace == null) {
                    "Сотрудник"
                } else {
                    "Сотрудник • ${user.workPlace}"
                }
            }

        val id = generateId(guid)
        val current = AccountModel(
            id = id,
            name = fullName,
            description = description,
            avatar = this.user.avatar.ifEmpty { null },
            accessToken = null,
            refreshToken = null,
        )

        val accounts = buildList {
            add(current)

            user.accounts?.forEach { account ->
                add(account.toModel(guid))
            }
        }

        return AccountsModel(
            current = current.id,
            accounts = accounts,
        )
    }

    private fun AccountsResponse.Account.toModel(
        guid: String?,
    ): AccountModel {
        val fullName = this.fio
        val jwt = this.jwt ?: this.jwtRefresh
        val guid2 = jwt?.parseJwtGuid() ?: guid
        val id = generateId(guid2)
        val isStudent = isStudent()

        val token = toTokenModel(guid)

        val accessToken = getJwtTokenUseCase(token)
        val refreshToken = getJwtTokenUseCase(token)

        val description =
            if (isStudent) {
                "$group • ${degreeLevel?.lowercase() ?: "студент"}"
            } else {
                if (workPlace == null) {
                    "Сотрудник"
                } else {
                    "Сотрудник • $workPlace"
                }
            }

        return AccountModel(
            id = id,
            name = fullName,
            description = description,
            avatar = this.avatar,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun AccountsResponse.generateId(guid: String?): String {
        val fullName = this.user.surname + " " + this.user.name + " " + this.user.patronymic
        val id = guid ?: this.user.id
        val userStatus = this.user.status
        val group = this.user.group.orEmpty()
        val workPlace = this.user.workPlace.orEmpty()

        return md5(id + fullName + userStatus + group + workPlace)
    }

    private fun AccountsResponse.Account.generateId(guid: String?): String {
        val fullName = this.fio
        val jwt = this.jwt ?: this.jwtRefresh ?: guid
        val id = jwt?.parseJwtGuid().orEmpty()
        val userStatus = this.status
        val group = this.group.orEmpty()
        val workPlace = this.workPlace.orEmpty()

        return md5(id + fullName + userStatus + group + workPlace)
    }

    private fun AccountsResponse.User.isStudent(): Boolean {
        return userStatus == "stud"
    }

    private fun AccountsResponse.Account.isStudent(): Boolean {
        return userStatus == "stud"
    }

    private fun AccountsResponse.User.isStaff(): Boolean {
        return userStatus == "staff"
    }

    private fun AccountsResponse.Account.isStaff(): Boolean {
        return userStatus == "staff"
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun AccountsResponse.Account.toTokenModel(guid: String?): TokenModel {
        var guid: String? = guid

        if (guid == null) {
            try {
                val jwt = jwt ?: jwtRefresh
                guid = jwt?.parseJwtGuid()
            } catch (_: Exception) {
            }
        }

        return TokenModel(
            token = token,
            guid = guid,
            jwt = jwt,
            jwtRefresh = jwtRefresh,
        )
    }

    private val jwtParser = JWTParser()

    private fun String.parseJwtGuid(): String? {
        return parseJwt().getClaim("IndividualGuid").asString()
    }

    private fun String.parseJwt(): Payload =
        JWT.decode(this)
            .payload
            .decodeBase64String()
            .let { jwtParser.parsePayload(it) }
}
