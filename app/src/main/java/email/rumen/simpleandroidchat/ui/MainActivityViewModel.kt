package email.rumen.simpleandroidchat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import email.rumen.simpleandroidchat.model.MessageRealm
import email.rumen.simpleandroidchat.repositories.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    realmDatabase: Realm
) : ViewModel() {

    var messages: Flow<RealmResults<MessageRealm>> = realmDatabase.where(MessageRealm::class.java)
        .findAllAsync()
        .toFlow()
        .flowOn(Dispatchers.Main)

    fun getPost(id: Int) {
        viewModelScope.launch {
            messageRepository.getPost(id)
        }
    }

    fun setPost(message: MessageRealm) {
        messageRepository.setPost(message)
    }
}
