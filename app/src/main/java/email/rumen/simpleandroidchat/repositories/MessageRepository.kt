package email.rumen.simpleandroidchat.repositories

import android.util.Log
import androidx.lifecycle.viewModelScope
import email.rumen.simpleandroidchat.api.ApiService
import email.rumen.simpleandroidchat.model.MessageRealm
import io.realm.Realm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import javax.inject.Inject
import kotlin.random.Random

class MessageRepository @Inject constructor(
    private val apiService: ApiService,
    private var realmDatabase: Realm
) {
    suspend fun getPost(id: Int) {
        val post = apiService.getPost(id)

        when (post.isSuccessful) {
            true -> {
                delay(Random.nextLong(0, 3000))
                if (System.currentTimeMillis() - (realmDatabase.where(MessageRealm::class.java).findAll().lastOrNull()?.timestamp ?: 0) > 60 * 60 * 1000) {
                    realmDatabase.executeTransactionAsync { r: Realm ->
                        val realmId = ObjectId()
                        val messageRealm = MessageRealm(
                            realmId,
                            System.currentTimeMillis(),
                            true,
                            post.body()?.body ?: "<empty message received>",
                            true
                        )
                        r.insertOrUpdate(messageRealm)
                    }

                } else {
                    realmDatabase.executeTransactionAsync { r: Realm ->
                        val realmId = ObjectId()
                        val messageRealm = MessageRealm(
                            realmId,
                            System.currentTimeMillis(),
                            true,
                            post.body()?.body ?: "<empty message received>",
                            false
                        )
                        r.insertOrUpdate(messageRealm)
                    }
                }
            }
            else -> {
                Log.e("MainActivityViewModel", post.message())
            }
        }
    }

    fun setPost(message: MessageRealm) {
        if (message.timestamp - (realmDatabase.where(MessageRealm::class.java).findAll().lastOrNull()?.timestamp ?: 0)  > 60 * 60 * 1000) {
            message.timestampIt = true
        }

        realmDatabase.executeTransactionAsync { r: Realm ->
            val id = ObjectId()
            val messageRealm = MessageRealm(
                id,
                message.timestamp,
                false,
                message.message,
                message.timestampIt
            )
            r.insertOrUpdate(messageRealm)
        }
    }
}