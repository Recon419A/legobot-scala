/**
  * Created by drew on 8/9/16.
  */
class Message(val text: String, val metadata: Object) {}

abstract class Lego (var children: List[Lego] = Nil) {
  def self_listening_for(msg: Message): Boolean {}

  def get_own_response(msg: Message): List[Message] {}

  final def get_responses(msg: Message): List[Message] = {
    val child_responses = children.flatMap(child => child.get_responses(msg))
    if (self_listening_for(msg)) {
      get_own_response(msg) ::: child_responses
    } else {
      child_responses
    }
  }
}

class WeatherListener extends Lego {
  class ZipCode extends Lego {
    override def self_listening_for(msg: Message): Boolean = {
      msg.text.contains("90210")
    }

    override def get_own_response(msg: Message): List[Message] = {
      List(new Message("The weather is sunny. It's California.", None))
    }
  }

  override def self_listening_for(msg: Message): Boolean = {
    msg.text.contains("!weather")
  }

  override def get_own_response(msg: Message): List[Message] = {
    children = new ZipCode() :: children
    List(new Message("Please enter a zipcode:", None))
  }
}

