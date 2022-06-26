package email.rumen.simpleandroidchat.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId

@RealmClass
open class MessageRealm : RealmModel {
    @PrimaryKey
    var _id : ObjectId = ObjectId()
    var timestamp: Long = 0L
    var isMuzMatch: Boolean = false
    var message: String = ""
    var timestampIt: Boolean = false

    constructor(_id: ObjectId, timestamp: Long, isMuzMatch: Boolean, message: String, timestampIt: Boolean) {
        this._id = _id
        this.timestamp = timestamp
        this.isMuzMatch = isMuzMatch
        this.message = message
        this.timestampIt = timestampIt
    }
    constructor()
}