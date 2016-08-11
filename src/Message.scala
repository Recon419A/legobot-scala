/**
  * Created by drew on 8/10/16.
  */
case class Message(text: String, metadata: Metadata) {}

case class Metadata(source: Endpoint, destination: Endpoint) {}
