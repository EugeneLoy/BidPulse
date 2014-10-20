package org.bidpulse

import org.scalatest.{Matchers, WordSpec}
import spray.testkit.{ScalatestRouteTest, Specs2RouteTest}
import spray.http._
import StatusCodes._

class ServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with Service {
  def actorRefFactory = system
  
  "Service" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        responseAs[String] should include ("Hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled should be (false)
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status should equal (MethodNotAllowed)
        responseAs[String] should equal ("HTTP method not allowed, supported methods: GET")
      }
    }
  }
}
