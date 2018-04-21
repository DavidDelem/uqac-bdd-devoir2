package Exercice2.Bestiary

import Exercice2.{Attack, Monster}
import Exercice2.Utils.Position

class MovanicDeva (position: Position, id: Int)
  extends Monster(
    "Movanic Deva",
    126,
    24,
    position,
    0,
    List(("ft", 40), ("fly", 60)),
    new Attack("+1 flaming greatsword", 8, 13, List(17, 12, 7), 5),
    null,
    id,
    "GoodGuys"
  ){
}
