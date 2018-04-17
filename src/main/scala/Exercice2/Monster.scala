package Exercice2

class Monster() extends LivingEntity {

  protected var armor = 0
  protected var regeneration = 0

  //Type de vitesse, vitesse
  protected var speeds = List.empty[(String, Int)]

  //Nom attaque, Précisions, Dommages[Min, Max], Distance à laquelle il peut utiliser l'attaque
  protected var melee: (String, List[Int], List[Int], Int) = ("", List.empty[Int], List.empty[Int], 0)

  //Nom attaque, Précisions, Dommages[Min, Max],  Distance à laquelle il peut utiliser l'attaque
  protected var ranged: (String, List[Int], List[Int], Int) = ("", List.empty[Int], List.empty[Int], 0)

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Armor : " + armor + ", Regeneration : " + regeneration + "Speeds : " + speeds

}