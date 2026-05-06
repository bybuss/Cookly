package bob.colbaskin.cookly.favourite.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesChangeNotifier @Inject constructor() {

    private val _changes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val changes = _changes.asSharedFlow()

    fun notifyChanged() { _changes.tryEmit(Unit) }
}
