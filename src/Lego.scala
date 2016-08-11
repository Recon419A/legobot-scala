abstract class Lego (protected var children: List[Lego] = Nil,
                     protected var finished: Boolean = false) extends Endpoint {
  def self_listening_for(msg: Message): Boolean

  def self_handle(msg: Message): Unit

  override final def listening_for(msg: Message): Boolean = {
    self_listening_for(msg) || children.exists(child => child.listening_for(msg))
  }

  final def handle(msg: Message): Unit = {
    children.foreach(child => child.handle(msg))
    if (self_listening_for(msg)) {
      val getter_thread = new HandlerThread(self_handle, msg)
      getter_thread.run()
    }
  }

  final def poll: List[Message] = {
    val child_messages = children.flatMap(child => child.poll)
    val own_messages = outbox.dequeueAll(message => true).toList
    // children that are finished have just had their final messages collected,
    // so clean them up
    children = children.filter(child => !child.finished)
    own_messages ::: child_messages
  }
}

class HandlerThread(val handler: (Message => Unit),
                    val msg: Message) extends Runnable {
  def run() = {
    handler(msg)
  }
}
