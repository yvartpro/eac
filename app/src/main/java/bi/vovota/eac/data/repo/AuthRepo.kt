package bi.vovota.eac.data.repo

import bi.vovota.eac.data.remote.dto.AuthLogin
import bi.vovota.eac.data.remote.dto.AuthRegister
import bi.vovota.eac.data.remote.dto.AuthResponse

interface AuthRepo {
    suspend fun register(request: AuthRegister): Result<Unit>
    suspend fun login(request: AuthLogin): Result<AuthResponse>
}