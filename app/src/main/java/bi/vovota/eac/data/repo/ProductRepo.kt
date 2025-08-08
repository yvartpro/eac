package bi.vovota.eac.data.repo

import bi.vovota.eac.model.Product


interface ProductRepo {
    suspend fun getProducts(): Result<List<Product>>
}