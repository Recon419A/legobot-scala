/**
  * Created by drew on 8/10/16.
  */
class Message(val text: String, val metadata: Metadata) {}

class Metadata(val source: Endpoint, val destination: Endpoint) {}
