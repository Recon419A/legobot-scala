import scala.collection.mutable

/**
  * Created by drew on 8/10/16.
  */
abstract class Connector(var inbox: mutable.Queue[Message] ) {
  final def poll(): List[Message] = {
    inbox.dequeueAll(message => true).toList
  }
}
