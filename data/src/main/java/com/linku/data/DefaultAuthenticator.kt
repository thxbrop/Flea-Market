package com.linku.data

import com.linku.data.usecase.SettingUseCase
import com.linku.domain.Authenticator
import kotlinx.coroutines.flow.*

class DefaultAuthenticator(
    private val settings: SettingUseCase
) : Authenticator {
    override val currentUID: Int?
        get() = try {
            settings.currentUID
        } catch (e: NumberFormatException) {
            null
        }

    override val token: String?
        get() {
            if (currentUID == null) {
                settings.token = null
            }
            return settings.token
        }

    private val _observeCurrent = MutableStateFlow(currentUID)
    override val observeCurrent: Flow<Int?> = _observeCurrent
        .onEach {
            print(it)
        }

    override suspend fun update(
        uid: Int?,
        token: String?
    ) {
        settings.currentUID = uid
        settings.token = token
        _observeCurrent.emit(uid)
    }

}