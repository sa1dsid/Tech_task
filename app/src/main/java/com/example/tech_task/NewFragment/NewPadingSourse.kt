import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tech_task.api.CharacterRequests
import com.example.tech_task.data.CharacterData

class NewPagingSource(
    private val apiService: CharacterRequests,
    private val name: String? = "rick",
) : PagingSource<Int, CharacterData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterData> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getCharacters(name, page)

            if (response.isSuccessful) {
                val characters = response.body()?.results ?: emptyList()
                val nextPageNumber = if (characters.isNotEmpty()) page + 1 else null


                return LoadResult.Page(
                    data = characters,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPageNumber
                )
            } else {
                LoadResult.Error(Exception("Ошибка загрузки данных (New): ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
