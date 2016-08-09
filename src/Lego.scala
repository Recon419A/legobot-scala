/**
  * Created by drew on 8/9/16.
  */
class Message(val text: String, val metadata: Object) {}

abstract class Lego (var children: List[Lego] = Nil) {
  def self_listening_for(msg: Message): Boolean {}

  def own_response(msg: Message): List[Message] {}

  final def get_responses(msg: Message): List[Message] = {
    val child_responses = children.flatMap(child => child.get_responses(msg))
    if (self_listening_for(msg)) {
      // implement thread for own_response call, since this is what can
      // take some time
      val thread1 = new Thread()
      own_response(msg) ::: child_responses
    } else {
      child_responses
    }
  }
}
