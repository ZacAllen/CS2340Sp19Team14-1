package controllers

import javax.inject._
import models.{JsonConverter, Player, Territory}
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.json.Json._

import scala.util.Random

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
  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    *
    * @return
    */
  def gameInitiate: Action[AnyContent] = Action {
    Ok("Game Initiated")
  }


  def addPlayer = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], "", 0, 0, 0)
    Ok(toJson(Map("id" -> np.getId)))
  def addPlayer: Action[JsValue] = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], "", 0, 0, 0)
    Ok(toJson(Map("id" -> np.getId)))
  }

  def gameInitiator: Action[JsValue] = Action(parse.json) { implicit request =>
    val input: List[JsValue] = (request.body \ "data").as[List[JsValue]]
    val turnOrder: List[Int] = randomizeTurns(input.length)
    var players: List[Map[String, Any]] = Nil
    var num: Int = 0
    val num_armies: Int = initArmiesUnits(input.length)
    for (player_data <- input) {
      val id = (player_data \ "id").as[Int]
      val name = (player_data \ "name").as[String]
      val email = (player_data \ "email").as[String]
      val color = (player_data \ "color").as[Int]
      val turn = turnOrder(num)
      val player = Player(id, name, email, turn, color, num_armies)
      num = num + 1
      val player_map = Map[String, Any]("id" -> player.getId, "name" -> player.getName, "email" -> player.getEmail,
        "color" -> player.getColor, "turn" -> player.getTurn, "numArmies" -> player.getNumArmies)
      players = player_map :: players
    }
    val final_data = JsonConverter.toJson(Map("data" -> players, "status" -> 1))
    Ok(final_data)
  }

  def initArmiesUnits(numPlayers: Int): Int = {
    if (numPlayers == 3) {
      val numArmies = 35
      numArmies
    } else if (numPlayers == 4) {
      val numArmies = 30
      numArmies
    } else if (numPlayers == 5) {
      val numArmies = 25
      numArmies
    } else {
      val numArmies = 20
      numArmies
    }
  }

  def randomizeTurns (playerCount: Int): List[Int] = {
    var turnList: List[Int] = Nil
    var num = 1
    //add 1 - player # to list
    for (_ <- 1 to playerCount) {
      turnList = num :: turnList
      num += 1
    }
    //shuffle
    turnList = util.Random.shuffle(turnList)
    turnList
  }

  //Creates a map that contains all the territory ID's as keys, and the player ID's as values
  //Ex: territoryID: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], playerID: [1, 2]
  //Returns [3: 2, 6: 1, 10: 2, 9: 1, 8: 2, 1: 1, 4: 2, ...]
  def randomizeTerritories(territoryID: List[Int], playerID: List[Int]): Map[Int, Int] = {
    util.Random.shuffle(territoryID)
    util.Random.shuffle(playerID)

    var map:Map[Int,Int] = Map()
    var i = 0
    for (a <- territoryID.indices) {
      map += territoryID(a) -> playerID(i % playerID.length)
      i += 1
    }
    map
  }

  //Returns the # of armies the player is allowed to place at the start of their turn
  def newArmies(playerID: Int, territories: Map[Int, Int]): Int = {
    //Based on the Rules on the Google doc, the # of new armies is the # of territories / 3. If < 3 territories they
    //Get 1 army
    var numTerritories = 0
    for ((key, value) <- territories) {
      if (value == playerID) numTerritories += 1
    }
    if (numTerritories < 3) 1
    else numTerritories / 3
  }

  def placeSingleArmy(territory: Territory): Unit = {
    territory.addUnits(1)
  }

  //Randomizes a player's territories based on the # of armies to add
  def randomizeArmies(territories: List[Territory], numArmies: Int): Unit = {
    var random = new Random
    for (a <- 1 to numArmies) {
      territories(random.nextInt(territories.length)).addUnits(1)
    }
  }

  def setPlayerTurn(players: List[Player], turnList: List[Int]) {
    if (players != Nil && turnList != Nil) {
      players.head.setTurn(turnList.head)
      setPlayerTurn(players.tail, turnList.tail)
    }
  }
}
