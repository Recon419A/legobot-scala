import scala.collection.mutable

/**
  * Created by drew on 8/10/16.
  */
abstract class Connector extends Endpoint {
  final def poll: List[Message] = {
    outbox.dequeueAll(message => true).toList
  }

  override def listening_for(msg: Message): Boolean = {
    msg.metadata.destination == this
  }
}

class NullConnector extends Connector {
  override def handle(msg: Message): Unit = {}
}
