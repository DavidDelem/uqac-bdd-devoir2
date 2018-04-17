package Exercice2

abstract class Monster() extends LivingEntity {

  protected var regeneration : Int = 0
  //Type de vitesse, vitesse
  protected var speeds: List[(String, Int)] = List()
  protected var melee: Attack = null
  protected var ranged: Attack = null

  def attack(target: LivingEntity, distance: Int) : Unit = {

    var attack: Attack = null

    if(distance <= melee.minDistance) attack = melee
    else if(distance <= ranged.minDistance) attack = ranged
    else return

    val damage = attack.getDamage
    if(damage >= target.armor) target.takeDamage(damage)

  }

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Armor : " + armor + ", Regeneration : " + regeneration + "Speeds : " + speeds

}