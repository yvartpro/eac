package bi.vovota.eac.data.repo

import bi.vovota.eac.data.remote.ApiService
import bi.vovota.eac.model.Product

class ProductRepoImpl( private val api: ApiService): ProductRepo {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = api.getProducts()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}