package Exercice2.Bestiary

import Exercice2.{Attack, Monster}
import Exercice2.Utils.Position

class GreataxeOrc  (position: Position, id: Int)
  extends Monster(
    "Greataxe Orc",
    42,
    15,
    position,
    0,
    List(("ft", 30)),
    new Attack("greathaxe", 11, 12, List(11), 5),
    new Attack("throwing haxe", 8, 13, List(5), 110),
    id,
    "BadGuys"
  ){
}
