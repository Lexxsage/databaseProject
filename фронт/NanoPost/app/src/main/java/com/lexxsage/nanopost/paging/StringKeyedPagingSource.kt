package com.lexxsage.nanopost.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lexxsage.nanopost.network.model.PagedDataResponse
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class StringKeyedPagingSource<T : Any>(
    private val dataLoader: suspend (String?) -> PagedDataResponse<T>,
) : PagingSource<String, T>() {

    override suspend fun load(params: LoadParams<String>) = try {
        val response = dataLoader(params.key)
        LoadResult.Page(
            data = response.items,
            prevKey = null,
            nextKey = response.nextFrom,
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<String, T>): String? = null
}
