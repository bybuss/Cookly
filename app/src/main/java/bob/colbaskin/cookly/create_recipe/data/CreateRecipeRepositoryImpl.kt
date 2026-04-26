package bob.colbaskin.cookly.create_recipe.data

import android.content.Context
import android.util.Log
import bob.colbaskin.cookly.common.ApiResult
import bob.colbaskin.cookly.common.utils.safeApiCall
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCommand
import bob.colbaskin.cookly.create_recipe.domain.CreateRecipeRepository
import bob.colbaskin.cookly.create_recipe.domain.models.toDto
import javax.inject.Inject

private const val TAG = "CreateRecipeRepositoryImpl"


class CreateRecipeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val apiService: CreateRecipeApiService
) : CreateRecipeRepository {

    override suspend fun submitRecipe(command: CreateRecipeCommand): ApiResult<Int> {
        Log.d(TAG, "Recipe has been submitted.")
        var createdRecipeId: Int? = null

        val result = safeApiCall(
            apiCall = {
                apiService.createRecipe(command.toDto())
            },
            successHandler = { createdRecipe ->
                createdRecipeId = createdRecipe.id

                command.mainPhoto?.let { image ->
                    apiService.uploadRecipeAvatar(
                        recipeId = createdRecipe.id,
                        file = createMultipartBodyPart(
                            context = context,
                            uri = image.uri,
                            fileName = image.fileName,
                            mimeType = image.mimeType
                        )
                    )
                }

                val serverStepsByNumber = createdRecipe.steps.associateBy { it.number }

                command.steps.forEach { step ->
                    val image = step.image ?: return@forEach

                    val serverStep = serverStepsByNumber[step.number]
                        ?: error("Сервер не вернул шаг с number=${step.number}")

                    apiService.uploadRecipeStepAvatar(
                        recipeId = createdRecipe.id,
                        recipeStepId = serverStep.id,
                        file = createMultipartBodyPart(
                            context = context,
                            uri = image.uri,
                            fileName = image.fileName,
                            mimeType = image.mimeType
                        )
                    )
                }

                createdRecipe.id
            },
            context = context
        )

        return when {
            result is ApiResult.Error && createdRecipeId != null -> {
                Log.e(TAG, "Recipe created with id=$createdRecipeId, but uploading photo failed. Error: ${result.text}")
                ApiResult.Error(
                    title = "Рецепт создан не полностью",
                    text = "Рецепт создан, но не удалось загрузить фото."
                )
            }

            else -> result
        }
    }
}
