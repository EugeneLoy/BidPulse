package org.bidpulse.server

import spray.http.MediaTypes._
import spray.routing._


// this trait defines our service behavior independently from the service actor
trait RoutedService extends HttpService {

  val route =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Hello</h1>
              </body>
            </html>
          }
        }
      }
    } ~ {
      getFromResourceDirectory("webapp")
    }

}





