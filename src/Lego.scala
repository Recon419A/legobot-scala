/**
  * Created by drew on 8/9/16.
  */
import scala.collection.mutable

class Message(val text: String, val metadata: Object) {}

class Metadata(val connector: Connector, val lego: Lego) {}

abstract class Lego (var children: List[Lego] = Nil,
                     var outbox: mutable.Queue[Message] =
                       new mutable.Queue[Message]) {

  def self_listening_for(msg: Message): Boolean {}

  def self_handle(msg: Message): Unit {}

  final def handle(msg: Message): Unit = {
    children.foreach(child => child.handle(msg))
    if (self_listening_for(msg)) {
      val getter_thread = new HandlerThread(self_handle, msg)
      getter_thread.run()
    }
  }

  final def poll(): List[Message] = {
    val child_messages = children.flatMap(child => child.poll())
    val own_messages = outbox.dequeueAll(message => true).toList
    own_messages ::: child_messages
  }
}

class HandlerThread(val handler: (Message => Unit),
                    val msg: Message) extends Runnable {
  def run() = {
    handler(msg)
  }
}
