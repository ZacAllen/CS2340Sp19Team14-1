package utils

import java.sql._
import java.text.SimpleDateFormat
import java.util.Calendar

object SQLDriver {

  // Get connection to database
  val myConn: Connection = DriverManager.getConnection("jdbc:mysql://127.0.0.2:3306/world", "root", "root")

  // Simple DB statement
  val myStatement: Statement = myConn.createStatement()

  var statement: String = ""

  def createGame(): Int = {
    val currDateTime: String = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").
      format(Calendar.getInstance().getTime)
    statement = "INSERT INTO game " + " (timestamp) " +
      String.format(" VALUES ('%s')", currDateTime)
    myStatement.executeUpdate(statement)
    val checkerStatement = "SELECT MAX(id) FROM game"
    val checkerResult = myStatement.executeQuery(checkerStatement)
    checkerResult.getInt(0)
  }

  def createPlayer(gameID: Int, playerName: String, playerEmail: String, turnNum: Int): Int = {
    val currDateTime: String = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").
      format(Calendar.getInstance().getTime)
    statement = "INSERT INTO player " + " (name, email, turn, game_id, timestamp) " +
      String.format(" VALUES ('%s', '%s', %d, %d, '%s')", playerName, playerEmail, turnNum, gameID, currDateTime)
    myStatement.executeUpdate(statement)
    val checkerStatement = "SELECT MAX(id) FROM player"
    val checkerResult = myStatement.executeQuery(checkerStatement)
    checkerResult.getInt(0)
  }

  def createTerritory(gameID: Int, playerID: Int, territoryActID: Int): Int = {
    statement = "INSERT INTO territory " + " (game_id, player_id, territory_act_id, army) " +
      String.format(" VALUES (%d, %d, %d, 1)", gameID, playerID, territoryActID)
    myStatement.executeUpdate(statement)
    val checkerStatement = "SELECT MAX(id) FROM territory"
    val checkerResult = myStatement.executeQuery(checkerStatement)
    checkerResult.getInt(0)
  }

  def updateTerritory(territoryID: Int, playerID: Int, numArmy: Int): Unit = {
    statement = "UPDATE territory " +
      String.format(" SET player_id = %d, army = %d ", playerID, numArmy) +
      String.format(" WHERE id = %d", territoryID)
    myStatement.executeUpdate(statement)
  }

  def getTerritories(gameID: Int): Map[Int, (Int, Int)] = {
    statement = "SELECT * FROM territory " +
      String.format(" WHERE game_id = %d", gameID)
    val checkerResult = myStatement.executeQuery(statement)
    val territoryMap: Map[Int, (Int, Int)] = Map.empty[Int, (Int, Int)]
    while (checkerResult.next()) {
      territoryMap(checkerResult.getInt("territory_act_id")) = (checkerResult.getInt("player_id"), checkerResult.getInt("army"))
    }
    checkerResult.close()
    territoryMap
  }

  def getPlayers(gameID: Int): Map[Int, (String, String, Int)] = {
    statement = "SELECT * FROM player " +
      String.format(" WHERE game_id = %d", gameID)
    val checkerResult = myStatement.executeQuery(statement)
    val playerMap: Map[Int, (String, String, Int)] = Map.empty[Int, (String, String, Int)]
    while (checkerResult.next()) {
      playerMap(checkerResult.getInt("turn")) = (checkerResult.getString("name"), checkerResult.getString("email"), checkerResult.getString("id"))
    }
    checkerResult.close()
    playerMap
  }

  def getPlayerDetails(id: Int): Map[Int, (String, String, Int)] = {
    statement = "SELECT * FROM player " +
      String.format(" WHERE id = %d", id)
    val checkerResult = myStatement.executeQuery(statement)
    val playerMap: Map[Int, (String, String, Int)] = Map.empty[Int, (String, String, Int)]
    while (checkerResult.next()) {
      playerMap(checkerResult.getInt("turn")) = (checkerResult.getString("name"), checkerResult.getString("email"), checkerResult.getString("id"))
    }
    checkerResult.close()
    playerMap
  }

  def getTerritoryDetails(gameID: Int, territoryActID: Int): Map[Int, (Int, Int)] = {
    statement = "SELECT * FROM territory " +
      String.format(" WHERE game_id = %d AND territory_act_id = %d", gameID, territoryActID)
    val checkerResult = myStatement.executeQuery(statement)
    val territoryMap: Map[Int, (Int, Int)] = Map.empty[Int, (Int, Int)]
    while (checkerResult.next()) {
      territoryMap(checkerResult.getInt("territory_act_id")) = (checkerResult.getInt("player_id"), checkerResult.getInt("army"))
    }
    checkerResult.close()
    territoryMap
  }

  def increaseTurns(gameID: Int): Unit = {
    statement = "SELECT num_turns FROM game " + String.format(" WHERE id=%d", gameID)
    val checkerResult = myStatement.executeQuery(statement)
    statement = "UPDATE game " +
      String.format(" SET num_turns = %d ", checkerResult.getInt("num_turns") + 1) +
      String.format(" WHERE id=%d", gameID)
    myStatement.executeUpdate(statement)
    checkerResult.close()
  }

  def getPlayerTerritories(gameID: Int, playerID: Int): List[Int] = {
    statement = "SELECT * FROM territory " +
      String.format(" WHERE game_id = %d AND player_id = %d", gameID, playerID)
    val checkerResult = myStatement.executeQuery(statement)
    var territoryList: List[Int] = Nil
    while (checkerResult.next()) {
      territoryList = checkerResult.getInt("territory_act_id") :: territoryList
    }
    checkerResult.close()
    territoryList
  }
}
