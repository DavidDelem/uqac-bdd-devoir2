package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class GreataxeOrc  (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Greataxe Orc",
    42,
    42,
    15,
    position,
    "BadGuys",
    0,
    List(("ft", 5)),
    new Attack("greathaxe", 11, 22, List(11), 5),
    new Attack("throwing haxe", 8, 13, List(5), 110),
    null
  ){
}
