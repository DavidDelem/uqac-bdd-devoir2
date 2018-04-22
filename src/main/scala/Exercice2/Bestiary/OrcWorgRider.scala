package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class OrcWorgRider (position: Position, id: Int)
  extends LivingEntity (
    id,
    "Orc worg Rider",
    13,
    13,
    18,
    position,
    "BadGuys",
    0,
    List(("ft", 3)),
    new Attack("mwk battleaxe", 4, 12, List(6), 5),
    new Attack("shortbow", 1, 6, List(4), 110)
  )
{

}
