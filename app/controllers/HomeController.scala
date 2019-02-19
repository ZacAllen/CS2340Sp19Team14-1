package controllers

import javax.inject._
import models.Player
import play.api.mvc._
import play.api.libs.json.Json._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    *
    * @return
    */
  def game_initiate = Action {
    Ok("Game Initiated")
  }

  def addPlayer = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], null)
    Ok(toJson(Map("id" -> np.getId())))
  }
}
