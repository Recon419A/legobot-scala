/**
  * Created by drew on 8/9/16.
  */

abstract class Lego (private var children: List[Lego]) extends Endpoint {
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
