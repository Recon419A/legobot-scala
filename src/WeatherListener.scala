/**
  * Created by drew on 8/9/16.
  */
class WeatherListener extends Lego {
  class ZipCode extends Lego {
    override def self_listening_for(msg: Message): Boolean = {
      msg.text.contains("90210")
    }

    override def own_response(msg: Message): List[Message] = {
      List(new Message("The weather is sunny. It's California.", None))
    }
  }

  override def self_listening_for(msg: Message): Boolean = {
    msg.text.contains("!weather")
  }

  override def own_response(msg: Message): List[Message] = {
    children = new ZipCode() :: children
    List(new Message("Please enter a zipcode:", None))
  }
}
