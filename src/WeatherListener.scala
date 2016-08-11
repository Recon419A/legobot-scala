/**
  * Created by drew on 8/9/16.
  */
class WeatherListener extends Lego {
  class ZipCode extends Lego {
    override def self_listening_for(msg: Message): Boolean = {
      msg.text.contains("90210")
    }

    override def self_handle(msg: Message): Unit = {
      val metadata = Metadata(this, msg.metadata.source)
      outbox.enqueue(Message("The weather is sunny.", metadata))
      finished = true
    }
  }

  override def self_listening_for(msg: Message): Boolean = {
    msg.text.contains("!weather")
  }

  override def self_handle(msg: Message): Unit = {
    children = new ZipCode() :: children
    val metadata = Metadata(this, msg.metadata.source)
    outbox.enqueue(Message("Please enter a zipcode:", metadata))
  }
}
