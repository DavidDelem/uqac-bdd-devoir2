package Exercice2.Bestiary

import Exercice2.{Attack, Monster}
import Exercice2.Utils.Position

class AstralDeva (position: Position, id: Int)
  extends Monster(
    "Astral Deva",
    172,
    29,
    position,
    0,
    List(("ft", 50), ("fly", 100)),
    new Attack("+2 disrupting warhammer", 15, 22, List(26, 21, 16), 5),
    null,
    id,
    "GoodGuys"
  ){
}
