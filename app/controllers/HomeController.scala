package controllers

import javax.inject._
import models.{JsonConverter, Player, GameData, Territory}
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.libs.json.Json._
import play.api.data._
import play.api.data.Forms._

import scala.util.Random

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  val gForm = Form(
    mapping(
      "name1" -> nonEmptyText,
      "email1" -> nonEmptyText,
      "name2" -> nonEmptyText,
      "email2" -> nonEmptyText,
      "name3" -> nonEmptyText,
      "email3" -> nonEmptyText,
      "name4" -> text,
      "email4" -> text,
      "name5" -> text,
      "email5" -> text,
      "name6" -> text,
      "email6" -> text
    ) (GameData.apply)(GameData.unapply)
  )

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.start_game(gForm))
  }

  /**
    *
    * @return
    */
  def gameInitiate: Action[AnyContent] = Action { implicit request =>
    gForm.bindFromRequest.fold(
      formWithErrors => {
        print("Not Processed")
        Ok(formWithErrors.toString)
      },

      gameData => {
        var list: List[Map[String, Any]] = Nil
        var playerData: Map[String, Any] = Map("id" -> 1, "name" -> gameData.name1, "email" -> gameData.email1)
        list = playerData :: list
        playerData = Map("id" -> 2, "name" -> gameData.name2, "email" -> gameData.email2)
        list = playerData :: list
        playerData = Map("id" -> 3, "name" -> gameData.name3, "email" -> gameData.email3)
        list = playerData :: list
        if (gameData.name4 != "") {
          playerData = Map("id" -> 4, "name" -> gameData.name4, "email" -> gameData.email4)
          list = playerData :: list
          if (gameData.name5 != "") {
            playerData = Map("id" -> 5, "name" -> gameData.name5, "email" -> gameData.email5)
            list = playerData :: list
            if (gameData.name6 != "") {
              playerData = Map("id" -> 6, "name" -> gameData.name6, "email" -> gameData.email6)
              list = playerData :: list
            }
          }
        }

        gameInitiator(list)
        Ok(views.html.game(players))
      }
    )
  }

  def addPlayer: Action[JsValue] = Action(parse.json) { implicit request =>
    val np = Player((request.body \ "id").as[Int], (request.body \ "name").as[String], "", 0, 0, 0)
    Ok(toJson(Map("id" -> np.getId)))
  }
  var players: List[Map[String, Any]] = Nil

  def gameInitiator(input: List[Map[String, Any]]): String = {
    val turnOrder: List[Int] = randomizeTurns(input.length)

    var num: Int = 0
    val num_armies: Int = initArmiesUnits(input.length)
    for (player_data <- input) {
      val id = player_data("id").asInstanceOf[Int]
      val name = player_data("name").asInstanceOf[String]
      val email = player_data("email").asInstanceOf[String]
      val color = turnOrder(num)
      val turn = turnOrder(num)
      val player = Player(id, name, email, turn, color, num_armies)
      num = num + 1
      val player_map = Map[String, Any]("id" -> player.getId, "name" -> player.getName, "email" -> player.getEmail,
        "color" -> player.getColor, "turn" -> player.getTurn, "numArmies" -> player.getNumArmies)
      players = player_map :: players
    }
    val final_data = JsonConverter.toJson(Map("data" -> players, "status" -> 1))
    final_data
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

  /*
  Creates a list of Territory objects, with ID's 1 to 42, and Names "1" to "42"
   */
  def createTerritories(): List[Territory] = {
    var territoryList: List[Territory] = Nil;
    var inc = 0;
    var name = "1"
    for (inc <- 1 to 42 ) {
      var territory: Territory = new Territory(inc, name, null, 0, null, true)
      territoryList = territory :: territoryList
      var nameTemp = name.toInt
      nameTemp = 1 + nameTemp
      name = nameTemp.toString
    }
    territoryList
  }

  def randomizeTerritories2(territories: List[Territory], playerList: List[Map[String, Any]]):
      List[Map[String, Any]] = {
    util.Random.shuffle(territories)
    util.Random.shuffle(players)

    playerList(1)
    var i = 0
    for (a <- territories.indices) {
      map += territories(a) -> playerID(i % playerID.length)
      i += 1
    }
    map
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
    for ((_, value) <- territories) {
      if (value == playerID) numTerritories += 1
    }
    if (numTerritories < 3) {
      1
    } else {
      numTerritories / 3
    }
  }

  def placeSingleArmy(territory: Territory): Unit = {
    territory.addUnits(1)
  }

  //Randomizes a player's territories based on the # of armies to add
  def randomizeArmies(territories: List[Territory], numArmies: Int): Unit = {
    val random = new Random
    for (_ <- 1 to numArmies) {
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
