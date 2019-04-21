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

  def createPlayer(gameID: Int, playerName: String, playerEmail: String): Int = {
    val currDateTime: String = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").
      format(Calendar.getInstance().getTime)
    statement = "INSERT INTO player " + " (name, email, game_id, timestamp) " +
      String.format(" VALUES ('%s', '%s', %d, '%s')", playerName, playerEmail, gameID, currDateTime)
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
}
