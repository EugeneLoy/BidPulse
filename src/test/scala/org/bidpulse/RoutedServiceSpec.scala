package org.bidpulse

import org.scalatest.{Matchers, WordSpec}
import org.bidpulse.server.RoutedService
import spray.testkit.{ScalatestRouteTest, Specs2RouteTest}
import spray.http._
import StatusCodes._

class RoutedServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with RoutedService {
  def actorRefFactory = system
  
  "Service" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> route ~> check {
        responseAs[String] should include ("Hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> route ~> check {
        handled should be (false)
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(route) ~> check {
        status should equal (MethodNotAllowed)
        responseAs[String] should equal ("HTTP method not allowed, supported methods: GET")
      }
    }
  }
}
