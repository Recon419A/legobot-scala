import scala.collection.mutable

/**
  * Created by drew on 8/9/16.
  */
object Legobot {
  def main(args: Array[String]): Unit = {
    val legobot = new Legobot
    val weather = new WeatherListener
    val printer = new CommandLinePrinter
    legobot.endpoints = weather :: legobot.endpoints
    legobot.endpoints = printer :: legobot.endpoints

    while (true) {
      legobot.collect()
      legobot.prepare_routes()
      legobot.distribute()
    }
  }
}

class Legobot(var endpoints: List[Endpoint] = Nil,
              protected val buffer: mutable.Queue[Message] = new mutable.Queue[Message]) {

  def collect() = {
    for (endpoint <- endpoints) {
      for (message <- endpoint.poll) {
        buffer.enqueue(message)
      }
    }
  }

  def prepare_routes() = {
    val messages = buffer.dequeueAll(message => true)
    for (message <- messages) {
      val metadata = message.metadata
      for (endpoint <- endpoints.filter(endpoint => endpoint.listening_for(message))) {
        val new_message = message.copy(metadata = metadata.copy(destination = endpoint))
        buffer.enqueue(new_message)
      }
    }
  }

  def distribute() = {
    val messages = buffer.dequeueAll(message => endpoints.contains(message.metadata.destination))
    messages.foreach(message => message.metadata.destination.handle(message))
  }
}
